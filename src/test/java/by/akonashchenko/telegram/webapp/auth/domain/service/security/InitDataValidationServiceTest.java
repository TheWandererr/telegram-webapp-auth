package by.akonashchenko.telegram.webapp.auth.domain.service.security;

import by.akonashchenko.telegram.webapp.auth.base.UnitTest;
import by.akonashchenko.telegram.webapp.auth.config.model.AuthConfig;
import by.akonashchenko.telegram.webapp.auth.domain.model.InitData;
import by.akonashchenko.telegram.webapp.auth.domain.model.exception.ValidationException;
import by.akonashchenko.telegram.webapp.auth.domain.service.hashing.InitDataHashService;
import by.akonashchenko.telegram.webapp.auth.util.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InitDataValidationServiceTest extends UnitTest {

    private static final Integer AUTH_VALIDITY_AMOUNT_SECONDS = 3;

    private InitDataParser parser;
    private InitDataHashService hashService;

    private InitDataValidationService instance;

    @BeforeEach
    void setUp() {
        AuthConfig authConfig = AuthConfig.builder()
                .validityAmountSeconds(AUTH_VALIDITY_AMOUNT_SECONDS)
                .build();
        parser = new InitDataParser(mapper);
        hashService = new InitDataHashService(new TelegramHashDataHolder(botConfig));
        instance = new InitDataValidationServiceTestWrapper(authConfig, parser, hashService);
    }

    @Test
    void shouldCreateCheckString() {
        // given
        InitData checkStringInitData = InitData
                .builder()
                .raw(
                        Map.of(
                                "key1", "value1",
                                "key2", "value2",
                                Constant.InitData.HASH_KEY, "hashValue"
                        )
                )
                .build();
        // then
        String checkString = assertDoesNotThrow(() -> instance.createCheckString(checkStringInitData));
        assertEquals(
                "key1=value1\nkey2=value2",
                checkString
        );
    }

    @Test
    void shouldValidateCorrectData() {
        // given()
        StringBuilder initDataBuilder = new StringBuilder(INITIAL_INIT_DATA_STRING);
        Instant authDate = Instant.now();
        initDataBuilder.append(Constant.Symbols.AND)
                .append(Constant.InitData.AUTH_DATE_KEY)
                .append(Constant.Symbols.EQUALS)
                .append(authDate.getEpochSecond());
        InitData checkStringInitData = parser.parse(initDataBuilder.toString());
        String checkString = instance.createCheckString(checkStringInitData);
        String hash = hashService.hmacHex(checkString);
        initDataBuilder.append(Constant.Symbols.AND)
                .append(Constant.InitData.HASH_KEY)
                .append(Constant.Symbols.EQUALS)
                .append(hash);
        String initDataString = initDataBuilder.toString();

        // then
        assertDoesNotThrow(() -> instance.validate(initDataString));
    }

    @Test
    void shouldThrownAnExceptionOnInvalidHash() {
        // given()
        StringBuilder initDataBuilder = new StringBuilder(INITIAL_INIT_DATA_STRING);
        // сначала парсим
        InitData checkStringInitData = parser.parse(initDataBuilder.toString());
        String checkString = instance.createCheckString(checkStringInitData);
        // неверный хеш
        String hash = hashService.hmacHex(checkString);
        // а потом вставляем дату входа
        Instant authDate = Instant.now();
        initDataBuilder.append(Constant.Symbols.AND)
                .append(Constant.InitData.AUTH_DATE_KEY)
                .append(Constant.Symbols.EQUALS)
                .append(authDate.getEpochSecond());
        initDataBuilder.append(Constant.Symbols.AND)
                .append(Constant.InitData.HASH_KEY)
                .append(Constant.Symbols.EQUALS)
                .append(hash);
        String initDataString = initDataBuilder.toString();

        // then
        assertThrows(
                ValidationException.class,
                () -> instance.validate(initDataString),
                "InitData is compromised: hash does not match target"
        );
    }

    @Test
    void shouldThrownAnExceptionOnOutdatedLogin() {
        // given()
        StringBuilder initDataBuilder = new StringBuilder(INITIAL_INIT_DATA_STRING);
        Instant authDate = Instant.now();
        // вставляем дату входа, которая уже считается устаревшей
        initDataBuilder.append(Constant.Symbols.AND)
                .append(Constant.InitData.AUTH_DATE_KEY)
                .append(Constant.Symbols.EQUALS)
                .append(authDate.minusSeconds(AUTH_VALIDITY_AMOUNT_SECONDS + 1).getEpochSecond());
        InitData checkStringInitData = parser.parse(initDataBuilder.toString());
        String checkString = instance.createCheckString(checkStringInitData);
        String hash = hashService.hmacHex(checkString);
        initDataBuilder.append(Constant.Symbols.AND)
                .append(Constant.InitData.HASH_KEY)
                .append(Constant.Symbols.EQUALS)
                .append(hash);
        String initDataString = initDataBuilder.toString();

        // then
        assertThrows(
                ValidationException.class,
                () -> instance.validate(initDataString),
                "AuthDate is corrupted or outdated"
        );
    }

    @Test
    void shouldThrownAnExceptionIfNoUserInfoProvided() {
        // given()
        // initData не содержит информацию о пользователе
        StringBuilder initDataBuilder = new StringBuilder("query_id=AAHdF6IQAAAAAN0XohDhrOrc");
        Instant authDate = Instant.now();
        initDataBuilder.append(Constant.Symbols.AND)
                .append(Constant.InitData.AUTH_DATE_KEY)
                .append(Constant.Symbols.EQUALS)
                .append(authDate.getEpochSecond());
        InitData checkStringInitData = parser.parse(initDataBuilder.toString());
        String checkString = instance.createCheckString(checkStringInitData);
        String hash = hashService.hmacHex(checkString);
        initDataBuilder.append(Constant.Symbols.AND)
                .append(Constant.InitData.HASH_KEY)
                .append(Constant.Symbols.EQUALS)
                .append(hash);
        String initDataString = initDataBuilder.toString();

        // then
        assertThrows(
                ValidationException.class,
                () -> instance.validate(initDataString),
                "User info is missed by Telegram WebApp"
        );
    }
}

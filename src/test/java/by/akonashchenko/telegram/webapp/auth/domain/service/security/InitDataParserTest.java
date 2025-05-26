package by.akonashchenko.telegram.webapp.auth.domain.service.security;

import by.akonashchenko.telegram.webapp.auth.base.UnitTest;
import by.akonashchenko.telegram.webapp.auth.domain.model.InitData;
import by.akonashchenko.telegram.webapp.auth.util.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;


public class InitDataParserTest extends UnitTest {

    private InitDataParser instance;

    @BeforeEach
    void setUp() {
        instance = new InitDataParser(mapper);
    }

    @Test
    void shouldParseInitDataString() {
        // given
        StringBuilder initDataBuilder = new StringBuilder(INITIAL_INIT_DATA_STRING);
        Instant authDate = Instant.now();
        initDataBuilder.append(Constant.Symbols.AND)
                .append(Constant.InitData.AUTH_DATE_KEY)
                .append(Constant.Symbols.EQUALS)
                .append(authDate.getEpochSecond());
        initDataBuilder.append(Constant.Symbols.AND)
                .append(Constant.InitData.HASH_KEY)
                .append(Constant.Symbols.EQUALS)
                .append("hash");

        // then
        InitData initData = assertDoesNotThrow(() -> instance.parse(initDataBuilder.toString()));
        assertEquals(authDate.getEpochSecond(), initData.getAuthDate());
        assertEquals("hash", initData.getHash());
        assertEquals("AAHdF6IQAAAAAN0XohDhrOrc", initData.getRaw().get("query_id"));
        InitData.User user = initData.getUser();
        assertNotNull(user);
        assertEquals(279058397, user.getId());
        assertEquals("Vladislav", user.getFirstName());
        assertEquals("Kibenko", user.getLastName());
        assertEquals("vdkfrost", user.getUsername());
        assertTrue(user.isPremium());
    }

    @Test
    void shouldThrowAndExceptionIfInitDataStringIsEmpty() {
        // given
        String initDataString = "";

        // then
        assertThrows(
                IllegalArgumentException.class,
                () ->  instance.parse(initDataString),
                "InitData is absent"
        );
    }
}

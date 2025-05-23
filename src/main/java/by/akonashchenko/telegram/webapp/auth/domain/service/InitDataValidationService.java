package by.akonashchenko.telegram.webapp.auth.domain.service;

import by.akonashchenko.telegram.webapp.auth.config.SecurityConfiguration;
import by.akonashchenko.telegram.webapp.auth.config.model.BotConfig;
import by.akonashchenko.telegram.webapp.auth.domain.model.InitData;
import by.akonashchenko.telegram.webapp.auth.domain.model.exception.ValidationException;
import by.akonashchenko.telegram.webapp.auth.util.InputDataHashUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static by.akonashchenko.telegram.webapp.auth.util.Constant.SECRET_KEY_PREFIX;

@Service
public class InitDataValidationService {

    private final SecurityConfiguration securityConfiguration;
    private final InitDataParser parser;
    private final HashingService hashingService;
    private final byte[] secret;

    public InitDataValidationService(
            SecurityConfiguration securityConfiguration,
            BotConfig botConfig,
            InitDataParser parser,
            HashingService hashingService
    ) {
        this.securityConfiguration = securityConfiguration;
        this.parser = parser;
        this.hashingService = hashingService;
        this.secret = this.hashingService.calculateSha256Key(
                SECRET_KEY_PREFIX + botConfig.getToken()
        );
    }

    public InitData validate(String input) {
        InitData initData = parser.parse(input);
        validateHash(initData);
        validateAuthDate(initData.getAuthDate());
        return initData;
    }

    private void validateHash(InitData initData) {
        String checkString = InputDataHashUtils.createCheckString(initData);
        String hash = hashingService.getSha256Hash(secret, checkString);
        if (!StringUtils.equals(hash, initData.getHash())) {
            throw new ValidationException(
                    "InitData is compromised"
            );
        }
    }

    private void validateAuthDate(long authDate) {
        var diff = Instant.now().getEpochSecond() - authDate;
        if (diff < 0 || diff >= securityConfiguration.getAuthValidityAmount()) {
            throw new ValidationException("authDate is corrupted or outdated");
        }
    }
}

package by.akonashchenko.telegram.webapp.auth.domain.service.security;

import by.akonashchenko.telegram.webapp.auth.config.model.AuthConfig;
import by.akonashchenko.telegram.webapp.auth.config.model.BotConfig;
import by.akonashchenko.telegram.webapp.auth.domain.model.InitData;
import by.akonashchenko.telegram.webapp.auth.domain.model.exception.ValidationException;
import by.akonashchenko.telegram.webapp.auth.domain.service.hashing.HashingService;
import by.akonashchenko.telegram.webapp.auth.domain.service.hashing.HmacHashingService;
import by.akonashchenko.telegram.webapp.auth.util.InputDataHashUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static by.akonashchenko.telegram.webapp.auth.util.Constant.InitData.SECRET_KEY_PREFIX;
import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_256;

@Service
public class InitDataValidationService {

    private final AuthConfig securityConfiguration;
    private final InitDataParser parser;
    private final HashingService hashingService;

    public InitDataValidationService(
            AuthConfig securityConfiguration,
            BotConfig botConfig,
            InitDataParser parser
    ) {
        this.securityConfiguration = securityConfiguration;
        this.parser = parser;
        this.hashingService = new HmacHashingService(
                HMAC_SHA_256.name(),
                SECRET_KEY_PREFIX + botConfig.getToken()
        );
    }

    public InitData validate(String input) {
        InitData initData = parser.parse(input);
        validateHash(initData);
        validateAuthDate(initData.getAuthDate());
        validateUser(initData.getUser());
        return initData;
    }

    private void validateHash(InitData initData) {
        String checkString = InputDataHashUtils.createCheckString(initData);
        if (!hashingService.matches(checkString, initData.getHash())) {
            throw new ValidationException(
                    "InitData is compromised: hash does not match target"
            );
        }
    }

    private void validateAuthDate(long authDate) {
        var diff = Instant.now().getEpochSecond() - authDate;
        if (diff < 0 || diff >= securityConfiguration.getValidityAmount()) {
            throw new ValidationException("authDate is corrupted or outdated");
        }
    }

    private void validateUser(InitData.User user) {
        if (user == null) {
            throw new ValidationException("User info is missed by provider");
        }
    }
}

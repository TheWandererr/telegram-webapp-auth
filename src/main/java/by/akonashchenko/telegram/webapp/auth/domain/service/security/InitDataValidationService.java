package by.akonashchenko.telegram.webapp.auth.domain.service.security;

import by.akonashchenko.telegram.webapp.auth.config.model.AuthConfig;
import by.akonashchenko.telegram.webapp.auth.domain.model.InitData;
import by.akonashchenko.telegram.webapp.auth.domain.model.exception.ValidationException;
import by.akonashchenko.telegram.webapp.auth.domain.service.hashing.InitDataHashService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

import static by.akonashchenko.telegram.webapp.auth.util.Constant.InitData.HASH_KEY;

@Service
public class InitDataValidationService {

    private final AuthConfig authConfig;
    private final InitDataParser parser;
    private final InitDataHashService initDataHashService;

    public InitDataValidationService(
            AuthConfig authConfig,
            InitDataParser parser,
            InitDataHashService initDataHashService
    ) {
        this.authConfig = authConfig;
        this.parser = parser;
        this.initDataHashService = initDataHashService;
    }

    public InitData validate(String input) {
        InitData initData = parser.parse(input);
        validateHash(initData);
        // validateAuthDate(initData.getAuthDate());
        validateUser(initData.getUser());
        return initData;
    }

    private void validateHash(InitData initData) {
        String checkString = createCheckString(initData);
        if (!initDataHashService.verifyHash(checkString, initData.getHash())) {
            throw new ValidationException(
                    "InitData is compromised: hash does not match target"
            );
        }
    }

    String createCheckString(InitData initData) {
        Map<String, String> data = new TreeMap<>(initData.getRaw());
        data.remove(HASH_KEY);
        if (data.isEmpty()) {
            throw new IllegalArgumentException("InitData is required to create check-string");
        }
        StringBuilder output = new StringBuilder();
        data.forEach((key, value) -> {
            output.append(key);
            output.append("=");
            output.append(value);
            output.append("\n");
        });
        output.setLength(output.length() - 1);
        return output.toString();
    }

    private void validateAuthDate(long authDate) {
        var diff = Instant.now().getEpochSecond() - authDate;
        if (diff < 0 || diff >= authConfig.getValidityAmountSeconds()) {
            throw new ValidationException("AuthDate is corrupted or outdated");
        }
    }

    private void validateUser(InitData.User user) {
        if (user == null) {
            throw new ValidationException("User info is missed by Telegram WebApp");
        }
    }
}

package by.akonashchenko.telegram.webapp.auth.domain.service.security;

import by.akonashchenko.telegram.webapp.auth.config.model.AuthConfig;
import by.akonashchenko.telegram.webapp.auth.domain.model.InitData;
import by.akonashchenko.telegram.webapp.auth.domain.service.hashing.InitDataHashService;

public class InitDataValidationServiceTestWrapper extends InitDataValidationService {

    public InitDataValidationServiceTestWrapper(
            AuthConfig authConfig,
            InitDataParser parser,
            InitDataHashService initDataHashService
    ) {
        super(authConfig, parser, initDataHashService);
    }

    @Override
    public String createCheckString(InitData initData) {
        return super.createCheckString(initData);
    }
}

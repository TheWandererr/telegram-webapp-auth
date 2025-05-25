package by.akonashchenko.telegram.webapp.auth.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {

    @UtilityClass
    public static class InitData {
        public static final String USER_KEY = "user";
        public static final String HASH_KEY = "hash";
        public static final String AUTH_DATE_KEY = "auth_date";

        public static final String SECRET_KEY_PREFIX = "WebAppData";
    }

    @UtilityClass
    public static class Jpa {
        public static final String REGULAR_ROLE_NAME = "REGULAR";
        public static final String PREMIUM_ROLE_NAME = "PREMIUM";
    }

}

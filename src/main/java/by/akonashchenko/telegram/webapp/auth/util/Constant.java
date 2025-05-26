package by.akonashchenko.telegram.webapp.auth.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {

    @UtilityClass
    public static class InitData {
        public static final String USER_KEY = "user";
        public static final String HASH_KEY = "hash";
        public static final String AUTH_DATE_KEY = "auth_date";

        public static final String CRYPTOGRAPHIC_KEY = "WebAppData";

        public static final String AUTHORIZATION_PREFIX = "tma ";
    }

    @UtilityClass
    public static class Roles {
        public static final String REGULAR = "REGULAR";
        public static final String PREMIUM = "PREMIUM";
    }

    @UtilityClass
    public static class Authorities {
        public static final String READ = "READ";
        public static final String WRITE = "WRITE";
    }

    @UtilityClass
    public static class Cookies {

        public static final String AUTH = "auth";
    }

    @UtilityClass
    public static class Symbols {

        public static final String COLON = ":";
        public static final String AND = "&";
        public static final String EQUALS = "=";
    }

    @UtilityClass
    public static class UriComponents {
        public static final String LOGIN = "/login";
    }
}

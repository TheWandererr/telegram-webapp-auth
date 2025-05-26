package by.akonashchenko.telegram.webapp.auth.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static by.akonashchenko.telegram.webapp.auth.util.Constant.InitData.AUTHORIZATION_PREFIX;
import static by.akonashchenko.telegram.webapp.auth.util.Constant.Symbols.AND;
import static by.akonashchenko.telegram.webapp.auth.util.Constant.Symbols.EQUALS;

@UtilityClass
public class RequestUtils {

    private static final Integer KEY_VALUE_SIZE = 2;

    public static Map<String, String> parseQuery(String query) {
        if (StringUtils.isBlank(query)) {
            return Map.of();
        }
        return Arrays.stream(query.split(AND))
                .map(param -> param.split(EQUALS, KEY_VALUE_SIZE))
                .filter(pair -> pair.length == KEY_VALUE_SIZE)
                .collect(Collectors.toMap(
                        pair -> URLDecoder.decode(pair[0], StandardCharsets.UTF_8),
                        pair -> URLDecoder.decode(pair[1], StandardCharsets.UTF_8)
                ));
    }

    public static String getTelegramWebAppInitData(String authorization) {
        return StringUtils.substringAfter(authorization, AUTHORIZATION_PREFIX);
    }
}

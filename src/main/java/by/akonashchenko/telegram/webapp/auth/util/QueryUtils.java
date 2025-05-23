package by.akonashchenko.telegram.webapp.auth.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@UtilityClass
public class QueryUtils {

    public static Map<String, String> parseQuery(String query) {
        return UriComponentsBuilder.newInstance()
                .query(query)
                .build()
                .getQueryParams()
                .asSingleValueMap();
    }
}

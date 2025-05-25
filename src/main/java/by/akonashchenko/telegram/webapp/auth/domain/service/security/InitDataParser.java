package by.akonashchenko.telegram.webapp.auth.domain.service.security;

import by.akonashchenko.telegram.webapp.auth.domain.model.InitData;
import by.akonashchenko.telegram.webapp.auth.util.QueryUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

import static by.akonashchenko.telegram.webapp.auth.util.Constant.InitData.*;
import static org.apache.commons.lang3.math.NumberUtils.toInt;

@Component
@RequiredArgsConstructor
public class InitDataParser {

    private final ObjectMapper mapper;

    public InitData parse(String rawInitData) {
        Map<String, String> decodedInitData = QueryUtils.parseQuery(rawInitData);
        return InitData.builder()
                .user(exctractUser(decodedInitData))
                .hash(decodedInitData.get(HASH_KEY))
                .authDate(toInt(decodedInitData.get(AUTH_DATE_KEY)))
                .raw(decodedInitData)
                .build();
    }

    private InitData.User exctractUser(Map<String, String> decodedInitData) {
        var json = decodedInitData.getOrDefault(USER_KEY, StringUtils.EMPTY);
        try {
            return mapper.readValue(json, InitData.User.class);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException(
                    "Error while user info deserialization with message: " +
                            ex.getMessage()
            );
        }
    }
}

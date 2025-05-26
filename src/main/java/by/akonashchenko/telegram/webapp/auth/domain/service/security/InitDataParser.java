package by.akonashchenko.telegram.webapp.auth.domain.service.security;

import by.akonashchenko.telegram.webapp.auth.domain.model.InitData;
import by.akonashchenko.telegram.webapp.auth.util.RequestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

import static by.akonashchenko.telegram.webapp.auth.util.Constant.InitData.*;
import static org.apache.commons.lang3.math.NumberUtils.toInt;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitDataParser {

    private final ObjectMapper mapper;

    public InitData parse(String rawInitData) {
        Map<String, String> decodedInitData = RequestUtils.parseQuery(rawInitData);
        if (decodedInitData.isEmpty()) {
            throw new IllegalArgumentException("InitData is absent");
        }
        return InitData.builder()
                .user(exctractUser(decodedInitData))
                .hash(decodedInitData.get(HASH_KEY))
                .authDate(toInt(decodedInitData.get(AUTH_DATE_KEY)))
                .raw(decodedInitData)
                .build();
    }

    private InitData.User exctractUser(Map<String, String> decodedInitData) {
        var json = decodedInitData.getOrDefault(USER_KEY, StringUtils.EMPTY);
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, InitData.User.class);
        } catch (JsonProcessingException ex) {
            log.error("Error while user info deserialization with message: {}", ex.getMessage());
            throw new IllegalArgumentException("Error while get user info");
        }
    }
}

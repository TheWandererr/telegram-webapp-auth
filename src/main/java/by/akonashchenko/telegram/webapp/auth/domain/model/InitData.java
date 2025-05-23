package by.akonashchenko.telegram.webapp.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class InitData {

    private User user;
    private String hash;
    @JsonProperty("auth_date")
    private long authDate;
    private Map<String, String> raw;

    @Data
    public static class User {
        private Integer id;
        private String username;
        @JsonProperty("first_name")
        private String firstname;
        @JsonProperty("last_name")
        private String lastname;
        @JsonProperty("is_premium")
        private boolean premium;
    }
}

package by.akonashchenko.telegram.webapp.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
        private Long id;
        private String username;
        @JsonProperty("first_name")
        private String firstName;
        @JsonProperty("last_name")
        private String lastName;
        @JsonProperty("is_premium")
        private boolean premium;

        public boolean hasId() {
            return id != null;
        }

        public boolean hasUsername() {
            return StringUtils.isNotBlank(username);
        }
    }

    public boolean hasUser() {
        return user != null;
    }
}

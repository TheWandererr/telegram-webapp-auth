package by.akonashchenko.telegram.webapp.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InitData {

    private User user;
    private String hash;
    @JsonProperty("auth_date")
    private long authDate;
    private Map<String, String> raw;

    @Data
    public static class User {
        private Long id;
        @JsonProperty("username")
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
}

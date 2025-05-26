package by.akonashchenko.telegram.webapp.auth.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "telegram.webapp.auth")
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthConfig {

    private Integer validityAmountSeconds;
}

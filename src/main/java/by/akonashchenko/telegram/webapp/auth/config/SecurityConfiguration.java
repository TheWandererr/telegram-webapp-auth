package by.akonashchenko.telegram.webapp.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.security.properties")
@Getter
@Setter
public class SecurityConfiguration {

    private Integer authValidityAmount;
}

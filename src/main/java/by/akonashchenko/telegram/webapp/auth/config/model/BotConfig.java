package by.akonashchenko.telegram.webapp.auth.config.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@ConfigurationProperties(prefix = "telegram.webapp.bot")
@Validated
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BotConfig {

    @NotBlank
    private String token;
}

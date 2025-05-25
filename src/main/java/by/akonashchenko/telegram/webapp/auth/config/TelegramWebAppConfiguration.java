package by.akonashchenko.telegram.webapp.auth.config;

import by.akonashchenko.telegram.webapp.auth.config.model.AuthConfig;
import by.akonashchenko.telegram.webapp.auth.config.model.BotConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramWebAppConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "telegram.webapp.bot")
    public BotConfig botConfig() {
        return new BotConfig();
    }
    
    @Bean
    @ConfigurationProperties(prefix = "telegram.webapp.auth")
    public AuthConfig authConfig() {
        return new AuthConfig();
    }
}

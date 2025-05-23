package by.akonashchenko.telegram.webapp.auth.config;

import by.akonashchenko.telegram.webapp.auth.config.model.BotConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "telegram.wepapp.bot")
    public BotConfig botConfig() {
        return new BotConfig();
    }

}

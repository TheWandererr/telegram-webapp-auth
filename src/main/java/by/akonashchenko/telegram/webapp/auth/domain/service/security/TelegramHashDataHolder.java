package by.akonashchenko.telegram.webapp.auth.domain.service.security;

import by.akonashchenko.telegram.webapp.auth.config.model.BotConfig;
import by.akonashchenko.telegram.webapp.auth.util.Constant;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TelegramHashDataHolder {

    private final String token;

    public TelegramHashDataHolder(BotConfig botConfig) {
        this.token = botConfig.getToken();
    }

    public String getCryptographicKey() {
        return Constant.InitData.CRYPTOGRAPHIC_KEY;
    }
}

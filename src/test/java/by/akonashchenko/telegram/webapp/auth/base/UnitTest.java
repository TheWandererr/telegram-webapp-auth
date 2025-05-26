package by.akonashchenko.telegram.webapp.auth.base;

import by.akonashchenko.telegram.webapp.auth.config.mapping.JacksonConfig;
import by.akonashchenko.telegram.webapp.auth.config.model.BotConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class UnitTest extends BaseTest {

    protected final ObjectMapper mapper = new JacksonConfig()
            .mapper();
    protected final BotConfig botConfig;

    protected UnitTest() {
        this.botConfig = BotConfig.builder()
                .token(System.getenv("TELEGRAM_WEBAPP_BOT_TOKEN"))
                .build();
    }
}

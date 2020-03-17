package com.podo.helloprice.telegram.client.config;

import com.podo.helloprice.telegram.client.core.TelegramBot;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.List;

@Profile("deploy")
@Configuration
public class TelegramBotRegisterConfig {

    public TelegramBotRegisterConfig(List<TelegramBot> telegramBots) throws TelegramApiRequestException {
        final TelegramBotsApi api = new TelegramBotsApi();

        for (TelegramBot telegramBot : telegramBots) {
            api.registerBot(telegramBot);
        }
    }
}

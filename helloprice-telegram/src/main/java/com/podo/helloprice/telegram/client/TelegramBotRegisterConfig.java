package com.podo.helloprice.telegram.client;

import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.List;

@Configuration
public class TelegramBotRegisterConfig {

    public TelegramBotRegisterConfig(List<TelegramBot> telegramBots) throws TelegramApiRequestException {
        final TelegramBotsApi api = new TelegramBotsApi();

        for (TelegramBot telegramBot : telegramBots) {
            api.registerBot(telegramBot);
        }
    }


}

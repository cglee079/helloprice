package com.podo.itemwatcher.telegram.global.infra.telegram;

import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.List;

@Configuration
public class TelegramConfig {

    public TelegramConfig(List<TelegramBot> telegramBots) throws TelegramApiRequestException {
        TelegramBotsApi api = new TelegramBotsApi();
        for (int i = 0; i < telegramBots.size(); i++) {
            api.registerBot(telegramBots.get(i));
        }
    }

}

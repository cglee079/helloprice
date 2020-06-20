package com.podo.helloprice.telegram.app.config;

import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.core.TelegramMessageReceiver;
import com.podo.helloprice.telegram.app.core.TelegramMessageReceiverHandler;
import com.podo.helloprice.telegram.app.core.TelegramMessageSender;
import com.podo.helloprice.telegram.domain.tuser.repository.TUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Configuration
public class TelegramBotConfig {

    @Value("${infra.telegram.bot.token}")
    private String botToken;

    @Value("${infra.telegram.bot.name}")
    private String botName;

    @Bean
    public TelegramMessageReceiver telegramMessageReceiver(TelegramMessageReceiverHandler telegramMessageReceiverHandler){
        return new TelegramMessageReceiver(botToken, botName, telegramMessageReceiverHandler);
    }

    @Bean
    public TelegramMessageSender telegramMessageSender(TUserRepository tUserRepository, SendMessageCallbackFactory sendMessageCallbackFactory){
        return new TelegramMessageSender(botToken, tUserRepository, sendMessageCallbackFactory);
    }

    @Bean
    public TelegramBotsApi TelegramBotRegisterConfig(TelegramMessageReceiverHandler telegramMessageReceiverHandler) throws TelegramApiRequestException {
        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        telegramBotsApi.registerBot(telegramMessageReceiver(telegramMessageReceiverHandler));

        return telegramBotsApi;
    }

}

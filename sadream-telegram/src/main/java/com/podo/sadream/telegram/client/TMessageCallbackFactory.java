package com.podo.sadream.telegram.client;

import com.podo.sadream.core.domain.user.Menu;
import com.podo.sadream.telegram.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

@Slf4j
@Component
@RequiredArgsConstructor
public class TMessageCallbackFactory {

    private final UserService userService;

    public SentCallback<Message> createDefaultCallback(String telegramId, Menu menu) {
        return new SentCallback<Message>() {
            @Override
            public void onResult(BotApiMethod<Message> method, Message response) {
                log.info("RESULT : " + Thread.currentThread());
                userService.updateMenuStatus(telegramId, menu);
                userService.resetUserErrorCount(telegramId);
            }

            @Override
            public void onError(BotApiMethod<Message> method, TelegramApiRequestException e) {
                userService.updateMenuStatus(telegramId, Menu.HOME);
                userService.increaseUserErrorCount(telegramId);
                log.error("{} >> Send Error, 메시지를 전송 할 수 없습니다 '{}'", telegramId, e.getMessage());
            }

            @Override
            public void onException(BotApiMethod<Message> method, Exception e) {
                userService.updateMenuStatus(telegramId, Menu.HOME);
                userService.increaseUserErrorCount(telegramId);
                log.error("{} >> Send Exception, 메시지를 전송 할 수 없습니다 '{}'", telegramId, e.getMessage());
            }
        };
    }
}

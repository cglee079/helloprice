package com.podo.helloprice.telegram.client;

import com.podo.helloprice.core.domain.model.Menu;
import com.podo.helloprice.telegram.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TMessageCallbackFactory {

    private final UserService userService;

    public SentCallback<Message> createDefaultNoAction(String telegramId) {
        return this.createDefault(telegramId, null);
    }

    public SentCallback<Message> createDefault(String telegramId, Menu menu) {
        return new SentCallback<Message>() {
            @Override
            public void onResult(BotApiMethod<Message> method, Message response) {
                if (Objects.nonNull(menu)) {
                    userService.updateMenuStatusByTelegramId(telegramId, menu);
                }
                userService.clearUserErrorCountByTelegramId(telegramId);
            }

            @Override
            public void onError(BotApiMethod<Message> method, TelegramApiRequestException e) {
                userService.updateMenuStatusByTelegramId(telegramId, Menu.HOME);
                userService.increaseUserErrorCountByTelegramId(telegramId);
                log.error("{} >> Send Error, 메시지를 전송 할 수 없습니다 '{}'", telegramId, e.getMessage());
            }

            @Override
            public void onException(BotApiMethod<Message> method, Exception e) {
                userService.updateMenuStatusByTelegramId(telegramId, Menu.HOME);
                userService.increaseUserErrorCountByTelegramId(telegramId);
                log.error("{} >> Send Exception, 메시지를 전송 할 수 없습니다 '{}'", telegramId, e.getMessage());
            }
        };
    }
}

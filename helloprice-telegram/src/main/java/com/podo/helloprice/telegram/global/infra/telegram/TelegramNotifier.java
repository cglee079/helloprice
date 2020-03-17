package com.podo.helloprice.telegram.global.infra.telegram;

import com.podo.helloprice.telegram.client.api.TelegramApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramNotifier {

    @Value("${infra.telegram.admin.id}")
    private String adminTelegramId;

    private final TelegramApi telegramApi;

    public void notifyUser(String telegramId, String image, String response) {
        telegramApi.send(telegramId, image, response);
    }

    public void notifyAdmin(String image, String message) {
        this.notifyUser(adminTelegramId, image, message);
    }
}

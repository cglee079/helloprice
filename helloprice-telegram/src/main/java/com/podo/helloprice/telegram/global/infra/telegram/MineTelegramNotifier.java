package com.podo.helloprice.telegram.global.infra.telegram;

import com.podo.helloprice.telegram.client.TelegramApi;
import com.podo.helloprice.telegram.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class MineTelegramNotifier {

    @Value("${telegram.podo_helloprice.admin.id}")
    private Integer adminTelegramId;

    private final TelegramApi telegramApi;

    public void notifyUser(Integer telegramId, String image, String response) {
        telegramApi.send(telegramId, image, response);
    }

    public void notifyAdmin(String message) {
        this.notifyUser(adminTelegramId, null, message);
    }
}

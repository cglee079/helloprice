package com.podo.helloprice.telegram.client;

import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.core.domain.user.UserStatus;
import com.podo.helloprice.telegram.domain.user.UserDto;
import com.podo.helloprice.telegram.domain.user.UserService;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramNotifier {

    @Value("${telegram.podo_helloprice.admin.id}")
    private Integer adminTelegramId;

    private final UserService userService;
    private final UserItemNotifyService userItemNotifyService;
    private final TelegramMessageSender telegramMessageSender;
    private final TMessageCallbackFactory callbackFactory;

    public void notifyUsers(Long itemId, String itemImage, String message) {
        List<UserDto.detail> users = userItemNotifyService.findNotifyUsersByItemId(itemId, UserStatus.ALIVE);

        for (UserDto.detail user : users) {
            notifyUser(user.getTelegramId(), itemImage, message);
        }
    }

    public void notifyUser(Integer telegramId, String itemImage, String response) {
        log.info("{}님에게 변경상태 알림을 전송합니다", telegramId);
        userService.updateNotifyAt(telegramId, LocalDateTime.now());

        TMessageVo tMessageVo = new TMessageVo(telegramId, null);

        telegramMessageSender.send(tMessageVo.newValue(response, itemImage, null, callbackFactory.createDefaultCallback(telegramId + "", Menu.HOME)));
    }

    public void notifyAdmin(String message) {
        this.notifyUser(adminTelegramId, null, message);
    }
}

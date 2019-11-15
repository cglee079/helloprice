package com.podo.helloprice.telegram.job.notifier;

import com.podo.helloprice.telegram.global.infra.gmail.GmailNotifier;
import com.podo.helloprice.telegram.global.infra.telegram.MineTelegramNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class GlobalNotifier {

    private final GmailNotifier gmailNotifier;
    private final MineTelegramNotifier mineTelegramNotifier;

    public void notifyUsers(List<NotifyUserVo> notifyUsers, String title, String image, String contents) {
        for (NotifyUserVo notifyUser : notifyUsers) {
            notifyUser(notifyUser, title, image, contents);
        }
    }

    public void notifyUser(NotifyUserVo notifyUser, String title, String image, String contents) {
        final Integer telegramId = notifyUser.getTelegramId();
        final String username = notifyUser.getUsername();

        log.info("{}({})님에게 상품 변경 알림을 전송합니다", telegramId, username);

        if (Objects.nonNull(notifyUser.getEmail())) {
            gmailNotifier.notifyUser(username, notifyUser.getEmail(), title, image, contents);
        }

        mineTelegramNotifier.notifyUser(telegramId, image, contents);
    }

    public void notifyAdmin(String title, String contents) {
        gmailNotifier.notifyAdmin(title, contents);
        mineTelegramNotifier.notifyAdmin(contents);
    }
}

package com.podo.helloprice.telegram.domain.user;

import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.core.domain.user.User;
import com.podo.helloprice.core.domain.user.UserRepository;
import com.podo.helloprice.core.domain.useritem.UserItemNotify;
import com.podo.helloprice.core.domain.useritem.UserItemNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    @Value("${user.max_error_count}")
    private Integer userMaxErrorCount;

    private final UserRepository userRepository;
    private final UserItemNotifyRepository userItemNotifyRepository;

    public UserDto.detail findByTelegramId(String telegramId) {
        final User user = userRepository.findByTelegramId(telegramId);

        if (Objects.isNull(user)) {
            return null;
        }

        return new UserDto.detail(user);
    }

    public void insert(UserDto.insert userInsert) {
        userRepository.save(userInsert.toEntity());
    }

    public void updateMenuStatus(String telegramId, Menu menu) {
        final User user = userRepository.findByTelegramId(telegramId);
        user.updateMenuStatus(menu);
    }

    public void increaseUserErrorCount(String telegramId) {
        final User user = userRepository.findByTelegramId(telegramId);
        user.increaseErrorCount();

        if (user.getErrorCount() > userMaxErrorCount) {

            user.died();

            for (UserItemNotify userItemNotify : user.getUserItemNotifies()) {
                userItemNotify.getUser().removeUserItemNotify(userItemNotify);
                userItemNotify.getItem().removeUserItemNotify(userItemNotify);

                userItemNotifyRepository.delete(userItemNotify);
            }
        }
    }

    public void resetUserErrorCount(String telegramId) {
        final User user = userRepository.findByTelegramId(telegramId);
        user.resetErrorCount();
    }

    public void reviveUser(Long userId) {
        User user = userRepository.findById(userId).get();
        user.revive();
    }

    public void updateSendAt(Long userId, LocalDateTime lastSendAt) {
        User user = userRepository.findById(userId).get();
        user.updateSendAt(lastSendAt);
    }

    public void updateNotifyAt(Integer telegramId, LocalDateTime lastNotifyAt) {
        User user = userRepository.findByTelegramId(telegramId + "");
        user.updateNotifyAt(lastNotifyAt);
    }

    public void updateEmail(String telegramId, String email) {
        final User user = userRepository.findByTelegramId(telegramId);
        user.updateEmail(email);
    }

}

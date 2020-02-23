package com.podo.helloprice.telegram.domain.user;

import com.podo.helloprice.core.domain.user.model.UserStatus;
import com.podo.helloprice.core.model.Menu;
import com.podo.helloprice.core.domain.user.User;
import com.podo.helloprice.core.domain.user.repository.UserRepository;
import com.podo.helloprice.core.domain.useritem.UserItemNotify;
import com.podo.helloprice.core.domain.useritem.repository.UserItemNotifyRepository;
import com.podo.helloprice.telegram.domain.user.exception.InvalidTelegramIdException;
import com.podo.helloprice.telegram.domain.user.exception.InvalidUserIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    @Value("${user.max_error_count}")
    private Integer userMaxErrorCount;

    @Value("${user.max_item}")
    private Integer maxCountOfItemNotifies;

    private final UserRepository userRepository;
    private final UserItemNotifyRepository userItemNotifyRepository;

    public UserDto.detail findByTelegramId(String telegramId) {
        try {
            return new UserDto.detail(findUserByTelegramId(telegramId));
        } catch (InvalidTelegramIdException e) {
            return null;
        }
    }

    public Long insertNewUser(UserDto.insert userInsert) {
        final User newUser = userInsert.toEntity();
        final User savedUser = userRepository.save(newUser);

        return savedUser.getId();
    }

    public void increaseUserErrorCountByTelegramId(String telegramId) {
        final User existedUser = findUserByTelegramId(telegramId);

        existedUser.increaseErrorCount(userMaxErrorCount);

        if (existedUser.getUserStatus().equals(UserStatus.DEAD)) {
            removeUserItemNotifies(existedUser.getUserItemNotifies());
        }
    }

    private void removeUserItemNotifies(List<UserItemNotify> userItemNotifies) {
        for (UserItemNotify userItemNotify : userItemNotifies) {
            userItemNotify.getUser().removeUserItemNotify(userItemNotify);
            userItemNotify.getItem().removeUserItemNotify(userItemNotify);

            userItemNotifyRepository.delete(userItemNotify);
        }
    }

    public void updateMenuStatusByTelegramId(String telegramId, Menu menu) {
        findUserByTelegramId(telegramId).updateMenuStatus(menu);
    }

    public void clearUserErrorCountByTelegramId(String telegramId) {
        findUserByTelegramId(telegramId).clearErrorCount();
    }

    public void reviveUser(Long userId) {
        findUserByUserId(userId).revive();
    }

    public void updateSendAt(Long userId, LocalDateTime lastSendAt) {
        findUserByUserId(userId).updateLastSendAt(lastSendAt);
    }

    public void updateEmailByTelegramId(String telegramId, String email) {
        findUserByTelegramId(telegramId).updateEmail(email);
    }

    public boolean hasMaxNotifyByUserTelegramId(String telegramId) {
        return findUserByTelegramId(telegramId).hasItemNotifiesMoreThan(maxCountOfItemNotifies);
    }

    private User findUserByTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId).orElseThrow(() -> new InvalidTelegramIdException(telegramId));
    }

    private User findUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new InvalidUserIdException(userId));
    }


}

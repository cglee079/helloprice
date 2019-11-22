package com.podo.helloprice.telegram.domain.user;

import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.core.domain.user.User;
import com.podo.helloprice.core.domain.user.UserRepository;
import com.podo.helloprice.core.domain.useritem.UserItemNotify;
import com.podo.helloprice.core.domain.useritem.UserItemNotifyRepository;
import com.podo.helloprice.telegram.domain.user.exception.InvalidUserIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    @Value("${user.max_error_count}")
    private Integer userMaxErrorCount;

    @Value("${user.max_item}")
    private Integer maxCountOfItemNotifies;

    private final UserRepository userRepository;
    private final UserItemNotifyRepository userItemNotifyRepository;

    public UserDto.detail findByTelegramId(String telegramId) {
        final User existedUser = userRepository.findByTelegramId(telegramId);

        if (Objects.isNull(existedUser)) {
            return null;
        }

        return new UserDto.detail(existedUser);
    }

    public Long insertNewUser(UserDto.insert userInsert) {
        final User newUser = userInsert.toEntity();
        final User savedUser = userRepository.save(newUser);
        return savedUser.getId();
    }

    public void updateMenuStatusByTelegramId(String telegramId, Menu menu) {
        final User existedUser = userRepository.findByTelegramId(telegramId);
        existedUser.updateMenuStatus(menu);
    }

    public void increaseUserErrorCountByTelegramId(String telegramId) {
        final User existedUser = userRepository.findByTelegramId(telegramId);

        existedUser.increaseErrorCount();

        final boolean isErrorCountThanMaximum = existedUser.getErrorCount() > userMaxErrorCount;

        if (isErrorCountThanMaximum) {
            existedUser.died();
            removeUserItemNotifies(existedUser.getUserItemNotifies());
        }
    }

    public void clearUserErrorCountByTelegramId(String telegramId) {
        final User user = userRepository.findByTelegramId(telegramId);
        user.clearErrorCount();
    }

    private void removeUserItemNotifies(List<UserItemNotify> userItemNotifies) {
        for (UserItemNotify userItemNotify : userItemNotifies) {
            userItemNotify.getUser().removeUserItemNotify(userItemNotify);
            userItemNotify.getItem().removeUserItemNotify(userItemNotify);

            userItemNotifyRepository.delete(userItemNotify);
        }
    }

    public void reviveUser(Long userId) {
        final Optional<User> existedUserOptional = userRepository.findById(userId);

        if (!existedUserOptional.isPresent()) {
            throw new InvalidUserIdException(userId);
        }

        final User existedUser = existedUserOptional.get();
        existedUser.revive();
    }

    public void updateSendAt(Long userId, LocalDateTime lastSendAt) {
        final Optional<User> existedUserOptional = userRepository.findById(userId);

        if (!existedUserOptional.isPresent()) {
            throw new InvalidUserIdException(userId);
        }

        final User existedUser = existedUserOptional.get();
        existedUser.updateSendAt(lastSendAt);
    }

    public void updateEmailByTelegramId(String telegramId, String email) {
        final User existedUser = userRepository.findByTelegramId(telegramId);
        existedUser.updateEmail(email);
    }

    public boolean hasMaxNotifyByUserTelegramId(String telegramId) {
        final User existedUser = userRepository.findByTelegramId(telegramId);
        if (existedUser.hasItemNotifiesMoreThan(maxCountOfItemNotifies)) {
            return true;
        }
        return false;
    }


}

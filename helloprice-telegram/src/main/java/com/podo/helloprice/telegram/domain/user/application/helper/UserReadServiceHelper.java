package com.podo.helloprice.telegram.domain.user.application.helper;

import com.podo.helloprice.telegram.domain.user.application.UserReadService;
import com.podo.helloprice.telegram.domain.user.exception.InvalidTelegramIdException;
import com.podo.helloprice.telegram.domain.user.exception.InvalidUserIdException;
import com.podo.helloprice.telegram.domain.user.model.User;
import com.podo.helloprice.telegram.domain.user.repository.UserRepository;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserReadServiceHelper {

    public static User findUserByTelegramId(UserRepository userRepository, String telegramId) {
        return userRepository.findByTelegramId(telegramId).orElseThrow(() -> new InvalidTelegramIdException(telegramId));
    }

    public static User findUserById(UserRepository userRepository, Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new InvalidUserIdException(userId));
    }

}

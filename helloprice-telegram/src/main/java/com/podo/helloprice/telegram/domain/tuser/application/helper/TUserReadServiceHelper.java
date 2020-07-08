package com.podo.helloprice.telegram.domain.tuser.application.helper;

import com.podo.helloprice.telegram.domain.tuser.exception.InvalidTelegramIdException;
import com.podo.helloprice.telegram.domain.tuser.exception.InvalidTUserIdException;
import com.podo.helloprice.telegram.domain.tuser.model.TUser;
import com.podo.helloprice.telegram.domain.tuser.repository.TUserRepository;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TUserReadServiceHelper {

    public static TUser findUserByTelegramId(TUserRepository userRepository, String telegramId) {
        return userRepository.findByTelegramId(telegramId).orElseThrow(() -> new InvalidTelegramIdException(telegramId));
    }

    public static TUser findUserById(TUserRepository userRepository, Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new InvalidTUserIdException(userId));
    }

}

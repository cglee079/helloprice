package com.podo.sadream.telegram.domain.user;

import com.podo.sadream.core.domain.user.Menu;
import com.podo.sadream.core.domain.user.User;
import com.podo.sadream.core.domain.user.UserRepository;
import com.podo.sadream.core.domain.useritem.UserItemNotify;
import com.podo.sadream.core.domain.useritem.UserItemNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

            userItemNotifyRepository.deleteAll(user.getUserItemNotifies());
//            for (UserItemNotify userItemNotify : user.getUserItemNotifies()) {
//                userItemNotify.getUser().deleteUserItemNotify(userItemNotify);
//                userItemNotify.getItem().deleteUserItemNotify(userItemNotify);
//
//                userItemNotifyRepository.delete(userItemNotify);
//            }
        }
    }

    public void resetUserErrorCount(String telegramId) {
        final User user = userRepository.findByTelegramId(telegramId);
        user.resetErrorCount();
    }

    public void reviveUser(Long id) {
        User user = userRepository.findById(id).get();
        user.revive();
    }
}

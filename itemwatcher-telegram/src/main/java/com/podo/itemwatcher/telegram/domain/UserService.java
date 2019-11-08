package com.podo.itemwatcher.telegram.domain;

import com.podo.itemwatcher.core.domain.user.MenuStatus;
import com.podo.itemwatcher.core.domain.user.User;
import com.podo.itemwatcher.core.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserDto.detail findByTelegramId(Integer telegramId) {
        final User user = userRepository.findByTelegramId(telegramId + "");

        if (Objects.isNull(user)) {
            return null;
        }

        return new UserDto.detail(user);
    }

    public void insert(UserDto.insert userInsert) {
        userRepository.save(userInsert.toEntity());
    }

    public void updateMenuStatus(Integer telegramId, MenuStatus menuStatus) {
        final User user = userRepository.findByTelegramId(telegramId + "");
        user.updateMenuStatus(menuStatus);
    }
}

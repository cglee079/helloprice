package com.podo.itemwatcher.telegram.domain.user;

import com.podo.itemwatcher.core.domain.user.Menu;
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


}

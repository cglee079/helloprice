package com.podo.helloprice.api.domain.user.application;

import com.podo.helloprice.api.domain.user.dto.UserInsert;
import com.podo.helloprice.api.domain.user.model.User;
import com.podo.helloprice.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserPersistService {

    private final UserRepository userRepository;

    public Long persist(UserInsert userInsert) {

        final String userKey = userInsert.getUserKey();

        Optional<User> existUser = userRepository.findByUserKey(userKey);

        if (existUser.isPresent()) {
            return existUser.get().getId();
        }

        final User newUser = userInsert.toEntity();

        return userRepository.save(newUser).getId();
    }
}

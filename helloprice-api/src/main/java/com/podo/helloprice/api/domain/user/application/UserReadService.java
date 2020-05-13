package com.podo.helloprice.api.domain.user.application;

import com.podo.helloprice.api.domain.user.exception.InvalidUserIdApiException;
import com.podo.helloprice.api.domain.user.model.User;
import com.podo.helloprice.api.domain.user.model.UserRole;
import com.podo.helloprice.api.domain.user.model.UserVo;
import com.podo.helloprice.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserReadService {

    private final UserRepository userRepository;

    public UserVo getUser(Long userId) {
        final Optional<User> userOptional = userRepository.findById(userId);

        final User user = userOptional.orElseThrow(() -> new InvalidUserIdApiException(userId));

        return UserVo.createByUser(user);
    }

    public Optional<UserRole> getRoleByKey(String userKey) {
        final Optional<User> userOptional = userRepository.findByUserKey(userKey);
        return userOptional.map(User::getRole);
    }
}

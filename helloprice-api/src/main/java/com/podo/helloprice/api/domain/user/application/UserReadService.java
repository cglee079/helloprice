package com.podo.helloprice.api.domain.user.application;

import com.podo.helloprice.api.domain.user.model.UserRole;
import com.podo.helloprice.api.domain.user.model.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserReadService {
    public UserVo getUser(Long userId) {
        return new UserVo();
    }

    public Optional<UserRole> getRoleByKey(String userKey) {
        return Optional.of(UserRole.USER);
    }
}

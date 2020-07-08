package com.podo.helloprice.product.update.analysis.domain.user.application;

import com.podo.helloprice.core.enums.UserStatus;
import com.podo.helloprice.product.update.analysis.domain.user.dto.UserDto;
import com.podo.helloprice.product.update.analysis.domain.user.model.User;
import com.podo.helloprice.product.update.analysis.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserReadService {

    private final UserRepository userRepository;

    public List<UserDto> findByIdsAndUserStatus(List<Long> userIds, UserStatus userStatus) {

        final List<User> users = userRepository.findByUserIdsAndUserStatus(userIds, userStatus);

        return users.stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }
}

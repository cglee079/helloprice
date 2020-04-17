package com.podo.helloprice.notify.analysis.domain.user;

import com.podo.helloprice.core.model.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserReadService {

    private final UserRepository userRepository;

    public List<String> findTelegramIdsByUserIdsAndUserStatus(List<Long> userIds, UserStatus userStatus){
        return userRepository.findTelegramIdsByUserIdsAndUserStatus(userIds, userStatus);
    }

}

package com.podo.helloprice.product.update.analysis.domain.user;

import com.podo.helloprice.core.model.UserStatus;
import com.podo.helloprice.product.update.analysis.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class UserReadService {

    private final UserRepository userRepository;

    public List<UserDto> findByUserIdsAndUserStatus(List<Long> userIds, UserStatus userStatus){
        if(userIds.isEmpty()){
            return Collections.emptyList();
        }

        return userRepository.findByUserIdsAndUserStatus(userIds, userStatus).stream()
                .map( u -> new UserDto(u.getTelegramId(), u.getUsername(), u.getEmail()))
                .collect(toList());
    }

}

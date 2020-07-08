package com.podo.helloprice.product.update.analysis.domain.tuser;

import com.podo.helloprice.core.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class TUserReadService {

    private final TUserRepository TUserRepository;

    public List<TUserDto> findByUserIdsAndUserStatus(List<Long> userIds, UserStatus userStatus){
        if(userIds.isEmpty()){
            return Collections.emptyList();
        }

        return TUserRepository.findByUserIdsAndUserStatus(userIds, userStatus).stream()
                .map( u -> new TUserDto(u.getTelegramId(), u.getUsername(), u.getEmail()))
                .collect(toList());
    }

}

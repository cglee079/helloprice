package com.podo.helloprice.telegram.domain.tuser.application;

import com.podo.helloprice.telegram.domain.tuser.dto.TUserDetailDto;
import com.podo.helloprice.telegram.domain.tuser.repository.TUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.podo.helloprice.telegram.domain.tuser.application.helper.TUserReadServiceHelper.findUserByTelegramId;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TUserReadService {

    @Value("${user.max_product}")
    private Integer maxCountOfProductNotifies;

    private final TUserRepository userRepository;

    public boolean existedTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId).isPresent();
    }

    public TUserDetailDto findByTelegramId(String telegramId) {
        return new TUserDetailDto(findUserByTelegramId(userRepository, telegramId));
    }

    public boolean hasMaxNotifyByTelegramId(String telegramId) {
        return findUserByTelegramId(userRepository, telegramId)
                .hasProductNotifiesMoreThan(maxCountOfProductNotifies);
    }

    public boolean hasMaxNotifyByTelegramIdIfAdded(String telegramId, int addedSize) {
        return findUserByTelegramId(userRepository, telegramId)
                .hasProductNotifiesMoreThan(maxCountOfProductNotifies - addedSize);
    }

}

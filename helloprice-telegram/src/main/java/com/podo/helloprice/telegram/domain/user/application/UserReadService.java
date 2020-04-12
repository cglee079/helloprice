package com.podo.helloprice.telegram.domain.user.application;

import com.podo.helloprice.telegram.domain.user.dto.UserDetailDto;
import com.podo.helloprice.telegram.domain.user.application.helper.UserReadServiceHelper;
import com.podo.helloprice.telegram.domain.user.exception.InvalidTelegramIdException;
import com.podo.helloprice.telegram.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.podo.helloprice.telegram.domain.user.application.helper.UserReadServiceHelper.findUserByTelegramId;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserReadService {

    @Value("${user.max_product}")
    private Integer maxCountOfProductNotifies;

    private final UserRepository userRepository;

    public boolean existedTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId).isPresent();
    }

    public UserDetailDto findByTelegramId(String telegramId) {
        return new UserDetailDto(findUserByTelegramId(userRepository, telegramId));
    }

    public boolean hasMaxNotifyByUserTelegramId(String telegramId) {
        return findUserByTelegramId(userRepository, telegramId)
                .hasProductNotifiesMoreThan(maxCountOfProductNotifies);
    }

}

package com.podo.helloprice.telegram.domain.user.application;

import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.domain.user.dto.UserInsertDto;
import com.podo.helloprice.telegram.domain.user.model.User;
import com.podo.helloprice.telegram.domain.user.model.UserStatus;
import com.podo.helloprice.telegram.domain.user.repository.UserRepository;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotify;
import com.podo.helloprice.telegram.domain.userproduct.repository.UserProductNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.podo.helloprice.telegram.domain.user.application.helper.UserReadServiceHelper.findUserByTelegramId;
import static com.podo.helloprice.telegram.domain.user.application.helper.UserReadServiceHelper.findUserById;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserWriteService {

    @Value("${user.max_error_count}")
    private Integer userMaxErrorCount;

    private final UserRepository userRepository;
    private final UserProductNotifyRepository userProductNotifyRepository;

    public Long insertNewUser(UserInsertDto userInsert) {
        final User savedUser = userRepository.save(userInsert.toEntity());
        return savedUser.getId();
    }

    public void increaseUserErrorCountByTelegramId(String telegramId) {
        final User user = findUserByTelegramId(userRepository, telegramId);

        user.increaseErrorCount(userMaxErrorCount);

        if (user.getUserStatus().equals(UserStatus.DEAD)) {
            removeUserProductNotifies(user.getUserProductNotifies());
        }
    }

    private void removeUserProductNotifies(List<UserProductNotify> userProductNotifies) {
        for (UserProductNotify userProductNotify : userProductNotifies) {
            userProductNotify.getUser().removeUserProductNotify(userProductNotify);
            userProductNotify.getProduct().removeUserProductNotify(userProductNotify);

            userProductNotifyRepository.delete(userProductNotify);
        }
    }

    public void updateMenuStatusByTelegramId(String telegramId, Menu menu) {
        findUserByTelegramId(userRepository, telegramId).updateMenuStatus(menu);
    }

    public void clearUserErrorCountByTelegramId(String telegramId) {
        findUserByTelegramId(userRepository, telegramId).clearErrorCount();
    }

    public void reviveUser(Long userId) {
        findUserById(userRepository, userId).revive();
    }

    public void updateSendAt(Long userId, LocalDateTime lastSendAt) {
        findUserById(userRepository, userId).updateLastSendAt(lastSendAt);
    }

    public void updateEmailByTelegramId(String telegramId, String email) {
        findUserByTelegramId(userRepository, telegramId).updateEmail(email);
    }
}

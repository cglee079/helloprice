package com.podo.helloprice.telegram.domain.tuser.application;

import com.podo.helloprice.core.enums.UserStatus;
import com.podo.helloprice.telegram.domain.tuser.application.helper.TUserReadServiceHelper;
import com.podo.helloprice.telegram.domain.tuser.dto.TUserInsertDto;
import com.podo.helloprice.telegram.domain.tuser.model.TUser;
import com.podo.helloprice.telegram.domain.tuser.repository.TUserRepository;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.domain.tusernotify.TUserNotify;
import com.podo.helloprice.telegram.domain.tusernotify.repository.TUserNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TUserWriteService {

    @Value("${user.max_error_count}")
    private Integer userMaxErrorCount;

    private final TUserRepository userRepository;
    private final TUserNotifyRepository userProductNotifyRepository;

    public Long insertNewUser(TUserInsertDto userInsert) {
        final TUser savedTUser = userRepository.save(userInsert.toEntity());
        return savedTUser.getId();
    }

    public void increaseUserErrorCountByTelegramId(String telegramId) {
        final TUser tUser = TUserReadServiceHelper.findUserByTelegramId(userRepository, telegramId);

        tUser.increaseErrorCount(userMaxErrorCount);

        if (tUser.getUserStatus().equals(UserStatus.DEAD)) {
            removeUserProductNotifies(tUser.getUserProductNotifies());
        }
    }

    private void removeUserProductNotifies(List<TUserNotify> userProductNotifies) {
        for (TUserNotify tUserNotify : userProductNotifies) {
            tUserNotify.getTUser().removeUserProductNotify(tUserNotify);
            tUserNotify.getProductSale().removeUserProductNotify(tUserNotify);

            userProductNotifyRepository.delete(tUserNotify);
        }
    }

    public void updateMenuStatusByTelegramId(String telegramId, Menu menu) {
        TUserReadServiceHelper.findUserByTelegramId(userRepository, telegramId).updateMenuStatus(menu);
    }

    public void clearUserErrorCountByTelegramId(String telegramId) {
        TUserReadServiceHelper.findUserByTelegramId(userRepository, telegramId).clearErrorCount();
    }

    public void reviveUser(Long userId) {
        TUserReadServiceHelper.findUserById(userRepository, userId).revive();
    }

    public void updateSendAt(Long userId, LocalDateTime lastSendAt) {
        TUserReadServiceHelper.findUserById(userRepository, userId).updateLastSendAt(lastSendAt);
    }

    public void updateEmailByTelegramId(String telegramId, String email) {
        TUserReadServiceHelper.findUserByTelegramId(userRepository, telegramId).updateEmail(email);
    }
}

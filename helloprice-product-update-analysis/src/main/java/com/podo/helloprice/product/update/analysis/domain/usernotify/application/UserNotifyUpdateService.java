package com.podo.helloprice.product.update.analysis.domain.usernotify.application;

import com.podo.helloprice.product.update.analysis.domain.usernotify.model.UserNotify;
import com.podo.helloprice.product.update.analysis.domain.usernotify.repository.UserNotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserNotifyUpdateService {

    private final UserNotifyRepository userNotifyRepository;

    public void updateNotifiedAtByProductId(Long productId, LocalDateTime notifiedAt) {

        final List<UserNotify> userNotifies = userNotifyRepository.findByProductId(productId);

        for (UserNotify userNotify : userNotifies) {
            userNotify.updateLastNotifiedAt(notifiedAt);
        }

    }
}

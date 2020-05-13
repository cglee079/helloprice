package com.podo.helloprice.product.update.analysis.domain.tusernotify.application;

import com.podo.helloprice.product.update.analysis.domain.tusernotify.TUserNotify;
import com.podo.helloprice.product.update.analysis.domain.tusernotify.repository.TUserNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TUserNotifyUpdateService {

    private final TUserNotifyRepository TUserNotifyRepository;

    public void updateNotifiedAtByProductId(Long productId, LocalDateTime notifiedAt) {
        for (TUserNotify TUserNotify : TUserNotifyRepository.findByProductId(productId)) {
            TUserNotify.updateNotifiedAt(notifiedAt);
        }
    }

}

package com.podo.helloprice.product.update.analysis.domain.userproduct.application;

import com.podo.helloprice.product.update.analysis.domain.userproduct.UserProductNotify;
import com.podo.helloprice.product.update.analysis.domain.userproduct.repository.UserProductNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserProductNotifyUpdateService {

    private final UserProductNotifyRepository userProductNotifyRepository;

    public void updateNotifiedAtByProductId(Long productId, LocalDateTime notifiedAt) {
        for (UserProductNotify userProductNotify : userProductNotifyRepository.findByProductId(productId)) {
            userProductNotify.updateNotifiedAt(notifiedAt);
        }
    }

}

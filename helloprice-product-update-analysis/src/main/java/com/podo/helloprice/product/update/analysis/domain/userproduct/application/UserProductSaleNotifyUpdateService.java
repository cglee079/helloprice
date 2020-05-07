package com.podo.helloprice.product.update.analysis.domain.userproduct.application;

import com.podo.helloprice.product.update.analysis.domain.userproduct.UserProductSaleNotify;
import com.podo.helloprice.product.update.analysis.domain.userproduct.repository.UserProductSaleNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserProductSaleNotifyUpdateService {

    private final UserProductSaleNotifyRepository userProductSaleNotifyRepository;

    public void updateNotifiedAtByProductId(Long productId, LocalDateTime notifiedAt) {
        for (UserProductSaleNotify userProductSaleNotify : userProductSaleNotifyRepository.findByProductId(productId)) {
            userProductSaleNotify.updateNotifiedAt(notifiedAt);
        }
    }

}

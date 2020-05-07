package com.podo.helloprice.product.update.analysis.domain.userproduct.application;

import com.podo.helloprice.product.update.analysis.domain.userproduct.UserProductSaleNotify;
import com.podo.helloprice.product.update.analysis.domain.userproduct.repository.UserProductSaleNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserProductSaleNotifyDeleteService {

    private final UserProductSaleNotifyRepository userProductSaleNotifyRepository;

    public void deleteNotifiesByProductId(Long productId) {
        final List<UserProductSaleNotify> userProductNotifies = userProductSaleNotifyRepository.findByProductId(productId);

        for (UserProductSaleNotify userProductSaleNotify : userProductNotifies) {
            this.userProductSaleNotifyRepository.delete(userProductSaleNotify);
        }
    }

}

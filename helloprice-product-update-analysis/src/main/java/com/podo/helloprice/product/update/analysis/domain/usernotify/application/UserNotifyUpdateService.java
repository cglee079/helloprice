package com.podo.helloprice.product.update.analysis.domain.usernotify.application;

import com.podo.helloprice.product.update.analysis.domain.productsale.ProductSale;
import com.podo.helloprice.product.update.analysis.domain.productsale.ProductSaleRepository;
import com.podo.helloprice.product.update.analysis.domain.usernotify.model.UserNotify;
import com.podo.helloprice.product.update.analysis.domain.usernotify.repository.UserNotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserNotifyUpdateService {

    private final UserNotifyRepository userNotifyRepository;
    private final ProductSaleRepository productSaleRepository;

    public void updateNotifiedAtByProductId(Long productId, LocalDateTime notifiedAt) {

        final List<Long> productSaleIds = productSaleRepository.findByProductId(productId)
                .stream()
                .map(ProductSale::getId)
                .collect(Collectors.toList());

        final List<UserNotify> userNotifies = userNotifyRepository.findByProductSaleIdIn(productSaleIds);

        for (UserNotify userNotify : userNotifies) {
            userNotify.updateLastNotifiedAt(notifiedAt);
        }

    }
}

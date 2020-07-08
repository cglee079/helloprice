package com.podo.helloprice.api.domain.usernotify.application;

import com.podo.helloprice.api.domain.productsale.exception.InvalidProductSaleIdApiException;
import com.podo.helloprice.api.domain.productsale.repository.ProductSaleRepository;
import com.podo.helloprice.api.domain.usernotify.model.UserNotify;
import com.podo.helloprice.api.domain.usernotify.dto.UserNotifyAddDto;
import com.podo.helloprice.api.domain.usernotify.exception.AlreadyNotifiedProductSaleApiException;
import com.podo.helloprice.api.domain.usernotify.repository.UserNotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@RequiredArgsConstructor
@Service
public class UserNotifyAddService {

    private final ProductSaleRepository productSaleRepository;
    private final UserNotifyRepository userNotifyRepository;

    public void insert(Long userId, UserNotifyAddDto userNotifyAdd) {

        final Long productSaleId = userNotifyAdd.getProductSaleId();

        if (!productSaleRepository.existsById(productSaleId)) {
            throw new InvalidProductSaleIdApiException(productSaleId);
        }

        if (userNotifyRepository.findByUserIdAndProductSaleId(userId, productSaleId).isPresent()) {
            throw new AlreadyNotifiedProductSaleApiException(productSaleId);
        }

        userNotifyRepository.save(new UserNotify(userId, productSaleId));
    }

}

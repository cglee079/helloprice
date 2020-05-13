package com.podo.helloprice.api.domain.usernotify.application;

import com.podo.helloprice.api.domain.product.model.Product;
import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.api.domain.productsale.exception.InvalidProductSaleIdApiException;
import com.podo.helloprice.api.domain.productsale.repository.ProductSaleRepository;
import com.podo.helloprice.api.domain.usernotify.model.UserNotify;
import com.podo.helloprice.api.domain.usernotify.dto.UserNotifyRemoveDto;
import com.podo.helloprice.api.domain.usernotify.exception.NoNotifiedProductSaleApiException;
import com.podo.helloprice.api.domain.usernotify.repository.UserNotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Transactional
@RequiredArgsConstructor
@Service
public class UserNotifyRemoveService {

    private final UserNotifyRepository userNotifyRepository;
    private final ProductSaleRepository productSaleRepository;

    public void remove(Long userId, UserNotifyRemoveDto userNotifyRemove) {
        final Long productSaleId = userNotifyRemove.getProductSaleId();

        final Optional<ProductSale> productSaleOptional = productSaleRepository.findById(productSaleId);
        final ProductSale productSale = productSaleOptional.orElseThrow(() -> new InvalidProductSaleIdApiException(productSaleId));
        final Product product = productSale.getProduct();

        final Optional<UserNotify> userNotifyOptional = userNotifyRepository.findByUserIdAndProductSaleId(userId, productSaleId);

        final UserNotify userNotify = userNotifyOptional.orElseThrow(() -> new NoNotifiedProductSaleApiException(productSaleId));

        userNotifyRepository.delete(userNotify);

        //알림을 삭제한 상품판매의 상품에 어떤 알림도 없을때, 상품 크롤 중지
        final List<Long> productSaleIds = productSaleRepository.findByProductId(product.getId()).stream()
                .map(ProductSale::getId)
                .collect(Collectors.toList());

        if(userNotifyRepository.findByProductSaleIdIn(productSaleIds).isEmpty()){
            product.pause();
        }

    }
}

package com.podo.helloprice.api.domain.usernotify.application;

import com.podo.helloprice.api.domain.product.exception.InvalidProductIdException;
import com.podo.helloprice.api.domain.product.model.Product;
import com.podo.helloprice.api.domain.product.repository.ProductRepository;
import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.api.domain.productsale.exception.InvalidProductSaleIdApiException;
import com.podo.helloprice.api.domain.productsale.repository.ProductSaleRepository;
import com.podo.helloprice.api.domain.usernotify.dto.UserNotifiesRemoveDto;
import com.podo.helloprice.api.domain.usernotify.dto.UserNotifyRemoveDto;
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

    private final ProductRepository productRepository;
    private final UserNotifyRepository userNotifyRepository;
    private final ProductSaleRepository productSaleRepository;

    public void removeByProductSale(Long userId, UserNotifyRemoveDto userNotifyRemove) {
        final Long productSaleId = userNotifyRemove.getProductSaleId();

        final Optional<ProductSale> productSaleOptional = productSaleRepository.findById(productSaleId);
        final ProductSale productSale = productSaleOptional.orElseThrow(() -> new InvalidProductSaleIdApiException(productSaleId));

        userNotifyRepository.deleteByUserIdAndProductSaleId(userId, productSaleId);

        pauseProductIfEmptyNotify(productSale.getProduct());

    }

    public void removeByProduct(Long userId, UserNotifiesRemoveDto userNotifiesRemoveDto) {
        final Long productId = userNotifiesRemoveDto.getProductId();

        final Product product = productRepository.findById(productId).orElseThrow(() -> new InvalidProductIdException(productId));

        final List<ProductSale> productSales = productSaleRepository.findByProductId(productId);

        for (ProductSale productSale : productSales) {
            userNotifyRepository.deleteByUserIdAndProductSaleId(userId, productSale.getId());
        }

        pauseProductIfEmptyNotify(product);
    }

    //알림을 삭제한 상품판매의 상품에 어떤 알림도 없을때, 상품 크롤 중지
    private void pauseProductIfEmptyNotify(Product product) {
        final List<Long> productSaleIds = productSaleRepository.findByProductId(product.getId()).stream()
                .map(ProductSale::getId)
                .collect(Collectors.toList());

        if(userNotifyRepository.findByProductSaleIdIn(productSaleIds).isEmpty()){
            product.pause();
        }
    }
}

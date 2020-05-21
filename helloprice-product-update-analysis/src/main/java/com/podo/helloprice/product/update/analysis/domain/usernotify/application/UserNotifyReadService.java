package com.podo.helloprice.product.update.analysis.domain.usernotify.application;

import com.podo.helloprice.product.update.analysis.domain.productsale.ProductSale;
import com.podo.helloprice.product.update.analysis.domain.productsale.ProductSaleRepository;
import com.podo.helloprice.product.update.analysis.domain.usernotify.model.UserNotify;
import com.podo.helloprice.product.update.analysis.domain.usernotify.repository.UserNotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Transactional
@RequiredArgsConstructor
@Service
public class UserNotifyReadService {

    private final UserNotifyRepository userNotifyRepository;
    private final ProductSaleRepository productSaleRepository;

    public List<Long> findUserIdsByProductId(Long productId) {
        final List<ProductSale> productSales = productSaleRepository.findByProductId(productId);

        final List<Long> productSaleIds = productSales.stream()
                .map(ProductSale::getId)
                .collect(toList());

        final List<UserNotify> existedUserProductNotifies = userNotifyRepository.findByProductSaleIdIn(productSaleIds);

        return existedUserProductNotifies.stream()
                .distinct()
                .map(UserNotify::getUserId)
                .collect(toList());
    }

    public List<Long> findUserIdsByProductSaleId(Long productSaleId) {
        return null;
    }
}

package com.podo.helloprice.api.domain.productsale.application;

import com.podo.helloprice.api.domain.productsale.dto.ProductTopDecreaseResponseDto;
import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.api.domain.productsale.repository.ProductSaleQuerydslRepository;
import com.podo.helloprice.api.domain.usernotify.model.UserNotify;
import com.podo.helloprice.api.domain.usernotify.repository.UserNotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductSaleTopDecreaseReadService {

    private static final int LIMIT = 10;

    private final UserNotifyRepository userNotifyRepository;
    private final ProductSaleQuerydslRepository productSaleQuerydslRepository;

    public List<ProductTopDecreaseResponseDto> getTopDecrease(Long userId, LocalDateTime now){

        final List<Long> notifiedProductSaleIds = userNotifyRepository.findByUserId(userId).stream()
                .map(UserNotify::getProductSaleId)
                .collect(Collectors.toList());

        final List<ProductSale> productSales = productSaleQuerydslRepository.findTopDecrease(LIMIT, now);

        return productSales.stream()
                .map( ps -> new ProductTopDecreaseResponseDto(ps, notifiedProductSaleIds.contains(ps.getId())))
                .collect(Collectors.toList());
    }


}


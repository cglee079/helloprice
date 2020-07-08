package com.podo.helloprice.api.domain.productsale.api;

import com.podo.helloprice.api.domain.productsale.dto.ProductTopDecreaseResponseDto;
import com.podo.helloprice.api.domain.productsale.application.ProductSaleTopDecreaseReadService;
import com.podo.helloprice.api.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class ProductSaleApi {

    private final ProductSaleTopDecreaseReadService productSaleTopDecreaseReadService;

    @GetMapping("/api/v0/product-sales/top-decrease")
    public List<ProductTopDecreaseResponseDto> getTopDecreaseProductToday(){
        return productSaleTopDecreaseReadService.getTopDecrease(SecurityUtil.getUserId(), LocalDateTime.now());
    }

}

package com.podo.helloprice.api.domain.productsale;

import com.podo.helloprice.api.domain.product.application.ProductByUserReadService;
import com.podo.helloprice.api.domain.product.application.ProductTopDecreaseReadService;
import com.podo.helloprice.api.domain.product.dto.ProductResponseDto;
import com.podo.helloprice.api.domain.product.dto.ProductTopDecreaseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class ProductSaleReadApi {

    private final ProductTopDecreaseReadService productTopDecreaseReadService;
    private final ProductByUserReadService productByUserReadService;

    @GetMapping("/api/v0/product-sales/top-decrease")
    public List<ProductTopDecreaseResponseDto> getTopDecreaseProductToday(){
        return productTopDecreaseReadService.getTopDecrease(LocalDateTime.now());
    }

}

package com.podo.helloprice.api.domain.product.api;

import com.podo.helloprice.api.domain.product.application.ProductByUserReadService;
import com.podo.helloprice.api.domain.product.dto.ProductResponseDto;
import com.podo.helloprice.api.global.response.CollectionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductReadApi {

    private final ProductByUserReadService productByUserReadService;

    @GetMapping("/api/v0/products/mine")
    public CollectionResponse getByUserId(@RequestHeader("userId") Long userId){
        final List<ProductResponseDto> userProducts = productByUserReadService.getUserProducts(userId);
        return CollectionResponse.ok(userProducts);
    }

}

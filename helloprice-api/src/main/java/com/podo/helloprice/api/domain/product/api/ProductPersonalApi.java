package com.podo.helloprice.api.domain.product.api;

import com.podo.helloprice.api.domain.product.application.ProductPersonalReadService;
import com.podo.helloprice.api.domain.product.dto.ProductResponseDto;
import com.podo.helloprice.api.global.rest.response.CollectionResponse;
import com.podo.helloprice.api.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductPersonalApi {


    private final ProductPersonalReadService productPersonalReadService;

    @GetMapping("/api/v0/my/products")
    public CollectionResponse getUserNotifyProducts(){
        final List<ProductResponseDto> userProducts = productPersonalReadService.getUserProducts(SecurityUtil.getUserId());
        return CollectionResponse.ok(userProducts);
    }
}

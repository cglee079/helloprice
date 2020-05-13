package com.podo.helloprice.api.domain.product.api;

import com.podo.helloprice.api.domain.product.application.ProductPersistService;
import com.podo.helloprice.api.domain.product.application.ProductSimpleReadService;
import com.podo.helloprice.api.domain.product.dto.ProductPersistDto;
import com.podo.helloprice.api.domain.product.dto.ProductSimpleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class ProductApi {

    private final ProductPersistService productPersistService;
    private final ProductSimpleReadService productSimpleReadService;

    @PostMapping("/api/v0/products")
    public ProductSimpleDto write(@Valid @RequestBody ProductPersistDto productWrite){
        final Long productId = productPersistService.persist(productWrite, LocalDateTime.now());
        return productSimpleReadService.get(productId);

    }


}

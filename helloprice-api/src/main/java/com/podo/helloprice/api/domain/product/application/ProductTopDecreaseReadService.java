package com.podo.helloprice.api.domain.product.application;

import com.podo.helloprice.api.domain.product.dto.ProductTopDecreaseResponseDto;
import com.podo.helloprice.api.domain.product.model.Product;
import com.podo.helloprice.api.domain.product.repository.ProductQuerydslRepository;
import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.api.domain.productsale.ProductSaleQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ProductTopDecreaseReadService {

    private final ProductQuerydslRepository productQueryDslRepository;
    private final ProductSaleQuerydslRepository productSaleQuerydslRepository;

    public List<ProductTopDecreaseResponseDto> getTopDecrease(LocalDateTime now){

        final List<ProductSale> productSales = productSaleQuerydslRepository.findTopDecrease(now);

        return new ArrayList<>();
    }


}

package com.podo.helloprice.api.domain.product.application;

import com.podo.helloprice.api.domain.product.dto.ProductResponseDto;
import com.podo.helloprice.api.domain.product.dto.ProductTopDecreaseResponseDto;
import com.podo.helloprice.api.domain.product.model.Product;
import com.podo.helloprice.api.domain.product.repository.ProductQuerydslRepository;
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

    public List<ProductTopDecreaseResponseDto> getTopDecrease(LocalDateTime now){

        final Map<Product, Double> topDecrease = productQueryDslRepository.findTopDecrease(now);

        return topDecrease.keySet().stream()
                .map(p -> new ProductTopDecreaseResponseDto(p, topDecrease.get(p)))
                .collect(Collectors.toList());
    }


}

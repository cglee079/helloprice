package com.podo.helloprice.api.domain.product.application;

import com.podo.helloprice.api.domain.product.dto.ProductResponseDto;
import com.podo.helloprice.api.domain.product.repository.ProductQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductByUserReadService {

    private final ProductQuerydslRepository productQuerydslRepository;

    public List<ProductResponseDto> getUserProducts(String userId) {

        productQuerydslRepository.findByUserId(userId);

        return new ArrayList<>();
    }
}

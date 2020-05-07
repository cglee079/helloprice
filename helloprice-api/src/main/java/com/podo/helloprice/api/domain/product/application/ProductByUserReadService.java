package com.podo.helloprice.api.domain.product.application;

import com.podo.helloprice.api.domain.product.dto.ProductResponseDto;
import com.podo.helloprice.api.domain.product.model.Product;
import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.api.domain.productsale.ProductSaleQuerydslRepository;
import com.podo.helloprice.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductByUserReadService {

    private final ProductSaleQuerydslRepository productSaleQuerydslRepository;

    public List<ProductResponseDto> getUserProducts(Long userId) {

        final List<ProductSale> productSales = productSaleQuerydslRepository.findNotifiedByUserId(userId);

        final List<Product> products = productSales.stream()
                .map(ProductSale::getProduct)
                .distinct()
                .collect(Collectors.toList());

        final Map<Product, List<ProductSale>> productToProductSales = productSaleQuerydslRepository.findInProducts(products);

        return productToProductSales.keySet().stream()
                .map(p -> new ProductResponseDto(p, productToProductSales.get(p)))
                .collect(Collectors.toList());
    }
}

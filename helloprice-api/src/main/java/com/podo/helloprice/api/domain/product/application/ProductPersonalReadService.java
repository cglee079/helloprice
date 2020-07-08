package com.podo.helloprice.api.domain.product.application;

import com.podo.helloprice.api.domain.product.dto.ProductResponseDto;
import com.podo.helloprice.api.domain.product.model.Product;
import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.api.domain.productsale.repository.ProductSaleQuerydslRepository;
import com.podo.helloprice.api.domain.productsale.repository.ProductSaleRepository;
import com.podo.helloprice.api.domain.usernotify.model.UserNotify;
import com.podo.helloprice.api.domain.usernotify.repository.UserNotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductPersonalReadService {

    private final UserNotifyRepository userNotifyRepository;
    private final ProductSaleRepository productSaleRepository;
    private final ProductSaleQuerydslRepository productSaleQuerydslRepository;

    public List<ProductResponseDto> getUserProducts(Long userId) {

        final List<UserNotify> notifiedByUserId = userNotifyRepository.findByUserId(userId);

        final List<Long> productSaleIds = notifiedByUserId.stream()
                .map(UserNotify::getProductSaleId)
                .collect(Collectors.toList());

        final List<ProductSale> productSales = productSaleRepository.findByIdIn(productSaleIds);

        final List<Product> products = productSales.stream()
                .map(ProductSale::getProduct)
                .distinct()
                .collect(Collectors.toList());

        final Map<Product, List<ProductSale>> productToProductSales = productSaleQuerydslRepository.findInProducts(products);

        return productToProductSales.keySet().stream()
                .map(p -> new ProductResponseDto(p, productToProductSales.get(p), productSales))
                .collect(Collectors.toList());
    }
}

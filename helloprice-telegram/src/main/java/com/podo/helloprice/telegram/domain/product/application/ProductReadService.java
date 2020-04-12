package com.podo.helloprice.telegram.domain.product.application;

import com.podo.helloprice.code.model.ProductAliveStatus;
import com.podo.helloprice.code.model.ProductUpdateStatus;
import com.podo.helloprice.telegram.domain.product.Product;
import com.podo.helloprice.telegram.domain.product.application.helper.ProductReadHelper;
import com.podo.helloprice.telegram.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.telegram.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ProductReadService {

    private final ProductRepository productRepository;

    public ProductDetailDto findByProductCode(String productCode) {
        final Optional<Product> existedProduct = productRepository.findByProductCode(productCode);
        return existedProduct.map(ProductDetailDto::new).orElse(null);
    }

    public ProductDetailDto findByProductId(Long productId) {
        final Product product = ProductReadHelper.findProductById(productRepository, productId);
        return new ProductDetailDto(product);
    }

    public boolean isExistedByProductCode(String productCode) {
        return productRepository.findByProductCode(productCode).isPresent();
    }

    public List<ProductDetailDto> findByStatusAndUpdateStatus(ProductAliveStatus productStatus, ProductUpdateStatus updateStatus) {
        final List<Product> products = productRepository.findByAliveStatusAndUpdateStatus(productStatus, updateStatus);

        return products.stream()
                .map(ProductDetailDto::new)
                .collect(Collectors.toList());
    }
}

package com.podo.helloprice.product.update.analysis.domain.product.application;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.product.update.analysis.domain.product.application.helper.ProductReadHelper;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductSimpleDto;
import com.podo.helloprice.product.update.analysis.domain.product.model.Product;
import com.podo.helloprice.product.update.analysis.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductReadService {

    private final ProductRepository productRepository;

    public ProductSimpleDto findByProductId(Long productId) {
        final Product product = ProductReadHelper.findProductById(productRepository, productId);
        return new ProductSimpleDto(product);
    }

    public ProductDetailDto findByProductId(Long productId, SaleType saleType) {
        final Product product = ProductReadHelper.findProductById(productRepository, productId);
        return new ProductDetailDto(product, saleType);
    }

}

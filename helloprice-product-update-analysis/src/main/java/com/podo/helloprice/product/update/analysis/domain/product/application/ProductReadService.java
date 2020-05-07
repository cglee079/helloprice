package com.podo.helloprice.product.update.analysis.domain.product.application;

import com.podo.helloprice.product.update.analysis.domain.product.application.helper.ProductReadHelper;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDto;
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

    public ProductDto findByProductId(Long productId) {
        final Product product = ProductReadHelper.findProductById(productRepository, productId);
        return new ProductDto(product);
    }


}

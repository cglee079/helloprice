package com.podo.helloprice.notify.analysis.domain.product.application;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.notify.analysis.domain.product.application.helper.ProductReadHelper;
import com.podo.helloprice.notify.analysis.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.notify.analysis.domain.product.model.Product;
import com.podo.helloprice.notify.analysis.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductReadService {

    private final ProductRepository productRepository;

    public ProductDetailDto findByProductId(Long productId, PriceType priceType) {
        final Product product = ProductReadHelper.findProductById(productRepository, productId);
        return new ProductDetailDto(product, priceType);
    }

}

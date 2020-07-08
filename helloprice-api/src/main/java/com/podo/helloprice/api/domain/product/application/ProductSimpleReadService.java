package com.podo.helloprice.api.domain.product.application;

import com.podo.helloprice.api.domain.product.dto.ProductSimpleDto;
import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.api.domain.productsale.repository.ProductSaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductSimpleReadService {

    private final ProductSaleRepository productSaleRepository;

    public ProductSimpleDto get(Long productId){
        final List<ProductSale> productSales = productSaleRepository.findByProductId(productId);
        return new ProductSimpleDto(productId, productSales);
    }
}

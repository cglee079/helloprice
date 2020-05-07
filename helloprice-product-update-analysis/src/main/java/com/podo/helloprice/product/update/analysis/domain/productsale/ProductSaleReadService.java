package com.podo.helloprice.product.update.analysis.domain.productsale;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.product.update.analysis.domain.productsale.dto.ProductSaleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductSaleReadService {

    private final ProductSaleRepository productSaleRepository;

    public ProductSaleDto findByProductIdAndSaleType(Long productId, SaleType saleType) {
        //TODO
        final Optional<ProductSale> productSale = productSaleRepository.findByProductIdAndSaleType(productId, saleType);
        return new ProductSaleDto(productSale.get());
    }

    public List<ProductSaleDto> findByProductId(Long productId) {
        final List<ProductSale> productSales = productSaleRepository.findByProductId(productId);

        return productSales.stream()
                .map(ProductSaleDto::new)
                .collect(Collectors.toList());
    }
}

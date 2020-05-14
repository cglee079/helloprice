package com.podo.helloprice.product.update.analysis.domain.updatelog;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.domain.product.exception.InvalidProductIdException;
import com.podo.helloprice.product.update.analysis.domain.product.model.Product;
import com.podo.helloprice.product.update.analysis.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductUpdateLogInsertService {

    private final ProductUpdateLogRepository productUpdateLogRepository;
    private final ProductRepository productRepository;

    public void insertNew(Long productId, ProductUpdateStatus productUpdateStatus, LocalDateTime now) {

        final Optional<Product> productOptional = productRepository.findById(productId);

        final Product product = productOptional.orElseThrow(() -> new InvalidProductIdException(productId));

        final ProductUpdateLog productUpdateLog = new ProductUpdateLog(product, productUpdateStatus, now);

        productUpdateLogRepository.save(productUpdateLog);
    }
}

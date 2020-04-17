package com.podo.helloprice.crawl.agent.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductCode(String itemCode);
}

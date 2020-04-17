package com.podo.helloprice.notify.analysis.domain.product.repository;

import com.podo.helloprice.notify.analysis.domain.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}

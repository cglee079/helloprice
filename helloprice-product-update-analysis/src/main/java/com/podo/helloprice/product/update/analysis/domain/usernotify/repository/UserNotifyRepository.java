package com.podo.helloprice.product.update.analysis.domain.usernotify.repository;

import com.podo.helloprice.product.update.analysis.domain.usernotify.model.UserNotify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserNotifyRepository extends JpaRepository<UserNotify, Long>{

    List<UserNotify> findByProductSaleIdIn(List<Long> productSaleIds);
}


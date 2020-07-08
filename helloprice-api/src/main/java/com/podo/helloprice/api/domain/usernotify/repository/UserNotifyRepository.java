package com.podo.helloprice.api.domain.usernotify.repository;

import com.podo.helloprice.api.domain.usernotify.model.UserNotify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserNotifyRepository extends JpaRepository<UserNotify, Long>{

    List<UserNotify> findByUserId(Long userId);

    Optional<UserNotify> findByUserIdAndProductSaleId(Long userId, Long productSaleId);

    List<UserNotify> findByProductSaleIdIn(List<Long> productSaleIds);

    void deleteByUserIdAndProductSaleId(Long userId, Long productSaleId);
}


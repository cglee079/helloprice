package com.podo.helloprice.product.update.analysis.domain.notifylog.repository;

import com.podo.helloprice.product.update.analysis.domain.notifylog.model.NotifyLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyLogRepository extends JpaRepository<NotifyLog, Long> {
}

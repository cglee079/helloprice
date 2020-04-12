package com.podo.helloprice.telegram.domain.notifylog.repository;

import com.podo.helloprice.telegram.domain.notifylog.model.NotifyLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyLogRepository extends JpaRepository<NotifyLog, Long> {
}

package com.podo.helloprice.telegram.domain.notifylog;

import com.podo.helloprice.core.domain.notifylog.NotifyLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class NotifyLogService {

    private final NotifyLogRepository notifyLogRepository;

    public void insertNewNotifyLog(NotifyLogDto notifyLog) {
        notifyLogRepository.save(notifyLog.toEntity());
    }
}

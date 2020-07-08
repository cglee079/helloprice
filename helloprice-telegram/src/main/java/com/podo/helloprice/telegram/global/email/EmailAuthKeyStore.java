package com.podo.helloprice.telegram.global.email;

import com.podo.helloprice.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class EmailAuthKeyStore {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    private Map<String, String> emailKeyStore = new ConcurrentHashMap<>();
    private Map<String, LocalDateTime> keyStoreTimes = new ConcurrentHashMap<>();

    @Value("${email.key.expire_minutes}")
    private long certifyTimeout;

    public String createAuthKey(String email) {
        final String newKey = createNewKey();

        emailKeyStore.put(email, newKey);
        keyStoreTimes.put(newKey, LocalDateTime.now());

        scheduleRemoveExpireKey(email, newKey);

        return newKey;
    }

    private void scheduleRemoveExpireKey(String email, String key) {
        scheduler.schedule(() -> {
            log.debug("EMAIL AUTH :: EXPIRE KEY :: {}({}) 인증시간이 만료되어 삭제합니다", email, key);
            this.emailKeyStore.remove(email);
            this.keyStoreTimes.remove(key);
        }, certifyTimeout, TimeUnit.MINUTES);
    }

    private String createNewKey() {
        final String newKey = String.format("%06d", NumberUtil.getRandomInt(999999));

        if (keyStoreTimes.containsKey(newKey)) {
            return createNewKey();
        }

        return newKey;
    }

    public String getEmailIfCertifiedByAuthKey(String key, LocalDateTime now) {
        final LocalDateTime keyStoreTime = keyStoreTimes.get(key);

        if (Objects.isNull(keyStoreTime)) {
            log.debug("EMAIL AUTH :: 저장되어있지 않은 KEY 입니다 {}", key);
            return null;
        }

        if (isExpiredKey(now, keyStoreTime)) {
            log.debug("EMAIL AUTH :: 시간이 만료된 KEY 입니다 {}", key);
            return null;
        }

        final String email = getEmailByKey(key);

        emailKeyStore.remove(email);
        keyStoreTimes.remove(key);

        return email;
    }

    private boolean isExpiredKey(LocalDateTime now, LocalDateTime keyStoreTime) {
        return now.minusMinutes(certifyTimeout).compareTo(keyStoreTime) > 0;
    }

    private String getEmailByKey(String key) {
        for (String email : emailKeyStore.keySet()) {
            if (emailKeyStore.get(email).equals(key)) {
                return email;
            }
        }

        return null;
    }


}

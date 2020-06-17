package com.podo.helloprice.api.global.infra.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Component
public class FCMInitializer {

    private static final String FIREBASE_CONFIG_PATH = "firebase-admin-sdk.json";

    @PostConstruct
    public void initialize() {
        try {
            final FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream())).build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.debug("firebase initialize");
            }
        } catch (IOException e) {
            log.error("Firebase initialize fail", e);
        }
    }
}

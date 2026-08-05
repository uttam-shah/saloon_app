package com.example.saloon.app.saloon_app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${firebase.credentials.path:}")
    private String credentialsPath;

    /**
     * Returns null (instead of failing app startup) when no credentials are configured,
     * so local/dev builds without a Firebase project still boot. FcmService must guard against a null bean.
     */
    @Bean
    public FirebaseApp firebaseApp() {
        if (credentialsPath == null || credentialsPath.isBlank()) {
            log.warn("firebase.credentials.path is not set — push notifications are disabled.");
            return null;
        }

        try (FileInputStream serviceAccount = new FileInputStream(credentialsPath)) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (!FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.getInstance();
            }
            return FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            log.warn("Failed to initialize Firebase from {} — push notifications are disabled.", credentialsPath, e);
            return null;
        }
    }
}

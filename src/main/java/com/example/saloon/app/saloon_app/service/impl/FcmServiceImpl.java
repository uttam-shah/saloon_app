package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.service.FcmService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService {

    private static final Logger log = LoggerFactory.getLogger(FcmServiceImpl.class);

    // ObjectProvider (not a direct FirebaseApp dependency) because FirebaseConfig.firebaseApp()
    // legitimately returns null when no credentials are configured — Spring treats a null @Bean
    // as "unavailable" for required constructor injection, so a plain FirebaseApp field would
    // fail context startup. ObjectProvider makes the dependency genuinely optional.
    private final ObjectProvider<FirebaseApp> firebaseAppProvider;

    @Override
    public boolean sendPush(String fcmToken, String title, String body, Map<String, String> data) {
        FirebaseApp firebaseApp = firebaseAppProvider.getIfAvailable();

        if (firebaseApp == null || fcmToken == null || fcmToken.isBlank()) {
            return false;
        }

        try {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                    .putAllData(data == null ? Map.of() : data)
                    .build();

            FirebaseMessaging.getInstance(firebaseApp).send(message);
            return true;
        } catch (Exception e) {
            log.warn("FCM push failed for token {}: {}", fcmToken, e.getMessage());
            return false;
        }
    }
}

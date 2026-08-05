package com.example.saloon.app.saloon_app.service;

import java.util.Map;

public interface FcmService {
    boolean sendPush(String fcmToken, String title, String body, Map<String, String> data);
}

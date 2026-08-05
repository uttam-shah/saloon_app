package com.example.saloon.app.saloon_app.service;

public interface WebSocketPushService {
    void pushToUser(String userId, Object payload);
    void pushToShopQueue(String shopId, String serviceId, Object payload);
}

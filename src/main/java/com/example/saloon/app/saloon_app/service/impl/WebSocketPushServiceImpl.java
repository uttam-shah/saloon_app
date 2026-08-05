package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.service.WebSocketPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketPushServiceImpl implements WebSocketPushService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void pushToUser(String userId, Object payload) {
        messagingTemplate.convertAndSendToUser(userId, "/queue/updates", payload);
    }

    @Override
    public void pushToShopQueue(String shopId, String serviceId, Object payload) {
        messagingTemplate.convertAndSend("/topic/shop/" + shopId + "/service/" + serviceId + "/queue", payload);
        messagingTemplate.convertAndSend("/topic/shop/" + shopId + "/queue", payload);
    }
}

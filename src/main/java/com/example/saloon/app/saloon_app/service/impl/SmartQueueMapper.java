package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.dto.SmartQueue.QueueServiceLiteDto;
import com.example.saloon.app.saloon_app.dto.SmartQueue.QueueShopLiteDto;
import com.example.saloon.app.saloon_app.dto.SmartQueue.QueueUserLiteDto;
import com.example.saloon.app.saloon_app.dto.SmartQueue.SmartQueueEntryResponseDto;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.ShopService;
import com.example.saloon.app.saloon_app.entity.SmartQueueEntry;
import com.example.saloon.app.saloon_app.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class SmartQueueMapper {

    public SmartQueueEntryResponseDto toResponseDto(SmartQueueEntry entry) {
        SmartQueueEntryResponseDto dto = new SmartQueueEntryResponseDto();
        dto.setQueueEntryId(entry.getQueueEntryId());
        dto.setShop(toShopLite(entry.getShop()));
        dto.setService(toServiceLite(entry.getService()));
        dto.setUser(toUserLite(entry.getUser()));
        dto.setJoinTime(entry.getJoinTime());
        dto.setQueuePosition(entry.getQueuePosition());
        dto.setEstimatedStartTime(entry.getEstimatedStartTime());
        dto.setArrivalDeadline(entry.getArrivalDeadline());
        dto.setStatus(entry.getStatus());
        dto.setTravelMode(entry.getTravelMode());
        dto.setReachedAt(entry.getReachedAt());
        dto.setCreatedAt(entry.getCreatedAt());
        dto.setUpdatedAt(entry.getUpdatedAt());
        return dto;
    }

    public QueueShopLiteDto toShopLite(SalonShop shop) {
        if (shop == null) return null;
        QueueShopLiteDto dto = new QueueShopLiteDto();
        dto.setShopId(shop.getShopId());
        dto.setShopName(shop.getShopName());
        dto.setCoverImage(shop.getCoverImage());
        return dto;
    }

    public QueueServiceLiteDto toServiceLite(ShopService service) {
        if (service == null) return null;
        QueueServiceLiteDto dto = new QueueServiceLiteDto();
        dto.setServiceId(service.getServiceId());
        dto.setName(service.getName());
        dto.setDurationMinutes(service.getDurationMinutes());
        return dto;
    }

    public QueueUserLiteDto toUserLite(Users user) {
        if (user == null) return null;
        QueueUserLiteDto dto = new QueueUserLiteDto();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        return dto;
    }
}

package com.example.saloon.app.saloon_app.service;

import com.example.saloon.app.saloon_app.dto.SmartQueue.JoinQueueDto;
import com.example.saloon.app.saloon_app.dto.SmartQueue.MyQueueStatusDto;
import com.example.saloon.app.saloon_app.dto.SmartQueue.OwnerQueueViewDto;
import com.example.saloon.app.saloon_app.dto.SmartQueue.ReportedArrivalDto;
import com.example.saloon.app.saloon_app.dto.SmartQueue.SmartQueueEntryResponseDto;

public interface SmartQueueService {

    SmartQueueEntryResponseDto join(JoinQueueDto dto, String userId);

    SmartQueueEntryResponseDto cancel(String queueEntryId, String userId);

    SmartQueueEntryResponseDto reportArrival(String queueEntryId, ReportedArrivalDto dto, String userId);

    SmartQueueEntryResponseDto reportTravelPing(String queueEntryId, ReportedArrivalDto dto, String userId);

    SmartQueueEntryResponseDto ownerMarkComplete(String queueEntryId, String ownerUserId);

    SmartQueueEntryResponseDto selfMarkStarted(String queueEntryId, String userId);

    SmartQueueEntryResponseDto selfMarkComplete(String queueEntryId, String userId);

    MyQueueStatusDto getMyActiveQueueStatus(String userId);

    OwnerQueueViewDto getShopQueueForOwner(String shopId, String ownerUserId, String serviceId);

    /** Auto-transfers any entry whose arrival deadline has passed without them reaching. Called by the scheduled sweep. */
    void sweepExpiredArrivals();

    /** Flags entries entering the "arrive soon" warning window. Called by the scheduled sweep. */
    void sweepApproachingWarnings();
}

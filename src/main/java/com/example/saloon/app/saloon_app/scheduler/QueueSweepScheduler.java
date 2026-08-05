package com.example.saloon.app.saloon_app.scheduler;

import com.example.saloon.app.saloon_app.service.SmartQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueSweepScheduler {

    private final SmartQueueService smartQueueService;

    @Scheduled(fixedDelayString = "${smart-queue.sweep-fixed-delay-ms:45000}")
    public void sweep() {
        smartQueueService.sweepExpiredArrivals();
        smartQueueService.sweepApproachingWarnings();
    }
}

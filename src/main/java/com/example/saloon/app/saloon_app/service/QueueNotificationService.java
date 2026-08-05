package com.example.saloon.app.saloon_app.service;

import com.example.saloon.app.saloon_app.entity.SmartQueueEntry;

public interface QueueNotificationService {
    void notifyPositionChanged(SmartQueueEntry entry, int oldPosition, int newPosition);

    void notifyEtaChanged(SmartQueueEntry entry, long deltaMinutes);

    void notifyArrivalWarning(SmartQueueEntry entry, long minutesRemaining);

    void notifyTransferred(SmartQueueEntry loser, SmartQueueEntry gainer);

    void notifyYourTurn(SmartQueueEntry entry);
}

package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.entity.NotificationLog;
import com.example.saloon.app.saloon_app.entity.NotificationType;
import com.example.saloon.app.saloon_app.entity.SmartQueueEntry;
import com.example.saloon.app.saloon_app.service.FcmService;
import com.example.saloon.app.saloon_app.service.NotificationLogService;
import com.example.saloon.app.saloon_app.service.QueueNotificationService;
import com.example.saloon.app.saloon_app.service.WebSocketPushService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Fans a queue event out to: a persisted NotificationLog (always), a live WebSocket push (best-effort),
 * and an FCM push (best-effort). Business logic (SmartQueueServiceImpl) never talks to transports directly.
 */
@Service
@RequiredArgsConstructor
public class QueueNotificationServiceImpl implements QueueNotificationService {

    private static final Logger log = LoggerFactory.getLogger(QueueNotificationServiceImpl.class);

    private final NotificationLogService notificationLogService;
    private final WebSocketPushService webSocketPushService;
    private final FcmService fcmService;
    private final SmartQueueMapper smartQueueMapper;

    @Override
    public void notifyPositionChanged(SmartQueueEntry entry, int oldPosition, int newPosition) {
        String title = "Queue position updated";
        String body = "You're now #" + newPosition + " in line (was #" + oldPosition + ").";
        dispatch(entry, NotificationType.QUEUE_POSITION_CHANGED, title, body);
    }

    @Override
    public void notifyEtaChanged(SmartQueueEntry entry, long deltaMinutes) {
        String direction = deltaMinutes < 0 ? "earlier" : "later";
        String title = "Estimated time updated";
        String body = "Your estimated turn moved " + Math.abs(deltaMinutes) + " min " + direction + ".";
        dispatch(entry, NotificationType.QUEUE_ETA_CHANGED, title, body);
    }

    @Override
    public void notifyArrivalWarning(SmartQueueEntry entry, long minutesRemaining) {
        String title = "Time to head to the shop";
        String body = "Please arrive within " + minutesRemaining + " min or your turn may be passed to the next person.";
        dispatch(entry, NotificationType.QUEUE_ARRIVAL_WARNING, title, body);
    }

    @Override
    public void notifyTransferred(SmartQueueEntry loser, SmartQueueEntry gainer) {
        dispatch(loser, NotificationType.QUEUE_TRANSFERRED,
                "Your turn was passed on",
                "You didn't reach in time, so your turn moved to the next person in line.");

        if (gainer != null) {
            dispatch(gainer, NotificationType.QUEUE_TRANSFERRED,
                    "You moved up!",
                    "Someone ahead of you missed their window — you're up sooner now.");
        }
    }

    @Override
    public void notifyYourTurn(SmartQueueEntry entry) {
        dispatch(entry, NotificationType.QUEUE_YOUR_TURN,
                "You're up!",
                "It's your turn — head to the counter.");
    }

    private void dispatch(SmartQueueEntry entry, NotificationType type, String title, String body) {
        NotificationLog logRow = notificationLogService.record(entry.getUser(), type, title, body, entry.getQueueEntryId());

        try {
            webSocketPushService.pushToUser(entry.getUser().getUserId(), smartQueueMapper.toResponseDto(entry));
        } catch (Exception e) {
            log.warn("WebSocket push failed for user {}: {}", entry.getUser().getUserId(), e.getMessage());
        }

        boolean delivered = false;
        try {
            delivered = fcmService.sendPush(
                    entry.getUser().getFcmToken(),
                    title,
                    body,
                    Map.of("type", type.name(), "queueEntryId", entry.getQueueEntryId())
            );
        } catch (Exception e) {
            log.warn("FCM push failed for user {}: {}", entry.getUser().getUserId(), e.getMessage());
        }

        notificationLogService.markPushDelivered(logRow.getNotificationId(), delivered);
    }
}

package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.dto.SmartQueue.*;
import com.example.saloon.app.saloon_app.entity.*;
import com.example.saloon.app.saloon_app.exception.BadRequestException;
import com.example.saloon.app.saloon_app.exception.ForbiddenException;
import com.example.saloon.app.saloon_app.exception.ResourceNotFoundException;
import com.example.saloon.app.saloon_app.repository.AuthRepository;
import com.example.saloon.app.saloon_app.repository.SalonShopRepository;
import com.example.saloon.app.saloon_app.repository.ShopServiceRepository;
import com.example.saloon.app.saloon_app.repository.SmartQueueEntryRepository;
import com.example.saloon.app.saloon_app.service.QueueNotificationService;
import com.example.saloon.app.saloon_app.service.SmartQueueService;
import com.example.saloon.app.saloon_app.service.WebSocketPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SmartQueueServiceImpl implements SmartQueueService {

    private static final List<SmartQueueStatus> ACTIVE_STATUSES = List.of(
            SmartQueueStatus.WAITING, SmartQueueStatus.NOTIFIED_APPROACHING,
            SmartQueueStatus.REACHED, SmartQueueStatus.IN_SERVICE
    );
    private static final List<SmartQueueStatus> QUEUE_ORDER_STATUSES = List.of(
            SmartQueueStatus.WAITING, SmartQueueStatus.NOTIFIED_APPROACHING, SmartQueueStatus.REACHED
    );

    private final SmartQueueEntryRepository smartQueueEntryRepository;
    private final SalonShopRepository salonShopRepository;
    private final ShopServiceRepository shopServiceRepository;
    private final AuthRepository authRepository;
    private final QueueNotificationService queueNotificationService;
    private final WebSocketPushService webSocketPushService;
    private final SmartQueueMapper smartQueueMapper;

    @Value("${smart-queue.buffer-people-count:2}")
    private int bufferPeopleCount;

    @Value("${smart-queue.approaching-warning-minutes:15}")
    private int approachingWarningMinutes;

    @Override
    @Transactional
    public SmartQueueEntryResponseDto join(JoinQueueDto dto, String userId) {
        System.out.println("joint the queue");

        Users user = authRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SalonShop shop = salonShopRepository.findById(dto.getShopId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));

        ShopService service = shopServiceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        if (!service.getShop().getShopId().equals(shop.getShopId())) {
            throw new BadRequestException("Service does not belong to this shop");
        }

        if (!Boolean.TRUE.equals(service.getIsSmartQueueEnabled())) {
            throw new BadRequestException("Smart queue is not enabled for this service");
        }

        smartQueueEntryRepository.findFirstByUser_UserIdAndStatusIn(userId, ACTIVE_STATUSES)
                .ifPresent(existing -> {
                    throw new BadRequestException("You already have an active smart queue entry");
                });

        int tailPosition = smartQueueEntryRepository
                .findByShop_ShopIdAndService_ServiceIdAndStatusInOrderByQueuePositionAsc(
                        shop.getShopId(), service.getServiceId(), QUEUE_ORDER_STATUSES)
                .size() + 1;

        SmartQueueEntry entry = SmartQueueEntry.builder()
                .user(user)
                .shop(shop)
                .service(service)
                .joinTime(LocalDateTime.now())
                .queuePosition(tailPosition)
                .status(SmartQueueStatus.WAITING)
                .travelMode(dto.getTravelMode())
                .reportedTravelTimeMinutes(dto.getInitialTravelTimeMinutes())
                .build();

        smartQueueEntryRepository.save(entry);

        recomputeQueue(shop.getShopId(), service.getServiceId());

        return smartQueueMapper.toResponseDto(smartQueueEntryRepository.findById(entry.getQueueEntryId()).orElseThrow());
    }

    @Override
    @Transactional
    public SmartQueueEntryResponseDto cancel(String queueEntryId, String userId) {
        SmartQueueEntry entry = requireActiveEntry(queueEntryId);

        if (!entry.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("You do not own this queue entry");
        }

        entry.setStatus(SmartQueueStatus.CANCELLED);
        smartQueueEntryRepository.save(entry);

        recomputeQueue(entry.getShop().getShopId(), entry.getService().getServiceId());

        return smartQueueMapper.toResponseDto(entry);
    }

    @Override
    @Transactional
    public SmartQueueEntryResponseDto reportArrival(String queueEntryId, ReportedArrivalDto dto, String userId) {
        SmartQueueEntry entry = requireActiveEntry(queueEntryId);

        if (!entry.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("You do not own this queue entry");
        }

        if (dto.getLat() != null) entry.setLastLat(dto.getLat());
        if (dto.getLng() != null) entry.setLastLng(dto.getLng());
        entry.setReachedAt(LocalDateTime.now());
        entry.setStatus(SmartQueueStatus.REACHED);
        smartQueueEntryRepository.save(entry);

        recomputeQueue(entry.getShop().getShopId(), entry.getService().getServiceId());

        return smartQueueMapper.toResponseDto(entry);
    }

    @Override
    @Transactional
    public SmartQueueEntryResponseDto reportTravelPing(String queueEntryId, ReportedArrivalDto dto, String userId) {
        SmartQueueEntry entry = requireActiveEntry(queueEntryId);

        if (!entry.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("You do not own this queue entry");
        }

        if (dto.getLat() != null) entry.setLastLat(dto.getLat());
        if (dto.getLng() != null) entry.setLastLng(dto.getLng());
        if (dto.getTravelTimeRemainingMinutes() != null) {
            entry.setReportedTravelTimeMinutes(dto.getTravelTimeRemainingMinutes());
        }
        smartQueueEntryRepository.save(entry);

        boolean willMissDeadline = entry.getReachedAt() == null
                && entry.getArrivalDeadline() != null
                && dto.getTravelTimeRemainingMinutes() != null
                && LocalDateTime.now().plusMinutes(dto.getTravelTimeRemainingMinutes()).isAfter(entry.getArrivalDeadline());

        if (willMissDeadline) {
            autoTransfer(entry);
        }

        return smartQueueMapper.toResponseDto(
                smartQueueEntryRepository.findById(queueEntryId).orElseThrow());
    }

    @Override
    @Transactional
    public SmartQueueEntryResponseDto ownerMarkComplete(String queueEntryId, String ownerUserId) {
        SmartQueueEntry entry = requireActiveEntry(queueEntryId);

        if (!entry.getShop().getOwner().getUserId().equals(ownerUserId)) {
            throw new ForbiddenException("You do not own this shop");
        }

        entry.setStatus(SmartQueueStatus.COMPLETED);
        entry.setCompletedAt(LocalDateTime.now());
        smartQueueEntryRepository.save(entry);

        recomputeQueue(entry.getShop().getShopId(), entry.getService().getServiceId());

        return smartQueueMapper.toResponseDto(entry);
    }

    @Override
    public SmartQueueEntryResponseDto selfMarkStarted(String queueEntryId, String userId) {
        throw new BadRequestException("Self-service start is not enabled for this shop yet");
    }

    @Override
    public SmartQueueEntryResponseDto selfMarkComplete(String queueEntryId, String userId) {
        throw new BadRequestException("Self-service completion is not enabled for this shop yet");
    }

    @Override
    public MyQueueStatusDto getMyActiveQueueStatus(String userId) {
        SmartQueueEntry entry = smartQueueEntryRepository
                .findFirstByUser_UserIdAndStatusIn(userId, ACTIVE_STATUSES)
                .orElse(null);

        if (entry == null) {
            return null;
        }

        List<SmartQueueEntry> activeEntries = smartQueueEntryRepository
                .findByShop_ShopIdAndService_ServiceIdAndStatusInOrderByQueuePositionAsc(
                        entry.getShop().getShopId(), entry.getService().getServiceId(), QUEUE_ORDER_STATUSES);

        long peopleAhead = activeEntries.stream()
                .filter(e -> e.getQueuePosition() != null && entry.getQueuePosition() != null
                        && e.getQueuePosition() < entry.getQueuePosition())
                .count();

        MyQueueStatusDto dto = new MyQueueStatusDto();
        dto.setQueueEntryId(entry.getQueueEntryId());
        dto.setShop(smartQueueMapper.toShopLite(entry.getShop()));
        dto.setService(smartQueueMapper.toServiceLite(entry.getService()));
        dto.setJoinTime(entry.getJoinTime());
        dto.setQueuePosition(entry.getQueuePosition());
        dto.setEstimatedStartTime(entry.getEstimatedStartTime());
        dto.setArrivalDeadline(entry.getArrivalDeadline());
        dto.setStatus(entry.getStatus());
        dto.setTravelMode(entry.getTravelMode());
        dto.setReachedAt(entry.getReachedAt());
        dto.setPeopleAheadCount((int) peopleAhead);
        dto.setMinutesUntilDeadline(entry.getArrivalDeadline() == null
                ? null
                : Duration.between(LocalDateTime.now(), entry.getArrivalDeadline()).toMinutes());

        return dto;
    }

    @Override
    public OwnerQueueViewDto getShopQueueForOwner(String shopId, String ownerUserId, String serviceId) {
        SalonShop shop = salonShopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));

        if (!shop.getOwner().getUserId().equals(ownerUserId)) {
            throw new ForbiddenException("You do not own this shop");
        }

        List<SmartQueueEntry> entries = (serviceId != null && !serviceId.isBlank())
                ? smartQueueEntryRepository.findByShop_ShopIdAndService_ServiceIdAndStatusInOrderByQueuePositionAsc(
                        shopId, serviceId, QUEUE_ORDER_STATUSES)
                : smartQueueEntryRepository.findByShop_ShopIdAndStatusIn(shopId, QUEUE_ORDER_STATUSES);

        OwnerQueueViewDto view = new OwnerQueueViewDto();
        view.setShopId(shopId);
        view.setTotalWaiting(entries.size());
        view.setEntries(entries.stream().map(smartQueueMapper::toResponseDto).collect(Collectors.toList()));
        return view;
    }

    @Override
    @Transactional
    public void sweepExpiredArrivals() {
        List<SmartQueueEntry> expired = smartQueueEntryRepository.findByStatusInAndArrivalDeadlineBefore(
                List.of(SmartQueueStatus.WAITING, SmartQueueStatus.NOTIFIED_APPROACHING), LocalDateTime.now());

        for (SmartQueueEntry entry : expired) {
            if (entry.getReachedAt() == null) {
                autoTransfer(entry);
            }
        }
    }

    @Override
    @Transactional
    public void sweepApproachingWarnings() {
        LocalDateTime now = LocalDateTime.now();
        List<SmartQueueEntry> approaching = smartQueueEntryRepository.findByStatusAndArrivalDeadlineBetween(
                SmartQueueStatus.WAITING, now, now.plusMinutes(approachingWarningMinutes));

        for (SmartQueueEntry entry : approaching) {
            entry.setStatus(SmartQueueStatus.NOTIFIED_APPROACHING);
            smartQueueEntryRepository.save(entry);
            long minutesRemaining = Duration.between(now, entry.getArrivalDeadline()).toMinutes();
            queueNotificationService.notifyArrivalWarning(entry, Math.max(minutesRemaining, 0));
        }
    }

    /**
     * Centralized recompute: reassigns position/ETA/deadline for every active entry in a shop+service queue,
     * in join order, and fires position/ETA-change notifications for anyone whose values shifted.
     * Called at the end of every mutation (join, cancel, complete, reportArrival) and by the scheduled sweep.
     */
    private void recomputeQueue(String shopId, String serviceId) {
        ShopService service = shopServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        int avgDuration = service.getDurationMinutes();

        List<SmartQueueEntry> nonTerminal = smartQueueEntryRepository
                .findByShop_ShopIdAndService_ServiceIdAndStatusInOrderByQueuePositionAsc(
                        shopId, serviceId, ACTIVE_STATUSES);

        SmartQueueEntry inService = nonTerminal.stream()
                .filter(e -> e.getStatus() == SmartQueueStatus.IN_SERVICE)
                .findFirst()
                .orElse(null);

        List<SmartQueueEntry> waitingEntries = nonTerminal.stream()
                .filter(e -> e.getStatus() != SmartQueueStatus.IN_SERVICE)
                .collect(Collectors.toList());

        LocalDateTime runningStartTime;
        if (inService != null) {
            LocalDateTime base = inService.getServiceStartedAt() != null
                    ? inService.getServiceStartedAt()
                    : LocalDateTime.now();
            runningStartTime = base.plusMinutes(avgDuration);
        } else {
            runningStartTime = LocalDateTime.now();
        }

        int position = 1;
        for (SmartQueueEntry entry : waitingEntries) {
            Integer oldPosition = entry.getQueuePosition();
            LocalDateTime oldEta = entry.getEstimatedStartTime();

            entry.setQueuePosition(position);
            entry.setEstimatedStartTime(runningStartTime);
            entry.setArrivalDeadline(runningStartTime.minusMinutes((long) bufferPeopleCount * avgDuration));
            runningStartTime = runningStartTime.plusMinutes(avgDuration);

            smartQueueEntryRepository.save(entry);

            boolean becameFirst = entry.getQueuePosition() == 1 && (oldPosition == null || oldPosition != 1);
            if (becameFirst) {
                queueNotificationService.notifyYourTurn(entry);
            } else if (oldPosition != null && !oldPosition.equals(entry.getQueuePosition())) {
                queueNotificationService.notifyPositionChanged(entry, oldPosition, entry.getQueuePosition());
            }

            if (oldEta != null) {
                long deltaMinutes = Duration.between(oldEta, entry.getEstimatedStartTime()).toMinutes();
                if (deltaMinutes != 0) {
                    queueNotificationService.notifyEtaChanged(entry, deltaMinutes);
                }
            }

            position++;
        }

        OwnerQueueViewDto ownerView = new OwnerQueueViewDto();
        ownerView.setShopId(shopId);
        ownerView.setTotalWaiting(waitingEntries.size());
        ownerView.setEntries(waitingEntries.stream().map(smartQueueMapper::toResponseDto).collect(Collectors.toList()));
        webSocketPushService.pushToShopQueue(shopId, serviceId, ownerView);
    }

    /**
     * Marks an entry as auto-skipped for missing its arrival deadline, notifies the loser and whoever
     * benefits, then recomputes the queue so everyone behind shifts up.
     */
    private void autoTransfer(SmartQueueEntry entry) {
        String shopId = entry.getShop().getShopId();
        String serviceId = entry.getService().getServiceId();
        Integer vacatedPosition = entry.getQueuePosition();

        entry.setStatus(SmartQueueStatus.NO_SHOW_TRANSFERRED);
        smartQueueEntryRepository.save(entry);

        SmartQueueEntry gainer = smartQueueEntryRepository
                .findByShop_ShopIdAndService_ServiceIdAndStatusInOrderByQueuePositionAsc(shopId, serviceId, QUEUE_ORDER_STATUSES)
                .stream()
                .filter(e -> vacatedPosition == null || (e.getQueuePosition() != null && e.getQueuePosition() >= vacatedPosition))
                .findFirst()
                .orElse(null);

        queueNotificationService.notifyTransferred(entry, gainer);

        recomputeQueue(shopId, serviceId);
    }

    private SmartQueueEntry requireActiveEntry(String queueEntryId) {
        SmartQueueEntry entry = smartQueueEntryRepository.findById(queueEntryId)
                .orElseThrow(() -> new ResourceNotFoundException("Queue entry not found"));

        if (!ACTIVE_STATUSES.contains(entry.getStatus())) {
            throw new BadRequestException("This queue entry is no longer active");
        }

        return entry;
    }
}

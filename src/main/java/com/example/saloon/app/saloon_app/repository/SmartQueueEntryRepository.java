package com.example.saloon.app.saloon_app.repository;

import com.example.saloon.app.saloon_app.entity.SmartQueueEntry;
import com.example.saloon.app.saloon_app.entity.SmartQueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SmartQueueEntryRepository extends JpaRepository<SmartQueueEntry, String> {

    List<SmartQueueEntry> findByShop_ShopIdAndService_ServiceIdAndStatusInOrderByQueuePositionAsc(
            String shopId, String serviceId, List<SmartQueueStatus> statuses);

    Optional<SmartQueueEntry> findFirstByUser_UserIdAndStatusIn(String userId, List<SmartQueueStatus> statuses);

    List<SmartQueueEntry> findByShop_ShopIdAndStatusIn(String shopId, List<SmartQueueStatus> statuses);

    List<SmartQueueEntry> findByStatusInAndArrivalDeadlineBefore(List<SmartQueueStatus> statuses, LocalDateTime cutoff);

    List<SmartQueueEntry> findByStatusAndArrivalDeadlineBetween(SmartQueueStatus status, LocalDateTime from, LocalDateTime to);
}

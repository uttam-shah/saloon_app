package com.example.saloon.app.saloon_app.controller;

import com.example.saloon.app.saloon_app.config.security.CurrentUser;
import com.example.saloon.app.saloon_app.dto.SmartQueue.*;
import com.example.saloon.app.saloon_app.service.SmartQueueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/smart-queue")
public class SmartQueueController {

    private final SmartQueueService smartQueueService;

    @PostMapping("/join")
    public ResponseEntity<SmartQueueEntryResponseDto> join(@RequestBody @Valid JoinQueueDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(smartQueueService.join(dto, CurrentUser.id()));
    }

    @DeleteMapping("/{queueEntryId}")
    public ResponseEntity<SmartQueueEntryResponseDto> cancel(@PathVariable String queueEntryId) {
        return ResponseEntity.ok(smartQueueService.cancel(queueEntryId, CurrentUser.id()));
    }

    @PostMapping("/{queueEntryId}/reached")
    public ResponseEntity<SmartQueueEntryResponseDto> reportArrival(
            @PathVariable String queueEntryId,
            @RequestBody(required = false) ReportedArrivalDto dto) {
        return ResponseEntity.ok(smartQueueService.reportArrival(
                queueEntryId, dto != null ? dto : new ReportedArrivalDto(), CurrentUser.id()));
    }

    @PostMapping("/{queueEntryId}/travel-ping")
    public ResponseEntity<SmartQueueEntryResponseDto> travelPing(
            @PathVariable String queueEntryId,
            @RequestBody ReportedArrivalDto dto) {
        return ResponseEntity.ok(smartQueueService.reportTravelPing(queueEntryId, dto, CurrentUser.id()));
    }

    @PatchMapping("/{queueEntryId}/owner-complete")
    public ResponseEntity<SmartQueueEntryResponseDto> ownerComplete(@PathVariable String queueEntryId) {
        return ResponseEntity.ok(smartQueueService.ownerMarkComplete(queueEntryId, CurrentUser.id()));
    }

    @PatchMapping("/{queueEntryId}/self-start")
    public ResponseEntity<SmartQueueEntryResponseDto> selfStart(@PathVariable String queueEntryId) {
        return ResponseEntity.ok(smartQueueService.selfMarkStarted(queueEntryId, CurrentUser.id()));
    }

    @PatchMapping("/{queueEntryId}/self-complete")
    public ResponseEntity<SmartQueueEntryResponseDto> selfComplete(@PathVariable String queueEntryId) {
        return ResponseEntity.ok(smartQueueService.selfMarkComplete(queueEntryId, CurrentUser.id()));
    }

    @GetMapping("/me")
    public ResponseEntity<MyQueueStatusDto> myStatus() {
        MyQueueStatusDto status = smartQueueService.getMyActiveQueueStatus(CurrentUser.id());
        return status == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(status);
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<OwnerQueueViewDto> shopQueue(
            @PathVariable String shopId,
            @RequestParam(required = false) String serviceId) {
        return ResponseEntity.ok(smartQueueService.getShopQueueForOwner(shopId, CurrentUser.id(), serviceId));
    }
}

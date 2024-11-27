package com.ahold.ctp.controller

import com.ahold.ctp.dto.BusinessSummaryDTO
import com.ahold.ctp.dto.DeliveryCreateDTO
import com.ahold.ctp.dto.DeliveryUpdateDTO
import com.ahold.ctp.model.Delivery
import com.ahold.ctp.service.DeliveryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID
import com.ahold.ctp.model.DeliveryStatus
import java.time.Instant

@RestController
@RequestMapping("/deliveries")
class DeliveryController(
    private val deliveryService: DeliveryService
) {
    @PostMapping
    fun createDelivery(@RequestBody createDTO: DeliveryCreateDTO): ResponseEntity<Delivery> {
        val delivery = Delivery(
            vehicleId = createDTO.vehicleId,
            startedAt = createDTO.startedAt,
            status = createDTO.status
        )
        return ResponseEntity.ok(deliveryService.createDelivery(delivery))
    }

    @PatchMapping("/{id}")
    fun updateDelivery(
        @PathVariable id: UUID,
        @RequestBody updateDTO: DeliveryUpdateDTO
    ): ResponseEntity<Delivery> {
        return ResponseEntity.ok(deliveryService.updateDelivery(id, updateDTO))
    }

    @PatchMapping("/bulk-update")
    fun bulkUpdateDeliveries(
        @RequestBody updates: List<Map<String, Any>>
    ): ResponseEntity<Map<String, List<Delivery>>> {
        val processedUpdates = updates.map { update ->
            val id = UUID.fromString(update["id"].toString())
            val updateDTO = DeliveryUpdateDTO(
                finishedAt = update["finishedAt"]?.let { Instant.parse(it.toString()) },
                status = DeliveryStatus.valueOf(update["status"].toString())
            )
            id to updateDTO
        }

        val updatedDeliveries = deliveryService.bulkUpdateDeliveries(processedUpdates)
        return ResponseEntity.ok(mapOf("deliveries" to updatedDeliveries))
    }

    @GetMapping("/business-summary")
    fun getBusinessSummary(): ResponseEntity<BusinessSummaryDTO> {
        return ResponseEntity.ok(deliveryService.getBusinessSummary())
    }
}
package com.ahold.ctp.service

import com.ahold.ctp.dto.BusinessSummaryDTO
import com.ahold.ctp.dto.DeliveryUpdateDTO
import com.ahold.ctp.model.Delivery
import com.ahold.ctp.model.DeliveryStatus
import com.ahold.ctp.repository.DeliveryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class DeliveryService(
    private val deliveryRepository: DeliveryRepository
) {
    @Transactional
    fun createDelivery(delivery: Delivery): Delivery {
        // Validate status on creation
        require(delivery.status in listOf(DeliveryStatus.IN_PROGRESS, DeliveryStatus.DELIVERED)) {
            "Invalid delivery status"
        }
        return deliveryRepository.save(delivery)
    }

    @Transactional
    fun updateDelivery(id: UUID, updateDTO: DeliveryUpdateDTO): Delivery {
        val delivery = deliveryRepository.findById(id)
            .orElseThrow { NoSuchElementException("Delivery not found") }

        // Validate status
        require(updateDTO.status in listOf(DeliveryStatus.IN_PROGRESS, DeliveryStatus.DELIVERED)) {
            "Invalid delivery status"
        }

        // Validate finishedAt for DELIVERED status
        if (updateDTO.status == DeliveryStatus.DELIVERED) {
            require(updateDTO.finishedAt != null) {
                "finishedAt must be provided for DELIVERED status"
            }
        }

        delivery.status = updateDTO.status
        delivery.finishedAt = updateDTO.finishedAt

        return deliveryRepository.save(delivery)
    }

    @Transactional
    fun bulkUpdateDeliveries(updates: List<Pair<UUID, DeliveryUpdateDTO>>): List<Delivery> {
        return updates.map { (id, updateDTO) ->
            updateDelivery(id, updateDTO)
        }
    }

    fun getBusinessSummary(): BusinessSummaryDTO {
        val amsterdamZone = ZoneId.of("Europe/Amsterdam")
        val yesterday = LocalDate.now(amsterdamZone).minusDays(1)

        val startOfYesterday = yesterday.atStartOfDay(amsterdamZone).toInstant()
        val startOfToday = yesterday.plusDays(1).atStartOfDay(amsterdamZone).toInstant()

        val deliveries = deliveryRepository.findYesterdaysDeliveries(startOfYesterday, startOfToday)

        // Calculate average time between delivery starts
        val sortedDeliveryStarts = deliveries.map { it.startedAt }.sorted()
        val timeDifferences = sortedDeliveryStarts.zipWithNext { a, b ->
            ChronoUnit.MINUTES.between(a, b)
        }

        val averageMinutesBetweenStarts = if (timeDifferences.isNotEmpty()) {
            timeDifferences.average().toInt()
        } else 0

        return BusinessSummaryDTO(
            deliveries = deliveries.size,
            averageMinutesBetweenDeliveryStart = averageMinutesBetweenStarts
        )
    }
}
package com.ahold.ctp.dto

import com.ahold.ctp.model.DeliveryStatus
import java.time.Instant
import java.util.UUID

data class DeliveryCreateDTO(
    val vehicleId: String,
    val startedAt: Instant,
    val status: DeliveryStatus
)

data class DeliveryUpdateDTO(
    val finishedAt: Instant? = null,
    val status: DeliveryStatus
)

data class BusinessSummaryDTO(
    val deliveries: Int,
    val averageMinutesBetweenDeliveryStart: Int
)
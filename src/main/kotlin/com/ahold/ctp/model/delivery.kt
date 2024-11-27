package com.ahold.ctp.model

import jakarta.persistence.*
import java.time.Instant

@Entity
data class Delivery(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, // Primary Key, auto-generated
    val vehicleId: String,
    val startedAt: Instant,
    var finishedAt: Instant? = null,
    @Enumerated(EnumType.STRING)
    var status: DeliveryStatus
) {
    // Default constructor needed for Hibernate
    constructor() : this(
        id = null,  // leave it null for auto-generated value
        vehicleId = "",  // default value or leave empty if vehicleId is required
        startedAt = Instant.now(),  // set a default Instant
        finishedAt = null,  // set finishedAt to null if not available
        status = DeliveryStatus.IN_PROGRESS  // set a default status or use the first status
    )
}

enum class DeliveryStatus {
    IN_PROGRESS, DELIVERED
}
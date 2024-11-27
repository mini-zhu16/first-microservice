package com.ahold.ctp.repository

import com.ahold.ctp.model.Delivery
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
interface DeliveryRepository : JpaRepository<Delivery, UUID> {
    @Query("""
        SELECT d FROM Delivery d 
        WHERE d.startedAt >= :startOfYesterday 
        AND d.startedAt < :startOfToday
    """)
    fun findYesterdaysDeliveries(
        startOfYesterday: Instant,
        startOfToday: Instant
    ): List<Delivery>
}
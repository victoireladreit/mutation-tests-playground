package com.mutation_tests_playground.virus.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "virus")
data class Virus(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val transmissionRate: Double,    // 0.0 to 1.0
    val incubationHours: Int,
    val lethality: Double            // 0.0 to 1.0
)
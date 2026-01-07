package com.mutation_tests_playground.human.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "human")
data class Human(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val isInfected: Boolean = false,
    val isImmune: Boolean = false,
    val infectionDate: LocalDateTime? = null
)
package com.mutation_tests_playground.zombie.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "zombie")
data class Zombie(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val strength: Int,               // 1 to 10
    val speed: Int,                  // 1 to 10
    val transformationDate: LocalDateTime = LocalDateTime.now()
)
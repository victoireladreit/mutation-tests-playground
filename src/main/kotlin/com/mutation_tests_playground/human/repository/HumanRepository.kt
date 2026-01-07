package com.mutation_tests_playground.human.repository

import com.mutation_tests_playground.human.model.Human
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface HumanRepository : JpaRepository<Human, UUID> {
    fun countByIsInfectedFalseAndIsImmuneFalse(): Int
    fun countByIsInfectedTrue(): Int
    fun countByIsImmuneTrue(): Int
    fun findAllByIsInfectedTrue(): List<Human>
}

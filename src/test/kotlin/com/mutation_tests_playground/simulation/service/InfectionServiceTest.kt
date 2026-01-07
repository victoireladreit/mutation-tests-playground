package com.mutation_tests_playground.simulation.service

import com.mutation_tests_playground.human.model.Human
import com.mutation_tests_playground.human.repository.HumanRepository
import com.mutation_tests_playground.simulation.model.InfectionOutcome
import com.mutation_tests_playground.virus.model.Virus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class InfectionServiceTest {

    private val humanRepository = mock(HumanRepository::class.java)
    private val infectionService = InfectionService(humanRepository)

    @Test
    fun `should return IMMUNE when human is immune`() {
        val human = Human(UUID.randomUUID(), "Alice", isInfected = false, isImmune = true, infectionDate = null)
        val virus = Virus(UUID.randomUUID(), "Zombie-X", 0.5, 24, 0.3)

        val result = infectionService.attemptInfection(human, virus)

        assertThat(result.outcome).isEqualTo(InfectionOutcome.IMMUNE)
        verifyNoInteractions(humanRepository)
    }

    @Test
    fun `should return SUCCESS when human is already infected`() {
        val human = Human(UUID.randomUUID(), "Bob", isInfected = true, isImmune = false, infectionDate = null)
        val virus = Virus(UUID.randomUUID(), "Zombie-X", 0.5, 24, 0.3)

        val result = infectionService.attemptInfection(human, virus)

        assertThat(result.outcome).isEqualTo(InfectionOutcome.SUCCESS)
        assertThat(result.message).isEqualTo("L'humain Bob est déjà infecté.")
        verifyNoInteractions(humanRepository)
    }

    @Test
    fun `should succeed when transmission rate is 100 percent`() {
        val human = Human(UUID.randomUUID(), "Charlie", isInfected = false, isImmune = false, infectionDate = null)
        val virus = Virus(UUID.randomUUID(), "Zombie-X", 1.0, 24, 0.3)

        val result = infectionService.attemptInfection(human, virus)

        assertThat(result.outcome).isEqualTo(InfectionOutcome.SUCCESS)
        verify(humanRepository).save(any(Human::class.java))
    }

    @Test
    fun `should fail when transmission rate is 0 percent`() {
        val human = Human(UUID.randomUUID(), "David", isInfected = false, isImmune = false, infectionDate = null)
        val virus = Virus(UUID.randomUUID(), "Zombie-Safe", 0.0, 24, 0.0)

        val result = infectionService.attemptInfection(human, virus)

        assertThat(result.outcome).isEqualTo(InfectionOutcome.FAILURE)
        verifyNoInteractions(humanRepository)
    }
}

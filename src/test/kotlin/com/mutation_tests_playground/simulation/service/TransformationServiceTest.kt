package com.mutation_tests_playground.simulation.service

import com.mutation_tests_playground.human.model.Human
import com.mutation_tests_playground.human.repository.HumanRepository
import com.mutation_tests_playground.virus.model.Virus
import com.mutation_tests_playground.zombie.model.Zombie
import com.mutation_tests_playground.zombie.repository.ZombieRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import java.time.LocalDateTime
import java.util.UUID

class TransformationServiceTest {

    private val humanRepository = mock(HumanRepository::class.java)
    private val zombieRepository = mock(ZombieRepository::class.java)
    private val transformationService = TransformationService(humanRepository, zombieRepository)

    @Test
    fun `should return null when human is not infected`() {
        val human = Human(UUID.randomUUID(), "Alice", isInfected = false, isImmune = false, infectionDate = null)
        val virus = Virus(UUID.randomUUID(), "Zombie-X", 0.5, 24, 0.3)

        val result = transformationService.checkTransformation(human, virus)

        assertThat(result).isNull()
        verifyNoInteractions(humanRepository)
        verifyNoInteractions(zombieRepository)
    }

    @Test
    fun `should return null when incubation is not finished`() {
        val now = LocalDateTime.now()
        val human = Human(UUID.randomUUID(), "Bob", isInfected = true, isImmune = false, infectionDate = now.minusHours(10))
        val virus = Virus(UUID.randomUUID(), "Zombie-X", 0.5, 24, 0.3)

        val result = transformationService.checkTransformation(human, virus)

        assertThat(result).isNull()
        verifyNoInteractions(humanRepository)
        verifyNoInteractions(zombieRepository)
    }

    @Test
    fun `should return null and delete human when lethality kills the human`() {
        val now = LocalDateTime.now()
        // Incubation 24h, infecté depuis 25h
        val human = Human(UUID.randomUUID(), "Charlie", isInfected = true, isImmune = false, infectionDate = now.minusHours(25))
        // Létalité 100% -> meurt à coup sûr
        val virus = Virus(UUID.randomUUID(), "Deadly-Virus", 0.5, 24, 1.0)

        val result = transformationService.checkTransformation(human, virus)

        assertThat(result).isNull()
        verify(humanRepository).delete(human)
        verifyNoInteractions(zombieRepository)
    }

    @Test
    fun `should return zombie and delete human when transformation succeeds`() {
        val now = LocalDateTime.now()
        val human = Human(UUID.randomUUID(), "David", isInfected = true, isImmune = false, infectionDate = now.minusHours(25))
        // Létalité 0% -> transformation à coup sûr
        val virus = Virus(UUID.randomUUID(), "Zombie-Maker", 0.5, 24, 0.0)

        val result = transformationService.checkTransformation(human, virus)

        assertThat(result).isNotNull
        assertThat(result?.name).isEqualTo("Zombie David")
        verify(zombieRepository).save(any(Zombie::class.java))
        verify(humanRepository).delete(human)
    }

    @Test
    fun `should generate valid attributes`() {
        val virus = Virus(UUID.randomUUID(), "Zombie-X", 0.8, 24, 0.4)
        
        val (strength, speed) = transformationService.generateZombieAttributes(virus)
        
        assertThat(strength).isBetween(1, 10)
        assertThat(speed).isBetween(1, 10)
    }
}

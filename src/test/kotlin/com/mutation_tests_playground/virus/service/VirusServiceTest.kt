package com.mutation_tests_playground.virus.service

import com.mutation_tests_playground.virus.model.Virus
import com.mutation_tests_playground.virus.repository.VirusRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class VirusServiceTest {

    private val virusRepository = mock(VirusRepository::class.java)
    private val virusService = VirusService(virusRepository)

    @Test
    fun `should create a mutated virus from original`() {
        val originalVirus = Virus(UUID.randomUUID(), "Original", 0.5, 24, 0.5)
        `when`(virusRepository.save(any(Virus::class.java))).thenAnswer { it.arguments[0] }

        val mutatedVirus = virusService.createMutation(originalVirus)

        assertThat(mutatedVirus.id).isNotEqualTo(originalVirus.id)
        assertThat(mutatedVirus.name).contains("Original")
        assertThat(mutatedVirus.name).contains("Muté")
        assertThat(mutatedVirus.transmissionRate).isBetween(0.4, 0.6)
        assertThat(mutatedVirus.lethality).isBetween(0.45, 0.55)
        assertThat(mutatedVirus.incubationHours).isEqualTo(originalVirus.incubationHours)
        
        verify(virusRepository).save(any(Virus::class.java))
    }

    @Test
    fun `should respect bounds for transmissionRate and lethality`() {
        val originalVirus = Virus(UUID.randomUUID(), "Edge", 0.05, 24, 0.95)
        `when`(virusRepository.save(any(Virus::class.java))).thenAnswer { it.arguments[0] }

        // We run it multiple times to increase chance of hitting bounds if random allows
        repeat(10) {
            val mutatedVirus = virusService.createMutation(originalVirus)
            assertThat(mutatedVirus.transmissionRate).isBetween(0.0, 1.0)
            assertThat(mutatedVirus.lethality).isBetween(0.0, 1.0)
        }
    }
}

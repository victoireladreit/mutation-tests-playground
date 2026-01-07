package com.mutation_tests_playground.simulation.service

import com.mutation_tests_playground.human.model.Human
import com.mutation_tests_playground.human.repository.HumanRepository
import com.mutation_tests_playground.virus.model.Virus
import com.mutation_tests_playground.zombie.repository.ZombieRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.util.UUID

class PopulationServiceTest {

    private val humanRepository = mock(HumanRepository::class.java)
    private val zombieRepository = mock(ZombieRepository::class.java)
    private val transformationService = mock(TransformationService::class.java)
    private val populationService = PopulationService(humanRepository, zombieRepository, transformationService)

    @Test
    fun `should return correct population stats`() {
        `when`(humanRepository.countByIsInfectedFalseAndIsImmuneFalse()).thenReturn(10)
        `when`(humanRepository.countByIsInfectedTrue()).thenReturn(5)
        `when`(zombieRepository.count()).thenReturn(3L)
        `when`(humanRepository.countByIsImmuneTrue()).thenReturn(2)

        val stats = populationService.getPopulationStats()

        assertThat(stats.healthyCount).isEqualTo(10)
        assertThat(stats.infectedCount).isEqualTo(5)
        assertThat(stats.zombieCount).isEqualTo(3)
        assertThat(stats.immuneCount).isEqualTo(2)
    }

    @Test
    fun `should process evolution for all infected humans`() {
        val virus = Virus(UUID.randomUUID(), "TestVirus", 0.5, 24, 0.5)
        val human1 = Human(UUID.randomUUID(), "H1", true, false, null)
        val human2 = Human(UUID.randomUUID(), "H2", true, false, null)

        `when`(humanRepository.findAllByIsInfectedTrue()).thenReturn(listOf(human1, human2))

        populationService.processDailyEvolution(virus)

        verify(transformationService).checkTransformation(human1, virus)
        verify(transformationService).checkTransformation(human2, virus)
        verify(humanRepository).findAllByIsInfectedTrue()
    }
}
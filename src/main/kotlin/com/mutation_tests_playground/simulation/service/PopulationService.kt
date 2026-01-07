package com.mutation_tests_playground.simulation.service

import com.mutation_tests_playground.human.repository.HumanRepository
import com.mutation_tests_playground.simulation.model.Population
import com.mutation_tests_playground.virus.model.Virus
import com.mutation_tests_playground.zombie.repository.ZombieRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PopulationService(
    private val humanRepository: HumanRepository,
    private val zombieRepository: ZombieRepository,
    private val transformationService: TransformationService
) {

    fun getPopulationStats(): Population {
        return Population(
            healthyCount = humanRepository.countByIsInfectedFalseAndIsImmuneFalse(),
            infectedCount = humanRepository.countByIsInfectedTrue(),
            zombieCount = zombieRepository.count().toInt(),
            immuneCount = humanRepository.countByIsImmuneTrue()
        )
    }

    @Transactional
    fun processDailyEvolution(virus: Virus) {
        val infectedHumans = humanRepository.findAllByIsInfectedTrue()
        infectedHumans.forEach { human ->
            transformationService.checkTransformation(human, virus)
        }
    }
}
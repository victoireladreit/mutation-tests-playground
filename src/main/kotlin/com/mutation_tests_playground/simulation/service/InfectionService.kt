package com.mutation_tests_playground.simulation.service

import com.mutation_tests_playground.human.model.Human
import com.mutation_tests_playground.human.repository.HumanRepository
import com.mutation_tests_playground.simulation.model.InfectionOutcome
import com.mutation_tests_playground.simulation.model.InfectionResult
import com.mutation_tests_playground.virus.model.Virus
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.random.Random

@Service
class InfectionService(
    private val humanRepository: HumanRepository
) {

    fun attemptInfection(human: Human, virus: Virus): InfectionResult {
        if (human.isImmune) {
            return InfectionResult(
                outcome = InfectionOutcome.IMMUNE,
                probability = 0.0,
                message = "L'humain ${human.name} est immunisé."
            )
        }

        if (human.isInfected) {
            return InfectionResult(
                outcome = InfectionOutcome.SUCCESS,
                probability = 1.0,
                message = "L'humain ${human.name} est déjà infecté."
            )
        }

        val randomValue = Random.Default.nextDouble(0.0, 1.0)
        val isSuccessful = randomValue <= virus.transmissionRate

        return if (isSuccessful) {
            val infectedHuman = human.copy(
                isInfected = true,
                infectionDate = LocalDateTime.now()
            )
            humanRepository.save(infectedHuman)
            InfectionResult(
                outcome = InfectionOutcome.SUCCESS,
                probability = virus.transmissionRate,
                message = "Infection réussie pour ${human.name}."
            )
        } else {
            InfectionResult(
                outcome = InfectionOutcome.FAILURE,
                probability = virus.transmissionRate,
                message = "L'infection a échoué pour ${human.name}."
            )
        }
    }
}
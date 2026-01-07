package com.mutation_tests_playground.simulation.service

import com.mutation_tests_playground.human.model.Human
import com.mutation_tests_playground.human.repository.HumanRepository
import com.mutation_tests_playground.virus.model.Virus
import com.mutation_tests_playground.zombie.model.Zombie
import com.mutation_tests_playground.zombie.repository.ZombieRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random

@Service
class TransformationService(
    private val humanRepository: HumanRepository,
    private val zombieRepository: ZombieRepository
) {

    /**
     * Vérifie si un humain infecté doit se transformer en zombie.
     * @return Le zombie créé si transformation, null sinon (incubation en cours ou décès).
     */
    fun checkTransformation(human: Human, virus: Virus): Zombie? {
        if (!human.isInfected || human.infectionDate == null) {
            return null
        }

        val now = LocalDateTime.now()
        val incubationLimit = human.infectionDate.plusHours(virus.incubationHours.toLong())

        if (now.isBefore(incubationLimit)) {
            return null
        }

        // L'incubation est terminée, on applique la létalité
        val randomValue = Random.Default.nextDouble(0.0, 1.0)

        // Si randomValue <= lethality, l'humain meurt (pas de zombie)
        if (randomValue <= virus.lethality) {
            humanRepository.delete(human)
            return null
        }

        // Sinon, transformation en zombie
        val (strength, speed) = generateZombieAttributes(virus)
        val zombie = Zombie(
            id = UUID.randomUUID(),
            name = "Zombie ${human.name}",
            strength = strength,
            speed = speed,
            transformationDate = LocalDateTime.now()
        )

        zombieRepository.save(zombie)
        humanRepository.delete(human)

        return zombie
    }

    /**
     * Génère la force et la vitesse du zombie (entre 1 et 10).
     */
    fun generateZombieAttributes(virus: Virus): Pair<Int, Int> {
        // Logique simplifiée : plus le virus est létal, plus le zombie est fort
        // On utilise Random pour varier un peu
        val baseStrength = (virus.lethality * 10).toInt().coerceIn(1, 10)
        val strength = (baseStrength + Random.Default.nextInt(-2, 3)).coerceIn(1, 10)

        val baseSpeed = (virus.transmissionRate * 10).toInt().coerceIn(1, 10)
        val speed = (baseSpeed + Random.Default.nextInt(-2, 3)).coerceIn(1, 10)

        return Pair(strength, speed)
    }
}
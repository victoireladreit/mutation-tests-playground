package com.mutation_tests_playground.virus.service

import com.mutation_tests_playground.virus.model.Virus
import com.mutation_tests_playground.virus.repository.VirusRepository
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.random.Random

@Service
class VirusService(
    private val virusRepository: VirusRepository
) {

    /**
     * Crée une mutation d'un virus existant en appliquant un facteur aléatoire :
     * - Taux de transmission : variation de +/- 0.1 maximum
     * - Létalité : variation de +/- 0.05 maximum
     *
     * Les valeurs globales restent contraintes entre 0.0 et 1.0.
     *
     * @param originalVirus Le virus d'origine.
     * @return Le nouveau virus muté et sauvegardé.
     */
    fun createMutation(originalVirus: Virus): Virus {
        val mutationFactor = Random.nextDouble(-0.1, 0.1)

        val mutatedName = "${originalVirus.name} Muté"
        val mutatedTransmissionRate = (originalVirus.transmissionRate + mutationFactor).coerceIn(0.0, 1.0)
        val mutatedLethality = (originalVirus.lethality + (mutationFactor / 2)).coerceIn(0.0, 1.0)

        val mutatedVirus = originalVirus.copy(
            id = UUID.randomUUID(),
            name = mutatedName,
            transmissionRate = mutatedTransmissionRate,
            lethality = mutatedLethality
        )
        
        return virusRepository.save(mutatedVirus)
    }
}

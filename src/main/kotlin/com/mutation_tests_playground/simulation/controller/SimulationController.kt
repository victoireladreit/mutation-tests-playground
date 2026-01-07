package com.mutation_tests_playground.simulation.controller

import com.mutation_tests_playground.simulation.model.InfectionResult
import com.mutation_tests_playground.simulation.service.InfectionService
import com.mutation_tests_playground.simulation.model.Population
import com.mutation_tests_playground.simulation.service.PopulationService
import com.mutation_tests_playground.human.repository.HumanRepository
import com.mutation_tests_playground.virus.repository.VirusRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/simulation")
class SimulationController(
    private val populationService: PopulationService,
    private val infectionService: InfectionService,
    private val humanRepository: HumanRepository,
    private val virusRepository: VirusRepository
) {

    @GetMapping("/stats")
    fun getStats(): Population {
        return populationService.getPopulationStats()
    }

    @PostMapping("/evolve/{virusId}")
    fun evolve(@PathVariable virusId: UUID): ResponseEntity<String> {
        val virus = virusRepository.findById(virusId).orElse(null)
            ?: return ResponseEntity.status(404).body("Virus non trouvé")
        
        populationService.processDailyEvolution(virus)
        return ResponseEntity.ok("Évolution quotidienne traitée avec le virus ${virus.name}")
    }

    @PostMapping("/infect")
    fun infect(
        @RequestParam humanId: UUID,
        @RequestParam virusId: UUID
    ): ResponseEntity<InfectionResult> {
        val human = humanRepository.findById(humanId).orElse(null)
            ?: return ResponseEntity.notFound().build()
        val virus = virusRepository.findById(virusId).orElse(null)
            ?: return ResponseEntity.notFound().build()

        val result = infectionService.attemptInfection(human, virus)
        return ResponseEntity.ok(result)
    }
}

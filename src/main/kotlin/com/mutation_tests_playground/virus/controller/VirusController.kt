package com.mutation_tests_playground.virus.controller

import com.mutation_tests_playground.virus.model.Virus
import com.mutation_tests_playground.virus.repository.VirusRepository
import com.mutation_tests_playground.virus.service.VirusService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/viruses")
class VirusController(
    private val virusRepository: VirusRepository,
    private val virusService: VirusService
) {

    @GetMapping
    fun getAll(): List<Virus> = virusRepository.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<Virus> {
        return virusRepository.findById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun create(@RequestBody virus: Virus): Virus {
        return virusRepository.save(virus)
    }

    @PostMapping("/{id}/mutate")
    fun mutate(@PathVariable id: UUID): ResponseEntity<Virus> {
        val original = virusRepository.findById(id).orElse(null)
            ?: return ResponseEntity.notFound().build()
        
        val mutated = virusService.createMutation(original)
        return ResponseEntity.ok(virusRepository.save(mutated))
    }
}

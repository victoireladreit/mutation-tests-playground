package com.mutation_tests_playground.human.controller

import com.mutation_tests_playground.human.model.Human
import com.mutation_tests_playground.human.repository.HumanRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/humans")
class HumanController(
    private val humanRepository: HumanRepository
) {

    @GetMapping
    fun getAll(): List<Human> = humanRepository.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<Human> {
        return humanRepository.findById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun create(@RequestBody human: Human): Human {
        return humanRepository.save(human)
    }
}

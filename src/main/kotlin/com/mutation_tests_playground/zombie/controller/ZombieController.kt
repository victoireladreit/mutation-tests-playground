package com.mutation_tests_playground.zombie.controller

import com.mutation_tests_playground.zombie.model.Zombie
import com.mutation_tests_playground.zombie.repository.ZombieRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/zombies")
class ZombieController(private val zombieRepository: ZombieRepository) {

    @GetMapping
    fun getAll(): List<Zombie> = zombieRepository.findAll()
}

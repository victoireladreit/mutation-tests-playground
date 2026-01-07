package com.mutation_tests_playground.zombie.repository

import com.mutation_tests_playground.zombie.model.Zombie
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ZombieRepository : JpaRepository<Zombie, UUID>

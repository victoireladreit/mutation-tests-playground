package com.mutation_tests_playground.virus.repository

import com.mutation_tests_playground.virus.model.Virus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface VirusRepository : JpaRepository<Virus, UUID>

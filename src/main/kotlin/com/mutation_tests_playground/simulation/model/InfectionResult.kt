package com.mutation_tests_playground.simulation.model

data class InfectionResult(
    val outcome: InfectionOutcome,
    val probability: Double,
    val message: String
)
package com.mutation_tests_playground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MutationTestsPlaygroundApplication

fun main(args: Array<String>) {
	runApplication<MutationTestsPlaygroundApplication>(*args)
}

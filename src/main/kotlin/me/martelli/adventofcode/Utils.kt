package me.martelli.adventofcode

import java.io.File

fun resource(name: String): File = File(ClassLoader.getSystemResource(name).file)

fun <T> test(fileName: String, phase1: (File) -> T, result1: T? = null, phase2: ((File) -> T)? = null, result2: T? = null) {
    val input = resource(fileName)

    val phase1Result = phase1(input)
    println("Phase 1 result: [$phase1Result]")

    if (result1 != null && result1 != phase1Result) {
        error("Phase 1 result not matching: expected [$result1]")
    }

    if (phase2 != null) {
        val phase2Result = phase2(input)
        println("Phase 2 result: [$phase2Result]")

        if (result2 != null && result2 != phase2Result) {
            error("Phase 2 result not matching: expected [$result2]")
        }
    }

    println("All good!")
}

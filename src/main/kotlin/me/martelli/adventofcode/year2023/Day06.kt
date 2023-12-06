package me.martelli.adventofcode.year2023

import java.io.File

fun main() = test("06", ::phase1, 2449062, ::phase2, 33149631)

private fun phase1(input: File): Int {
    val (times, distances) = input.readLines()
        .map { line ->
            line.split(":")[1].trim()
                .split("\\s+".toRegex())
                .map { it.toLong() }
        }

    return times.zip(distances).map { (time, distance) ->
        countPossibleSolutions(time, distance)
    }.reduce { a, b -> a * b }
}

private fun phase2(input: File): Int {
    val (time, distance) = input.readLines()
        .map { line ->
            line.split(":")[1]
                .replace(" ", "")
                .toLong()
        }

    return countPossibleSolutions(time, distance)
}

fun countPossibleSolutions(time: Long, distance: Long) = (0..time)
    .map { speed -> (time - speed) * speed }
    .count { it > distance }

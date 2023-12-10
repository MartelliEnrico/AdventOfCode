package me.martelli.adventofcode.year2023

import java.io.File

fun main() = test("09", ::phase1, 1901217887, ::phase2, 905)

private fun phase1(input: File): Int {
    return input.readLines()
        .map { it.split(" ").map(String::toInt) }
        .sumOf { predictNextValue(it) }
}

fun predictNextValue(history: List<Int>): Int {
    var nextValue = history.last()
    var list = history
    while (list.any { it != 0 }) {
        list = list.windowed(2)
            .map { (a, b) -> b - a }
            .also {
                nextValue += it.last()
            }
    }
    return nextValue
}

private fun phase2(input: File): Int {
    return input.readLines()
        .map { it.split(" ").map(String::toInt).reversed() }
        .sumOf { predictNextValue(it) }
}

package me.martelli.adventofcode.year2024

import me.martelli.adventofcode.test

fun main() = test(2024, 2, ::part1, ::part2)

private fun part1(input: String) = input.lines()
    .map { it.split(" ").map(String::toInt) }
    .count { checkLevels(it) == -1 }

private fun part2(input: String) = input.lines()
    .map { it.split(" ").map(String::toInt) }
    .count {
        val error = checkLevels(it)
        if (error != -1) {
            return@count listOf(error - 1, error, error + 1)
                .filter { index -> index >= 0 }
                .map { index -> it.toMutableList().apply { removeAt(index) } }
                .any { list -> checkLevels(list) == -1 }
        }
        true
    }

private fun checkLevels(levels: List<Int>): Int {
    val ascending = levels[0] < levels[1]
    return levels.windowed(2) { (a, b) -> if (ascending) b - a else a - b }
        .indexOfFirst { diff -> diff !in 1..3 }
}

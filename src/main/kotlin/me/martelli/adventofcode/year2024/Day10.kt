package me.martelli.adventofcode.year2024

import me.martelli.adventofcode.test

fun main() = test(2024, 10, ::part1, ::part2)

private fun part1(input: String): Int {
    val grid = getGrid(input)
    return grid.filterValues { it == 0 }
        .keys
        .sumOf { navigateTrail(grid, it).toSet().size }
}

private fun part2(input: String): Int {
    val grid = getGrid(input)
    return grid.filterValues { it == 0 }
        .keys
        .sumOf { navigateTrail(grid, it).size }
}

private fun getGrid(input: String): Map<Pair<Int, Int>, Int> = input.lines()
    .flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, char ->
            (x to y) to (char - '0')
        }
    }
    .associate { it }

private fun navigateTrail(grid: Map<Pair<Int, Int>, Int>, position: Pair<Int, Int>): List<Pair<Int, Int>> {
    val current = grid[position]!!
    if (current == 9) return listOf(position)

    val (x, y) = position
    return listOf(x+1 to y, x to y+1, x-1 to y, x to y-1)
        .filter { grid[it] == current + 1 }
        .flatMap { navigateTrail(grid, it) }
}

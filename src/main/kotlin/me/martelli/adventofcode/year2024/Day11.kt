package me.martelli.adventofcode.year2024

import me.martelli.adventofcode.test

fun main() = test(2024, 11, ::part1, ::part2)

private fun part1(input: String) = cycle(input, 25)

private fun part2(input: String) = cycle(input, 75)

private fun cycle(input: String, steps: Int): Long {
    var stones = input.split(" ")
        .map { it.toLong() }
        .associateWith { 1L }

    for (i in 1..steps) {
        stones = blink(stones)
    }

    return stones.values.sum()
}

private fun blink(stones: Map<Long, Long>): Map<Long, Long> {
    val newStones = mutableMapOf<Long, Long>().withDefault { 0L }

    for ((stone, count) in stones.entries) {
        if (stone == 0L) {
            newStones.merge(1L, count, Long::plus)
        } else {
            val s = stone.toString()
            if (s.length % 2 == 0) {
                val a = s.substring(0, s.length / 2).toLong()
                val b = s.substring(s.length / 2).toLong()

                newStones.merge(a, count, Long::plus)
                newStones.merge(b, count, Long::plus)
            } else {
                newStones.merge(stone * 2024L, count, Long::plus)
            }
        }
    }

    return newStones
}

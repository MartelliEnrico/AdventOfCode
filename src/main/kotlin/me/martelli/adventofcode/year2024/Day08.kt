package me.martelli.adventofcode.year2024

import me.martelli.adventofcode.test

fun main() = test(2024, 8, ::part1, ::part2)

private fun part1(input: String) = countAntiNodes(input, true)

private fun part2(input: String) = countAntiNodes(input, false)

private fun countAntiNodes(input: String, single: Boolean): Int {
    val (width, height) = input.lines().let { it[0].length to it.size }
    return input.lines()
        .flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, char ->
                if (char != '.') Antenna(x, y, char) else null
            }
        }
        .groupBy { it.frequency }
        .values
        .flatMap {
            buildList {
                for (i in it.indices) {
                    for (j in i + 1..<it.size) {
                        addAll(findAntiNodes(it[i], it[j], width, height, single))
                    }
                }
            }
        }
        .toSet()
        .size
}

private data class Antenna(val x: Int, val y: Int, val frequency: Char)

private fun findAntiNodes(a: Antenna, b: Antenna, width: Int, height: Int, single: Boolean): List<Pair<Int, Int>> {
    val dx = a.x - b.x
    val dy = a.y - b.y

    return buildList {
        var (ax, ay) = a
        var (bx, by) = b

        if (!single) {
            add(ax to ay)
            add(bx to by)
        }

        do {
            ax += dx
            ay += dy
            if (ax in 0..<width && ay in 0..<height) add(ax to ay) else break
        } while (!single)

        do {
            bx -= dx
            by -= dy
            if (bx in 0..<width && by in 0..<height) add(bx to by) else break
        } while (!single)
    }
}

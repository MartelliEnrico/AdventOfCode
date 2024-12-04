package me.martelli.adventofcode.year2024

import me.martelli.adventofcode.test

fun main() = test(2024, 4, ::part1, ::part2)

private fun part1(input: String): Int {
    val matrix = input.lines()
    var count = 0

    for (y in matrix.indices) {
        val line = matrix[y]
        for (x in line.indices) {
            if (line[x] != 'X') continue

            for (dy in -1..1) {
                for (dx in -1..1) {
                    var search = 1
                    var newY = y + dy
                    var newX = x + dx

                    while (search <= 4) {
                        if (newY < 0 || newY >= matrix.size || newX < 0 || newX >= line.length) break
                        if (matrix[newY][newX] != Search[search]) break
                        if (search == 3) {
                            count++
                            break
                        } else {
                            search++
                            newY += dy
                            newX += dx
                        }
                    }
                }
            }
        }
    }

    return count
}

private const val Search = "XMAS"

private fun part2(input: String): Int {
    val matrix = input.lines()
    var count = 0

    for (y in 1..<matrix.size-1) {
        val line = matrix[y]
        for (x in 1..<line.length-1) {
            val char = line[x]
            if (char != 'A') continue

            for (r in 0..<4) {
                val a = matrix[y - 1][x - 1]
                val b = matrix[y - 1][x + 1]
                val c = matrix[y + 1][x + 1]
                val d = matrix[y + 1][x - 1]

                if (a == Corners[r] && b == Corners[(r + 1) % 4] && c == Corners[(r + 2) % 4] && d == Corners[(r + 3) % 4]) {
                    count++
                    break
                }
            }
        }
    }

    return count
}

private const val Corners = "MMSS"

package me.martelli.adventofcode.year2023

import java.io.File
import kotlin.math.abs

fun main() = test("11", ::phase1, 9693756, ::phase2, 717878258016)

private fun phase1(input: File) =
    input.readLines()
        .expand(2)
        .sumOfDistances()

private fun phase2(input: File) =
    input.readLines()
        .expand(1000000)
        .sumOfDistances()

private fun List<String>.expand(expansion: Int): Universe {
    val emptyLines = mapIndexed { x, line ->
        if (line.indexOf('#') == -1) x else null
    }.filterNotNull()

    val emptyColumns = mutableListOf<Int>()
    for (y in 0..<this[0].length) {
        if (all { line -> line[y] == '.' }) {
            emptyColumns.add(y)
        }
    }

    val galaxies = flatMapIndexed { x, line ->
        line.mapIndexed { y, tile ->
            if (tile == '#') x to y else null
        }
    }.filterNotNull()

    return Universe(expansion - 1, emptyLines, emptyColumns, galaxies)
}

private data class Universe(
    val expansion: Int,
    val emptyLines: List<Int>,
    val emptyColumns: List<Int>,
    val galaxies: List<Point>
) {
    fun sumOfDistances() =
        galaxies.pairs()
            .sumOf { (a, b) ->
                var distance = a.distanceTo(b).toLong()
                distance += emptyLines.count { it in absRange(a.first, b.first) } * expansion
                distance += emptyColumns.count { it in absRange(a.second, b.second) } * expansion
                distance
            }
}

private typealias Point = Pair<Int, Int>

private fun <T> List<T>.pairs() = sequence {
    forEachIndexed { i, t ->
        for (j in i+1..<size) {
            yield(listOf(t, get(j)))
        }
    }
}

private fun Point.distanceTo(other: Point) = abs(first - other.first) + abs(second - other.second)

private fun absRange(a: Int, b: Int) = if (a < b) a..b else b..a

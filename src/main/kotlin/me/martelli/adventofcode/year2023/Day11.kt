package me.martelli.adventofcode.year2023

import java.io.File
import kotlin.math.abs

fun main() = test("11", ::phase1, 9693756)

private fun phase1(input: File): Int {
    val universe = input.readLines()
        .map { line -> line.map { it.toSpaceTile() } }
        .expand()
    val galaxies = findAllGalaxies(universe)
    return galaxies.combinations()
        .sumOf { (a, b) -> a.distanceTo(b) }
}

fun Char.toSpaceTile(): SpaceTile = when (this) {
    '.' -> SpaceTile.Space
    '#' -> SpaceTile.Galaxy
    else -> error("unreachable")
}

enum class SpaceTile {
    Space, Galaxy
}

fun List<List<SpaceTile>>.expand(): List<List<SpaceTile>> {
    val emptyColumns = mutableListOf<Int>()
    for (y in 0..<this[0].size) {
        if (all { line -> line[y] == SpaceTile.Space }) {
            emptyColumns.add(y)
        }
    }

    return flatMap {
        val empty = it.all { tile -> tile == SpaceTile.Space }
        val line = it.flatMapIndexed { y, tile ->
            if (y in emptyColumns) listOf(tile, tile) else listOf(tile)
        }
        if (empty) listOf(line, line) else listOf(line)
    }
}

typealias Point = Pair<Int, Int>

fun findAllGalaxies(universe: List<List<SpaceTile>>): List<Point> {
    return universe.flatMapIndexed { x, line ->
        line.mapIndexed { y, tile ->
            if (tile == SpaceTile.Galaxy) x to y else null
        }
    }.filterNotNull()
}

fun <T> List<T>.combinations() = sequence {
    forEachIndexed { i, t ->
        for (j in i+1..<size) {
            yield(listOf(t, get(j)))
        }
    }
}

fun Point.distanceTo(other: Point) = abs(first - other.first) + abs(second - other.second)

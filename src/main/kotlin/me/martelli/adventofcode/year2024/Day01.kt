package me.martelli.adventofcode.year2024

import java.io.File
import kotlin.math.abs

fun main() = test("01", ::phase1, 1189304, ::phase2, 24349736)

private fun phase1(input: File) = input.readLines()
    .map { it.split("\\s+".toPattern()).map(String::toInt) }
    .transpose()
    .map { it.sorted() }
    .transpose()
    .sumOf { abs(it[0] - it[1]) }

private fun phase2(input: File) = input.readLines()
    .map { it.split("\\s+".toPattern()).map(String::toInt) }
    .transpose()
    .let { (a, b) ->
        a.sumOf { aval -> aval * b.count { bval -> aval == bval } }
    }

fun <T> List<List<T>>.transpose(): List<List<T>> {
    return (this[0].indices).map { i -> (this.indices).map { j -> this[j][i] } }
}

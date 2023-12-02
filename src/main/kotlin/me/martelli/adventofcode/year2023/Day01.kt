package me.martelli.adventofcode.year2023

import java.io.File

fun main() = test("01", ::phase1, 54239, ::phase2, 55343)

private fun phase1(input: File) = input.readLines()
    .sumOf {
        val list = it.mapNotNull { if (it.isDigit()) it.digitToInt() else null  }
        list.first() * 10 + list.last()
    }

private fun phase2(input: File) = input.readLines()
    .sumOf {
        val matches = Number.findAll(it)
        val first = matches.first().groupValues[1]
        val last = matches.last().groupValues[1]
        first.toNumber() * 10 + last.toNumber()
    }

val Numbers = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)
val Number = "(?=([1-9]|${Numbers.keys.joinToString(separator = "|")})).".toRegex()

fun String.toNumber(): Int = when {
    this[0].isDigit() -> this[0].digitToInt()
    else -> Numbers[this]!!
}

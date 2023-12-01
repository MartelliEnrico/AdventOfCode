package me.martelli.adventofcode.year2023

import java.io.File

fun main() = test("01", ::phase1, 54239, ::phase2, 55343)

fun phase1(input: File) = input.readLines()
    .sumOf {
        val list = it.mapNotNull { if (it.isDigit()) it.digitToInt() else null  }
        list.first() * 10 + list.last()
    }

val Number = "([1-9]|one|two|three|four|five|six|seven|eight|nine)"
val Line = "($Number.*$Number|$Number)".toRegex()

fun phase2(input: File) = input.readLines()
    .sumOf {
        val groups = Line.find(it)!!.groupValues
        if (groups[4].isEmpty()) {
            (groups[2].toNumber() * 10) + groups[3].toNumber()
        } else {
            groups[4].toNumber() * 11
        }
    }

fun String.toNumber(): Int = when(this) {
    "one" -> 1
    "two" -> 2
    "three" -> 3
    "four" -> 4
    "five" -> 5
    "six" -> 6
    "seven" -> 7
    "eight" -> 8
    "nine" -> 9
    else -> toInt()
}

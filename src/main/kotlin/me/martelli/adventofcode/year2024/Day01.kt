package me.martelli.adventofcode.year2024

import java.io.File
import kotlin.math.abs

fun main() = test("01", ::phase1, 1189304, ::phase2, 24349736)

private fun phase1(input: File) = input.readLines()
    .map { it.split(Whitespace).map(String::toInt) }
    .let { items ->
        val a = MutableList(items.size) { items[it][0] }.sorted()
        val b = MutableList(items.size) { items[it][1] }.sorted()
        a.zip(b) { av, bv -> abs(av - bv) }.sum()
    }

private fun phase2(input: File) = input.readLines()
    .map { it.split(Whitespace).map(String::toInt) }
    .let { items ->
        val a = MutableList(items.size) { items[it][0] }
        val b = MutableList(items.size) { items[it][1] }
        a.sumOf { av -> av * b.count { bv -> av == bv } }
    }

val Whitespace = "\\s+".toPattern()

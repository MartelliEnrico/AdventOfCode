package me.martelli.adventofcode.year2024

import me.martelli.adventofcode.test
import kotlin.math.abs

fun main() = test(2024, 1, ::part1, ::part2)

private fun part1(input: String) = input.lines()
    .map { it.split(Whitespace).map(String::toInt) }
    .let { items ->
        val a = MutableList(items.size) { items[it][0] }.sorted()
        val b = MutableList(items.size) { items[it][1] }.sorted()
        a.zip(b) { av, bv -> abs(av - bv) }.sum()
    }

private fun part2(input: String) = input.lines()
    .map { it.split(Whitespace).map(String::toInt) }
    .let { items ->
        val a = MutableList(items.size) { items[it][0] }
        val b = MutableList(items.size) { items[it][1] }
        a.sumOf { av -> av * b.count { bv -> av == bv } }
    }

private val Whitespace = Regex("\\s+")

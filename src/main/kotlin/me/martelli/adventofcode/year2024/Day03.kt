package me.martelli.adventofcode.year2024

import me.martelli.adventofcode.test

fun main() = test(2024, 3, ::part1, ::part2)

private fun part1(input: String) = Mul.findAll(input)
    .sumOf {
        val (x, y) = it.destructured
        x.toInt() * y.toInt()
    }

private fun part2(input: String) = input.split("do()")
    .map { it.substringBefore("don't()") }
    .flatMap { Mul.findAll(it) }
    .sumOf {
        val (x, y) = it.destructured
        x.toInt() * y.toInt()
    }

private val Mul = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")

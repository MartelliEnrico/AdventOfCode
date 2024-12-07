package me.martelli.adventofcode.year2024

import me.martelli.adventofcode.test

fun main() = test(2024, 7, ::part1, ::part2)

private fun part1(input: String) = input.lines()
    .map { it.toEquation() }
    .sumOf { if (it.solvable(Op1)) it.result else 0 }

private fun part2(input: String) = input.lines()
    .map { it.toEquation() }
    .sumOf { if (it.solvable(Op2)) it.result else 0 }

private class Equation(val result: Long, val operators: List<Long>)

private fun String.toEquation() = split(": ").let { (result, ops) ->
    val operators = ops.split(" ").map(String::toLong)
    Equation(result.toLong(), operators)
}

private fun Equation.solvable(ops: List<Op>) = isSolvable(ops, result, operators.subList(1, operators.size), operators[0])
private fun isSolvable(ops: List<Op>, result: Long, operators: List<Long>, current: Long): Boolean {
    if (operators.isEmpty()) return result == current
    val others = operators.subList(1, operators.size)
    return ops.any { isSolvable(ops, result, others, it(current, operators[0])) }
}

private typealias Op = (Long, Long) -> Long
private val Op1: List<Op> = listOf({ a, b -> a + b }, { a, b -> a * b })
private val Op2: List<Op> = Op1 + { a, b -> "$a$b".toLong() }

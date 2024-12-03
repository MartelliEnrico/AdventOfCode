package me.martelli.adventofcode.year2023

import java.io.File
import kotlin.math.max

fun main() = test("04", ::phase1, 23441, ::phase2, 5923918)

private fun phase1(input: File) =
    input.readLines()
    .map { it.toCard() }
    .sumOf { it.calculatePoints() }

private fun phase2(input: File): Int {
    val cards = input.readLines().map { it.toCard() }
    val additionalCards = mutableMapOf<Int, Int>()
    var count = cards.size

    cards.forEachIndexed { i, card ->
        val iterations = additionalCards[i] ?: 0
        count += iterations
        val matches = card.calculateMatches()
        for (j in 1..matches) {
            additionalCards[i + j] = (additionalCards[i + j] ?: 0) + 1 + iterations
        }
    }

    return count
}

private data class Card(val winningNumbers: List<Int>, val numbers: List<Int>) {
    fun calculateMatches() = numbers.count { winningNumbers.contains(it) }

    fun calculatePoints(): Int {
        val count = calculateMatches()
        return max(0, 1 shl (count - 1))
    }
}

private fun String.toCard(): Card {
    val sets = split(": ")[1].split(" | ")
    val (winningNumbers, numbers) = sets.map { it.trim().split("\\s+".toRegex()).map { n -> n.toInt() } }
    return Card(winningNumbers, numbers)
}

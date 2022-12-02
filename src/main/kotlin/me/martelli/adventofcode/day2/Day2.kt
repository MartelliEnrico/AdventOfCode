package me.martelli.adventofcode.day2

import java.io.File

fun main() {
    val input = File(ClassLoader.getSystemResource("day2.txt").file)
    val lines = input.readLines()
    val rounds = lines.map { Move.fromChar(it[0]) to it[2] }
    val total = rounds.sumOf { (their, mine) -> Move.fromChar(mine).totalScore(their) }
    val secretTotal = rounds.sumOf { (their, mine) -> Move.fromSecretPlan(their, mine).totalScore(their) }
    println("1) Total Score: $total")
    println("2) Secret Score: $secretTotal")
}

enum class Move {
    Rock, Paper, Scissor;

    private fun typeScore() = when (this) {
        Rock -> 1
        Paper -> 2
        Scissor -> 3
    }

    private fun outcomeScore(other: Move) = when (other) {
        Rock -> when (this) {
            Rock -> 3
            Paper -> 6
            Scissor -> 0
        }
        Paper -> when (this) {
            Rock -> 0
            Paper -> 3
            Scissor -> 6
        }
        Scissor -> when (this) {
            Rock -> 6
            Paper -> 0
            Scissor -> 3
        }
    }

    fun totalScore(other: Move) = typeScore() + outcomeScore(other)

    companion object {
        fun fromChar(char: Char): Move = when (char) {
            'A', 'X' -> Rock
            'B', 'Y' -> Paper
            'C', 'Z' -> Scissor
            else -> error("unreachable")
        }

        fun fromSecretPlan(other: Move, char: Char): Move = when (char) {
            'X' -> when (other) {
                Rock -> Scissor
                Paper -> Rock
                Scissor -> Paper
            }
            'Y' -> other
            'Z' -> when (other) {
                Rock -> Paper
                Paper -> Scissor
                Scissor -> Rock
            }
            else -> error("unreachable")
        }
    }
}

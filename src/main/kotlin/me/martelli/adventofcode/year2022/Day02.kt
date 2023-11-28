package me.martelli.adventofcode.year2022

fun main() {
    val input = resource("day02.txt")
    val lines = input.readLines()
    val rounds = lines.map { Move.fromChar(it[0]) to it[2] }
    val total = rounds.sumOf { (their, mine) -> Move.fromChar(mine).totalScore(their) }
    val secretTotal = rounds.sumOf { (their, mine) -> Move.fromSecretPlan(their, mine).totalScore(their) }
    println("1) Total Score: $total")
    println("2) Secret Score: $secretTotal")
}

private enum class Move {
    Rock, Paper, Scissor;

    private fun typeScore() = when (this) {
        Rock -> 1
        Paper -> 2
        Scissor -> 3
    }

    private fun outcomeScore(other: Move) = when (this to other) {
        Rock to Paper, Paper to Scissor, Scissor to Rock -> 0
        Rock to Rock, Paper to Paper, Scissor to Scissor -> 3
        else -> 6
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

package me.martelli.adventofcode.year2023

import java.io.File

fun main() = test("02", ::phase1, 2164, ::phase2, 69929)

private fun phase1(input: File) = input.readLines()
    .map { it.toGame() }
    .filter {
        it.red <= 12 && it.green <= 13 && it.blue <= 14
    }
    .sumOf {
        it.id
    }

private fun phase2(input: File) = input.readLines()
    .map { it.toGame() }
    .sumOf {
        it.red * it.green * it.blue
    }

data class Game(
    val id: Int,
    val red: Int,
    val green: Int,
    val blue: Int
)

val GameInfo = "Game (\\d+): (.*)".toRegex()

fun String.toGame(): Game {
    val match = GameInfo.find(this)!!
    val (id, extractions) = match.destructured
    val colors = extractions.split("; ")
        .map { extraction ->
            extraction.split(", ")
                .associate { group ->
                    val (number, color) = group.split(" ")
                    color to number.toInt()
                }
        }
        .flatMap { it.entries }
        .groupBy { it.key }
        .mapValues { it.value.maxOf { v -> v.value } }
    return Game(id.toInt(), colors["red"] ?: 0, colors["green"] ?: 0, colors["blue"] ?: 0)
}

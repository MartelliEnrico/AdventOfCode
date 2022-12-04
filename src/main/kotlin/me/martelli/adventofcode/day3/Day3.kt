package me.martelli.adventofcode.day3

import java.io.File

fun main() {
    val input = File(ClassLoader.getSystemResource("day3.txt").file)
    val lines = input.readLines()
    val rucksacks = lines.map { Rucksack(it) }
    val totalPriorities = rucksacks.flatMap { it.getCommonItems() }.sumOf { it.priority }
    val totalBadges = rucksacks.chunked(3) { it.getGroupBadge() }.sumOf { it.priority }
    println("1) Total Priorities: $totalPriorities")
    println("2) Total Badges: $totalBadges")
}

data class Rucksack(val items: String) {
    private val first: String
        get() = items.substring(0, items.length/2)

    private val second: String
        get() = items.substring(items.length/2)

    fun getCommonItems(): Set<Char> = first.toCharArray().intersect(second.toCharArray().toSet())
}

fun List<Rucksack>.getGroupBadge(): Char = this.fold(this[0].items.toCharArray().toSet()) {
        acc, it -> it.items.toCharArray().intersect(acc)
}.first()

val Char.priority: Int
    get() = when(this) {
        in 'a'..'z' -> 1 + (this - 'a')
        in 'A'..'Z' -> 27 + (this - 'A')
        else -> 0
    }

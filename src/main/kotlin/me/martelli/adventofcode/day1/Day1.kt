package me.martelli.adventofcode.day1

import java.io.File

fun main() {
    val input = File(ClassLoader.getSystemResource("day1.txt").file)
    val lines = input.readLines()
    val elves = mutableListOf<Int>()
    var currentCalories = 0
    lines.forEach {
        if (it.isEmpty()) {
            elves.add(currentCalories)
            currentCalories = 0
        } else {
            currentCalories += it.toInt()
        }
    }
    val top3 = elves.sortedDescending().take(3)
    val maxCalories = top3[0]
    println("1) Max Calories: $maxCalories")
    val top3Total = top3.sum()
    println("2) Top 3 Calories: $top3Total")
}

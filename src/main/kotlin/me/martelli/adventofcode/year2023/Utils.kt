package me.martelli.adventofcode.year2023

import me.martelli.adventofcode.test as baseTest

import java.io.File

fun <T> test(day: String, phase1: (File) -> T, result1: T? = null, phase2: ((File) -> T)? = null, result2: T? = null) =
    baseTest("2023/day$day.txt", phase1, result1, phase2, result2)

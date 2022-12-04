package me.martelli.adventofcode.day4

import java.io.File

fun main() {
    val input = File(ClassLoader.getSystemResource("day4.txt").file)
    val lines = input.readLines()
    val pairs = lines.map { it.split(',', '-').let { (a1, a2, b1, b2) -> a1.toInt()..a2.toInt() to b1.toInt()..b2.toInt() } }
    val fullyEnclosed = pairs.count { (a, b) -> fullyEnclosed(a, b) || fullyEnclosed(b, a) }
    val partiallyOverlapped = pairs.count  { (a, b) -> partiallyOverlapped(a, b) || partiallyOverlapped(b, a) }
    println("1) Fully enclosed: $fullyEnclosed")
    println("2) Partially overlapped: $partiallyOverlapped")
}

fun fullyEnclosed(a: IntRange, b: IntRange): Boolean = a.first in b && a.last in b

fun partiallyOverlapped(a: IntRange, b: IntRange): Boolean = a.first in b || a.last in b

package me.martelli.adventofcode.year2024

import arrow.core.MemoizedDeepRecursiveFunction
import arrow.core.memoize
import me.martelli.adventofcode.test

fun main() = test(2024, 11, ::part1, ::part2)

private fun part1(input: String) = cycle(input, 25)

private fun part2(input: String) = cycle(input, 75)

private fun cycle(input: String, times: Int): Long = input.split(" ")
    .map { it.toLong() }
    .sumOf { blinkMultiple(it to times) }

private val blinkMultiple = MemoizedDeepRecursiveFunction<Pair<Long, Int>, Long> { (stone, times) ->
    val blinked = blinkMemo(stone)
    if (times == 1) return@MemoizedDeepRecursiveFunction blinked.size.toLong()

    var count = callRecursive(blinked[0] to times - 1)
    if (blinked.size > 1) count += callRecursive(blinked[1] to times - 1)

    return@MemoizedDeepRecursiveFunction count
}

private fun blink(stone: Long) = if (stone == 0L) {
    arrayOf(1L)
} else {
    val s = stone.toString()
    if (s.length % 2 == 0) {
        val a = s.substring(0, s.length / 2).toLong()
        val b = s.substring(s.length / 2).toLong()
        arrayOf(a, b)
    } else {
        arrayOf(stone * 2024L)
    }
}

private val blinkMemo = ::blink.memoize()

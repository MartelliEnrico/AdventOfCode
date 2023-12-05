package me.martelli.adventofcode.year2023

import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() = test("05", ::phase1, 579439039, ::phase2, 7873084)

private fun phase1(input: File): Long {
    val sections = input.readText().split("\n\n", "\r\n\r\n")
    val seeds = sections[0]
        .split(": ")[1]
            .split(" ")
            .map { it.toLong() }
    val steps = sections.drop(1)
        .map { it.toStep() }

    return seeds.minOf {
        steps.fold(it) { destination, step ->
            val offset = step.find { range -> destination in range.source }?.offset ?: 0
            destination + offset
        }
    }
}

private fun phase2(input: File): Long {
    val sections = input.readText().split("\n\n", "\r\n\r\n")
    val seedRanges = sections[0]
        .split(": ")[1]
            .split(" ")
            .map { it.toLong() }
            .windowed(2, step = 2) { it[0]..(it[0]+it[1]) }
    val steps = sections.drop(1)
        .map { it.toStep() }

    val valueRanges = mutableListOf<MutableList<List<LongRange>>>()
    seedRanges.forEach { valueRanges.add(mutableListOf(listOf(it))) }

    var minLocation = Long.MAX_VALUE
    for (valueRange in valueRanges) {
        var current = valueRange[0]
        for (step in steps) {
            current = current.flatMap { getMappedRanges(it, step) }
            valueRange.add(current)
        }
        minLocation = min(minLocation, current.minOf { it.first })
    }

    return minLocation
}

fun getMappedRanges(start: LongRange, step: List<StepRange>): List<LongRange> {
    val result = mutableListOf<LongRange>()

    var remain = start
    for (range in step) {
        if (remain.first < range.source.first) {
            result.add(remain.first..min(remain.last, range.source.first))
            remain = range.source.first..remain.last
        }
        if (remain.first < range.source.last && remain.last > range.source.first) {
            result.add(max(remain.first, range.source.first)+range.offset..min(remain.last, range.source.last)+range.offset)
            remain = range.source.last..remain.last
        }
    }
    result.add(remain)

    return result.filter { it.last > it.first }
}

fun String.toStep() =
    split("\n", "\r\n")
    .drop(1)
    .map { line ->
        val (destinationStart, sourceStart, length) = line.split(" ").map { it.toLong() }
        StepRange(sourceStart..(sourceStart+length), destinationStart)
    }
    .sortedBy { it.source.first }

data class StepRange(val source: LongRange, val destination: Long) {
    val offset = destination - source.first
}

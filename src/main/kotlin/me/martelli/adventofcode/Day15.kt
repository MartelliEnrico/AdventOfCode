package me.martelli.adventofcode

import kotlin.math.abs

fun main() {
    val input = resource("day15.txt")
    val lines = input.readLines()
    val sensors = lines.map { it.toSensor() }
    val filledAt2000000 = sensors.flatMap { it.signalRangeAt(2_000_000) }.toSet().count()
    val resonance = sensors.flatMap { it.outerPerimeter() }
        .first { it.x in 0..4_000_000 && it.y in 0..4_000_000
                && sensors.none { sensor -> distance(sensor.position, it) <= sensor.distance } }
        .let { it.x * 4_000_000L + it.y }
    println("1) Filled coordinates at y=2000000: $filledAt2000000")
    println("2) Resonance value: $resonance")
}

private data class Coordinates(val x: Int, val y: Int)

private data class Sensor(val position: Coordinates, val beaconPosition: Coordinates) {
    val distance = distance(position, beaconPosition)
}

private fun distance(a: Coordinates, b: Coordinates) = abs(a.x - b.x) + abs(a.y - b.y)

private fun String.toSensor() = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()
    .find(this)!!.destructured.let { (x1, y1, x2, y2) ->
    Sensor(
        Coordinates(x1.toInt(), y1.toInt()),
        Coordinates(x2.toInt(), y2.toInt())
    )
}

private fun Sensor.signalRangeAt(y: Int) = buildSet {
    val remaining = distance - abs(position.y - y)
    for (x in 1..remaining) {
        add(position.copy(x = position.x + x, y = y))
        add(position.copy(x = position.x - x, y = y))
    }
}

private fun Sensor.outerPerimeter() = buildSet {
    val perimeter = distance + 1
    for (i in 0..distance) {
        add(position.copy(x = position.x + perimeter - i, y = position.y - i))
        add(position.copy(x = position.x + i, y = position.y + perimeter - i))
        add(position.copy(x = position.x - perimeter + i, y = position.y + i))
        add(position.copy(x = position.x - i, y = position.y - perimeter + i))
    }
}

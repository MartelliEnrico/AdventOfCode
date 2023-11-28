package me.martelli.adventofcode.year2022

import kotlin.math.abs
import kotlin.math.max

fun main() {
    val input = resource("day09.txt")
    val lines = input.readLines()
    val rules = lines.map { it.split(" ").let { tokens -> Movement(tokens[0].toDirection(), tokens[1].toInt()) } }
    val head = Segment(Position(0, 0))
    val visitedBy1 = mutableSetOf<Position>()
    val visitedBy9 = mutableSetOf<Position>()
    var currentHead = head
    for (i in 1..9) {
        val segment = when (i) {
            1 -> LoggingSegment(visitedBy1, currentHead.position.copy())
            9 -> LoggingSegment(visitedBy9, currentHead.position.copy())
            else -> Segment(currentHead.position.copy())
        }
        currentHead.next = segment
        currentHead = segment
    }
    rules.forEach { head.move(it) }
    val totalVisitedBy1 = visitedBy1.size
    val totalVisitedBy9 = visitedBy9.size
    println("1) Total visited by 1: $totalVisitedBy1")
    println("2) Total visited by 9: $totalVisitedBy9")
}

private enum class Direction {
    Up, Down, Left, Right
}

private fun String.toDirection() = when (this) {
    "U" -> Direction.Up
    "D" -> Direction.Down
    "L" -> Direction.Left
    "R" -> Direction.Right
    else -> error("unreachable")
}

private data class Movement(val direction: Direction, val steps: Int)

private data class Position(val x: Int, val y: Int)

private fun Position.distance(other: Position): Int = max(abs(x - other.x), abs(y - other.y))

private open class Segment(var position: Position, var next: Segment? = null) {
    fun move(direction: Direction) {
        position.also {
            position = when (direction) {
                Direction.Up -> it.copy(y = it.y + 1)
                Direction.Down -> it.copy(y = it.y - 1)
                Direction.Left -> it.copy(x = it.x - 1)
                Direction.Right -> it.copy(x = it.x + 1)
            }
            next?.update(position)
        }
    }

    open fun update(previousSegmentPosition: Position) {
        val distance = position.distance(previousSegmentPosition)
        if (distance > 1) {
            val movementX = (previousSegmentPosition.x - position.x).coerceIn(-1, 1)
            val movementY = (previousSegmentPosition.y - position.y).coerceIn(-1, 1)
            position = position.copy(x = position.x + movementX, y = position.y + movementY)
            next?.update(position)
        }
    }
}

private fun Segment.move(movement: Movement) {
    for (i in 1..movement.steps) {
        move(movement.direction)
    }
}

private class LoggingSegment(val visited: MutableSet<Position>, position: Position, next: Segment? = null) : Segment(position, next) {
    init {
        visited.add(position)
    }

    override fun update(previousSegmentPosition: Position) {
        super.update(previousSegmentPosition)
        visited.add(position)
    }
}

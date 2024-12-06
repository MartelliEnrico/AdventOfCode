package me.martelli.adventofcode.year2024

import me.martelli.adventofcode.test

fun main() = test(2024, 6, ::part1, ::part2)

private fun part1(input: String): Int {
    val (guard, walls) = getPoints(input)
    val visited = getPath(guard, walls)
    return visited.size
}

private fun part2(input: String): Int {
    val (guard, walls) = getPoints(input)
    val path = getPath(guard, walls) - Point(guard.x, guard.y)
    return path.count { getPath(guard, walls + it).isEmpty() }
}

private fun getPoints(input: String): Pair<Position, List<Point>> {
    lateinit var guard: Position
    val walls = buildList {
        input.lines().forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                when (char) {
                    Guard -> guard = Position(x, y, Direction.Up)
                    Wall -> add(Point(x, y))
                }
            }
        }
    }

    return guard to walls
}

private fun getPath(start: Position, walls: List<Point>): Set<Point> {
    var guard = start
    val width = walls.maxOf { it.x }
    val height = walls.maxOf { it.y }
    val visited = mutableSetOf<Position>()

    do {
        val wall = when (guard.direction) {
            Direction.Up -> walls.filter { it.x == guard.x && it.y < guard.y }.maxByOrNull { it.y } ?: Point(guard.x, -1)
            Direction.Down -> walls.filter { it.x == guard.x && it.y > guard.y }.minByOrNull { it.y } ?: Point(guard.x, height + 1)
            Direction.Left -> walls.filter { it.y == guard.y && it.x < guard.x }.maxByOrNull { it.x } ?: Point(-1, guard.y)
            Direction.Right -> walls.filter { it.y == guard.y && it.x > guard.x }.minByOrNull { it.x } ?: Point(width + 1, guard.y)
        }

        val positions = when (guard.direction) {
            Direction.Up -> (wall.y+1..guard.y).map { y -> Position(guard.x, y, guard.direction) }
            Direction.Down -> (guard.y..<wall.y).map { y -> Position(guard.x, y, guard.direction) }
            Direction.Left -> (wall.x+1..guard.x).map { x -> Position(x, guard.y, guard.direction) }
            Direction.Right -> (guard.x..<wall.x).map { x -> Position(x, guard.y, guard.direction) }
        }

        if (!visited.addAll(positions)) return emptySet() // we are looping

        guard = when (guard.direction) {
            Direction.Up -> Position(wall.x, wall.y + 1, Direction.Right)
            Direction.Down -> Position(wall.x, wall.y - 1, Direction.Left)
            Direction.Left -> Position(wall.x + 1, wall.y, Direction.Up)
            Direction.Right -> Position(wall.x - 1, wall.y, Direction.Down)
        }
    } while (guard.x in 1..<width && guard.y in 1..<height)

    return visited.map { Point(it.x, it.y) }.toSet()
}

private const val Guard = '^'
private const val Wall = '#'

private data class Point(val x: Int, val y: Int)
private data class Position(val x: Int, val y: Int, val direction: Direction)
private enum class Direction { Up, Right, Down, Left }

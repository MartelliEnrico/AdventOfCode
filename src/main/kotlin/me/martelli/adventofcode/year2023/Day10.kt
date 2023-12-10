package me.martelli.adventofcode.year2023

import java.io.File

fun main() = test("10", ::phase1, 6882, ::phase2)

private fun phase1(input: File): Int {
    val map = input.readLines().map { it.map(Char::toTile) }

    val start = map.mapIndexed { x, line -> x to line.indexOf(Tile.StartingPosition) }.first { (_, y) -> y != -1 }
    val startingPipe = findStartingPipeDirection(start, map)

    var previous1 = start
    var previous2 = start

    var (point1, point2) = findNextPoints(start, startingPipe)

    var distance = 1

    do {
        val temp1 = previous1
        previous1 = point1
        point1 = findNextPoint(temp1, point1, map)

        val temp2 = previous2
        previous2 = point2
        point2 = findNextPoint(temp2, point2, map)

        distance++
    } while (point1 != point2)

    return distance
}

private fun phase2(input: File): Int {
    val map = input.readLines().map { it.map(Char::toTile) }

    val start = map.mapIndexed { x, line -> x to line.indexOf(Tile.StartingPosition) }.first { (_, y) -> y != -1 }
    val startingPipe = findStartingPipeDirection(start, map)

    val loop = mutableSetOf(start)

    var previous = start
    var point = findNextPoints(start, startingPipe)[0]

    while (point != start) {
        loop.add(point)
        val temp = previous
        previous = point
        point = findNextPoint(temp, point, map)
    }

    var count = 0

    map.forEachIndexed { x, line ->
        var inside = false
        line.forEachIndexed { y, tile ->
            val p = x to y
            val inLoop = p in loop
            if (tile is Tile.Pipe && tile.south && inLoop) {
                inside = !inside
            } else if (inside && !inLoop) {
                count++
            }
        }
    }

    return count
}

sealed interface Tile {
    data object StartingPosition : Tile
    data object Ground : Tile
    data class Pipe(
        val north: Boolean = false,
        val south: Boolean = false,
        val east: Boolean = false,
        val west: Boolean = false
    ) : Tile
}

fun Char.toTile() = when (this) {
    'S' -> Tile.StartingPosition
    '.' -> Tile.Ground
    '|' -> Tile.Pipe(north = true, south = true)
    '-' -> Tile.Pipe(west = true, east = true)
    'L' -> Tile.Pipe(north = true, east = true)
    'J' -> Tile.Pipe(north = true, west = true)
    '7' -> Tile.Pipe(south = true, west = true)
    'F' -> Tile.Pipe(south = true, east = true)
    else -> error("unreachable")
}

fun findStartingPipeDirection(start: Pair<Int, Int>, map: List<List<Tile>>): Tile.Pipe {
    val (x, y) = start
    val north = map[x - 1][y]
    val south = map[x + 1][y]
    val east = map[x][y + 1]
    val west = map[x][y - 1]
    return Tile.Pipe(
        north = south is Tile.Pipe && south.north,
        south = north is Tile.Pipe && north.south,
        east = west is Tile.Pipe && west.east,
        west = east is Tile.Pipe && east.west
    )
}

fun Pair<Int, Int>.goTo(
    north: Boolean = false,
    south: Boolean = false,
    east: Boolean = false,
    west: Boolean = false
): Pair<Int, Int>? {
    val (x, y) = this
    return when {
        north -> x-1 to y
        south -> x+1 to y
        east -> x to y+1
        west -> x to y-1
        else -> null
    }
}

fun findNextPoints(point: Pair<Int, Int>, pipe: Tile.Pipe): List<Pair<Int, Int>> {
    return listOfNotNull(
        point.goTo(north = pipe.north),
        point.goTo(south = pipe.south),
        point.goTo(east = pipe.east),
        point.goTo(west = pipe.west)
    )
}

fun findNextPoint(previous: Pair<Int, Int>, current: Pair<Int, Int>, map: List<List<Tile>>): Pair<Int, Int> {
    val tile = map[current.first][current.second] as Tile.Pipe
    return findNextPoints(current, tile).first { it != previous }
}

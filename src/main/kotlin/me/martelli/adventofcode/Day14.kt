package me.martelli.adventofcode

fun main() {
    val input = resource("day14.txt")
    val lines = input.readLines()
    val paths = lines.map { it.toPath() }
    val grid = paths.flatMap { it.drawLine() }.toSet()
    val bottomlessSandUnits = simulateSandBottomless(grid)
    val sandUnits = simulateSand(grid)
    println("1) Unit of sand added to bottomless pit: $bottomlessSandUnits")
    println("2) Unit of sand added: $sandUnits")
}

private fun String.toPath() = this.split(" -> ").map { it.toPoint() }.let { Path(it) }

private fun String.toPoint() = this.split(",").let { (x, y) -> Point(x.toInt(), y.toInt()) }

private data class Path(val points: List<Point>)

private fun Path.drawLine() = points.windowed(2) { (a, b) ->
    if (a.x == b.x) {
        listOf(a.y, b.y).sorted().let { (min, max) -> min..max }.map { Point(a.x, it) }
    } else {
        listOf(a.x, b.x).sorted().let { (min, max) -> min..max }.map { Point(it, a.y) }
    }
}.flatten().toSet()

private data class Point(val x: Int, val y: Int) {
    companion object {
        val Start = Point(500, 0)
    }
}

private fun simulateSandBottomless(grid: Set<Point>): Int {
    val newGrid = grid.toMutableSet()
    fun Point.next() = listOf(copy(y = y+1), copy(x = x-1, y = y+1), copy(x = x+1, y = y+1)).firstOrNull { !newGrid.contains(it) }
    val maxY = grid.maxOf { it.y } + 1
    var sand = Point.Start
    while (true) {
        val next = sand.next()
        if (next == null) {
            newGrid.add(sand)
            sand = Point.Start
        } else if (next.y < maxY) {
            sand = next
        } else {
            break
        }
    }
    return newGrid.size - grid.size
}

private fun simulateSand(grid: Set<Point>): Int {
    val newGrid = grid.toMutableSet()
    val maxY = grid.maxOf { it.y } + 2
    fun Point.next() = listOf(copy(y = y+1), copy(x = x-1, y = y+1), copy(x = x+1, y = y+1)).firstOrNull { !newGrid.contains(it) }
    var sand = Point.Start
    while (true) {
        val next = sand.next()
        if (next == null || next.y == maxY) {
            newGrid.add(sand)
            if (sand == Point.Start) break
            sand = Point.Start
        } else if (next.y < maxY) {
            sand = next
        }
    }
    return newGrid.size - grid.size
}

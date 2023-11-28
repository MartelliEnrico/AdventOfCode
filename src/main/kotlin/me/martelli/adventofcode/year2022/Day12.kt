package me.martelli.adventofcode.year2022

fun main() {
    val input = resource("day12.txt")
    val lines = input.readLines()
    val heightMap = lines.map { line -> line.map { it.height } }
    val start = find(lines, 'S')
    val lowest = findAll(lines, 'a')
    val end = find(lines, 'E')
    val distanceFromStart = shortestDistance(heightMap, start, end)
    val shortestDistance = lowest.map { shortestDistance(heightMap, it, end) }.sorted()[0]
    println("1) Distance from start: $distanceFromStart")
    println("2) Shortest distance: $shortestDistance")
}

private val Char.height: Int
    get() = when (this) {
        'S' -> 1
        'E' -> 26
        in 'a'..'z' -> 1 + (this - 'a')
        else -> error("unreachable")
    }

private fun find(lines: List<String>, character: Char) = lines
    .mapIndexed { index, line -> index to line.indexOf(character) }
    .first { (_, x) -> x != -1 }

private fun findAll(lines: List<String>, character: Char) = lines
    .mapIndexed { index, line -> index to line.indexOf(character) }
    .filter { (_, x) -> x != -1 }

private fun moves(pair: Pair<Int, Int>) = buildList {
    val (y, x) = pair
    add(y+1 to x)
    add(y-1 to x)
    add(y to x+1)
    add(y to x-1)
}

private fun shortestDistance(heightMap: List<List<Int>>, start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
    fun possible(from: Pair<Int, Int>, to: Pair<Int, Int>) = heightMap[to] in 0..heightMap[from]+1
    val unvisitedSet = mutableSetOf<Pair<Int, Int>>()
    val tentativeDistance = mutableMapOf<Pair<Int, Int>, Int>()
    for (y in heightMap.indices) {
        for (x in heightMap[y].indices) {
            unvisitedSet.add(y to x)
            tentativeDistance[y to x] = Int.MAX_VALUE
        }
    }
    tentativeDistance[start] = 0
    var current = start
    do {
        val moves = moves(current).filter { it in unvisitedSet }.filter { possible(current, it) }
        for (move in moves) {
            val distance = tentativeDistance[current]!! + 1
            if (distance < tentativeDistance[move]!!) tentativeDistance[move] = distance
        }
        unvisitedSet.remove(current)
        current = unvisitedSet.sortedBy { tentativeDistance[it]!! }[0]
        if (tentativeDistance[current]!! == Int.MAX_VALUE) break
    } while (current != end)
    return tentativeDistance[end]!!
}

private operator fun List<List<Int>>.get(pair: Pair<Int, Int>) = this[pair.first][pair.second]

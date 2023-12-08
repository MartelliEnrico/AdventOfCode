package me.martelli.adventofcode.year2023

import java.io.File

fun main() = test("08", ::phase1, 14893, ::phase2)

private fun phase1(input: File): Int {
    val map = input.readLines().toSandMap()
    return map.countSteps()
}

private fun phase2(input: File): Int {
    val map = input.readLines().toSandMap()
    return map.countStepsSimultaneously()
}

fun List<String>.toSandMap(): SandMap {
    val directions = get(0).map {
        when (it) {
            'L' -> Direction.Left
            'R' -> Direction.Right
            else -> error("can't happen")
        }
    }

    val nodes = drop(2).associate {
        val (node, left, right) = NodeLine.matchEntire(it)!!.destructured
        node to (left to right)
    }

    return SandMap(directions, nodes)
}

const val NodeId = "[A-Z]{3}"
val NodeLine = "($NodeId) = \\(($NodeId), ($NodeId)\\)".toRegex()

data class SandMap(val directions: List<Direction>, val nodes: Map<String, Pair<String, String>>) {
    private val loopingDirections
        get() = sequence { while (true) yieldAll(directions) }.iterator()

    fun countSteps(): Int {
        val directions = loopingDirections
        var count = 0
        var node = "AAA"
        do {
            val (left, right) = nodes[node]!!
            node = when (directions.next()) {
                Direction.Left -> left
                Direction.Right -> right
            }
            count++
        } while (node != "ZZZ")
        return count
    }

    fun countStepsSimultaneously(): Int {
        val directions = loopingDirections
        var count = 0
        var shouldContinue: Boolean
        var currentNodes = nodes.keys.filter { it.endsWith("A") }
        do {
            shouldContinue = false
            val nextDirection = directions.next()
            currentNodes = currentNodes.map {
                val (left, right) = nodes[it]!!
                when (nextDirection) {
                    Direction.Left -> left
                    Direction.Right -> right
                }.also { n -> shouldContinue = shouldContinue || !n.endsWith("Z") }
            }
            count++
        } while (shouldContinue)
        return count
    }
}

enum class Direction {
    Left, Right
}

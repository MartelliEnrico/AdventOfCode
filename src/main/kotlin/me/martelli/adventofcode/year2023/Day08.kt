package me.martelli.adventofcode.year2023

import java.io.File

fun main() = test("08", ::phase1, 14893, ::phase2, 10241191004509)

private fun phase1(input: File): Long {
    val map = input.readLines().toSandMap()
    return map.countSteps()
}

private fun phase2(input: File): Long {
    val map = input.readLines().toSandMap()
    return map.countStepsSimultaneously()
}

private fun List<String>.toSandMap(): SandMap {
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

private const val NodeId = "[A-Z]{3}"
private val NodeLine = "($NodeId) = \\(($NodeId), ($NodeId)\\)".toRegex()

private data class SandMap(val directions: List<Direction>, val nodes: Map<String, Pair<String, String>>) {
    private val loopingDirections
        get() = sequence { while (true) yieldAll(directions) }.iterator()

    fun countSteps(): Long {
        val directions = loopingDirections
        var count = 0L
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

    fun countStepsSimultaneously(): Long {
        fun gcd(a: Long, b: Long): Long {
            return if (b == 0L) a else gcd(b, a % b)
        }

        fun lcm(a: Long, b: Long): Long {
            return a * (b / gcd(a, b))
        }

        return nodes.keys.filter { it.endsWith("A") }.map {
            val directions = loopingDirections
            var count = 0L
            var node = it
            var firstEnd = 0L
            while (true) {
                val (left, right) = nodes[node]!!
                node = when (directions.next()) {
                    Direction.Left -> left
                    Direction.Right -> right
                }
                count++
                if (node.endsWith("Z")) {
                    if (firstEnd == 0L) {
                        firstEnd = count
                    } else {
                        break
                    }
                }
            }
            count - firstEnd
        }.reduce(::lcm)
    }
}

private enum class Direction {
    Left, Right
}

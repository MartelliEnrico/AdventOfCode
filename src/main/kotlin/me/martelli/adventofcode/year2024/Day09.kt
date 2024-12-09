package me.martelli.adventofcode.year2024

import me.martelli.adventofcode.test

fun main() = test(2024, 9, ::part1, ::part2)

private fun part1(input: String): Long {
    val memory = input.flatMapIndexed { index, char ->
        val data = if (index % 2 == 0) index / 2 else -1
        List(char - '0') { data }
    }.toMutableList()

    var firstSpace = -1
    for (i in memory.indices.reversed()) {
        val value = memory[i]
        if (value == -1) continue

        firstSpace = memory.indexOf(-1)
        if (firstSpace > i) break

        memory[firstSpace] = value
        memory[i] = -1
    }

    return memory.take(firstSpace)
        .mapIndexed { index, s -> index * s.toLong() }
        .sum()
}

private fun part2(input: String): Long {
    val memory = input.mapIndexed { index, char ->
        val size = char - '0'
        if (index % 2 == 0) Block.Data(size, index / 2) else Block.Free(size)
    }.toMutableList()

    for (i in memory.indices.reversed()) {
        val block = memory[i]
        if (block is Block.Free) continue

        val firstSpace = memory.indexOfFirst { it is Block.Free && it.size >= block.size }
        if (firstSpace > i || firstSpace == -1) continue

        memory[i] = Block.Free(block.size)

        val free = memory[firstSpace]
        if (free.size > block.size) {
            memory[firstSpace] = Block.Free(free.size - block.size) // might need to unify with following space
            memory.add(firstSpace, block)
        } else {
            memory[firstSpace] = block
        }
    }

    return memory.flatMap { data ->
        val num = if (data is Block.Data) data.id else 0
        List(data.size) { num }
    }
        .mapIndexed { index, s -> index * s.toLong() }
        .sum()
}

private sealed class Block(val size: Int) {
    class Free(size: Int) : Block(size)
    class Data(size: Int, val id: Int) : Block(size)
}

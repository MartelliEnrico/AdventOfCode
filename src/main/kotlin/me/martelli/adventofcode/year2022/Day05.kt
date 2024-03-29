package me.martelli.adventofcode.year2022

fun main() {
    val input = resource("day05.txt")
    val lines = input.readLines()
    val splitIndex = lines.indexOf("")
    val stacks = lines.subList(0, splitIndex - 1).toStacks()
    val instructions = lines.subList(splitIndex + 1, lines.size).map { it.toInstruction() }
    val crateMover9000 = clone(stacks).apply { instructions.forEach { it.move9000(this) } }.joinToString(separator = "") { it.last().toString() }
    val crateMover9001 = clone(stacks).apply { instructions.forEach { it.move9001(this) } }.joinToString(separator = "") { it.last().toString() }
    println("1) CrateMover 9000: $crateMover9000")
    println("1) CrateMover 9001: $crateMover9001")
}

private fun clone(stacks: List<ArrayDeque<Char>>) = stacks.map { ArrayDeque(it) }.toList()

private val stackRegex = Regex("""(\s{3}|\[\w])(?:$|\s)""")
private fun String.toCrates() = stackRegex.findAll(this).map { it.value[1] }.toList()
private fun List<String>.toStacks() = map { it.toCrates() }.let {
    List(it[0].size) { ArrayDeque<Char>() }.also { stacks ->
        it.forEach { chars ->
            chars.forEachIndexed { i, c ->
                if (c != ' ') stacks[i].addFirst(c)
            }
        }
    }
}

private val instructionRegex = Regex("""move (\d+) from (\d+) to (\d+)""")
private fun String.toInstruction() = instructionRegex.find(this)!!.destructured.let { (howMany, fromIndex, toIndex) ->
    Instruction(howMany.toInt(), fromIndex.toInt() - 1, toIndex.toInt() - 1)
}

private data class Instruction(val howMany: Int, val fromIndex: Int, val toIndex: Int) {
    fun move9000(stacks: List<ArrayDeque<Char>>) {
        for (i in 1..howMany) {
            val crate = stacks[fromIndex].removeLast()
            stacks[toIndex].addLast(crate)
        }
    }

    fun move9001(stacks: List<ArrayDeque<Char>>) {
        val deque = ArrayDeque<Char>()
        for (i in 1..howMany) {
            val crate = stacks[fromIndex].removeLast()
            deque.addFirst(crate)
        }
        stacks[toIndex].addAll(deque)
    }
}

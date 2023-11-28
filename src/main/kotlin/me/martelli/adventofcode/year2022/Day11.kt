package me.martelli.adventofcode.year2022

fun main() {
    val input = resource("day11.txt")
    val lines = input.readLines()
    val monkeys = lines.chunked(7).map { it.toMonkey() }
    val firstRun = run(clone(monkeys), 20) { it.floorDiv(3) }
    val secondRun = run(clone(monkeys), 10_000) { it }
    println("1) Monkey business #1: $firstRun")
    println("2) Monkey business #2: $secondRun")
}

private data class Monkey(
    val items: MutableList<Long>,
    val worryLevelIncrease: (Long) -> Long,
    val test: Long,
    val positive: Int,
    val negative: Int
) {
    fun throwTo(item: Long) = if (item % test == 0L) positive else negative
}

private fun List<String>.toMonkey(): Monkey {
    val items = this[1].split(": ")[1].split(",").map { it.trim().toLong() }.toMutableList()
    val operation: (Long) -> Long = this[2].split(" = ")[1].split(" ").let {
        when (it[1]) {
            "+" -> { old -> old + it[2].trim().toLong() }
            "*" -> when (it[2]) {
                "old" -> { old -> old * old }
                else -> { old -> old * it[2].trim().toLong() }
            }
            else -> error("unreachable")
        }
    }
    val test = this[3].split("divisible by ")[1].trim().toLong()
    val positive = this[4].split("monkey ")[1].trim().toInt()
    val negative = this[5].split("monkey ")[1].trim().toInt()
    return Monkey(items, operation, test, positive, negative)
}

private fun clone(monkeys: List<Monkey>) = monkeys.map { it.copy(items = it.items.toMutableList()) }.toList()

private fun run(monkeys: List<Monkey>, repetitions: Int, worryLevelDecrease: (Long) -> Long): Long {
    val inspections = MutableList(monkeys.size) { 0L }
    val lcm = monkeys.map { it.test }.reduce { a, b -> a * b }
    repeat(repetitions) {
        for (i in monkeys.indices) {
            val monkey = monkeys[i]
            inspections[i] += monkey.items.size.toLong()
            for (item in monkey.items) {
                val worryLevel = worryLevelDecrease(monkey.worryLevelIncrease(item)) % lcm
                monkeys[monkey.throwTo(worryLevel)].items.add(worryLevel)
            }
            monkey.items.clear()
        }
    }
    return inspections.sortedDescending().take(2).reduce { a, b -> a * b }
}

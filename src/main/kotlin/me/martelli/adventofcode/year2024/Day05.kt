package me.martelli.adventofcode.year2024

import me.martelli.adventofcode.test

fun main() = test(2024, 5, ::part1, ::part2)

private fun part1(input: String): Int {
    val (rules, printings) = input.getRulesAndPrintings()

    val correct = printings.filter { pages ->
        pages.forEachIndexed { index, page ->
            val prevPages = rules.getOrDefault(page, emptyList())
            if (prevPages.any { pages.indexOf(it) > index }) return@filter false
        }
        true
    }

    return correct.sumOf { it[it.size / 2] }
}

private fun part2(input: String): Int {
    val (rules, printings) = input.getRulesAndPrintings()

    val incorrect = printings.filterNot { pages ->
        pages.forEachIndexed { index, page ->
            val prevPages = rules.getOrDefault(page, emptyList())
            if (prevPages.any { pages.indexOf(it) > index }) return@filterNot false
        }
        true
    }.map { p ->
        val pages = p.toMutableList()
        fixOrdering(pages, rules)
        pages.toList()
    }

    return incorrect.sumOf { it[it.size / 2] }
}

private fun String.getRulesAndPrintings() = let { input ->
    val (r, p) = input.lines().let {
        val splitIndex = it.indexOf("")
        it.subList(0, splitIndex) to it.subList(splitIndex + 1, it.size)
    }

    val rules = r.map {
        it.split("|")
            .map(String::toInt)
    }.groupBy({it[1]}, {it[0]})

    val printings = p.map {
        it.split(",")
            .map(String::toInt)
    }

    rules to printings
}

private fun fixOrdering(list: MutableList<Int>, rules: Map<Int, List<Int>>) {
    list.forEachIndexed { index, page ->
        val prevPages = rules.getOrDefault(page, emptyList())
        for (prevPage in prevPages) {
            val prevIndex = list.indexOf(prevPage)
            if (prevIndex > index) {
                list[prevIndex] = list[index].also { list[index] = list[prevIndex] }
                fixOrdering(list.subList(0, prevIndex), rules)
            }
        }
    }
}

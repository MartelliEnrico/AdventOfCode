package me.martelli.adventofcode.year2023

import java.io.File

fun main() = test("03", ::phase1, 536576, ::phase2, 75741499)

private fun phase1(input: File): Int {
    val data = input.readLines().flatMapIndexed { index, s ->
        Points.findAll(s).map {
            if (it.value[0].isDigit()) {
                Data.Number(it.value.toInt(), index, it.range)
            } else {
                Data.Symbol(index, it.range.first)
            }
        }
    }.groupBy { it is Data.Number }

    val numbers = data[true]!!
    val symbols = data[false]!!
    var total = 0

    for (number in numbers) {
        val (lines, rows) = (number as Data.Number).boundingBox()
        for (symbol in symbols) {
            val (line, row) = symbol as Data.Symbol
            if (line in lines && row in rows) {
                total += number.value
                break
            }
        }
    }

    return total
}

private fun phase2(input: File): Int {
    val data = input.readLines().flatMapIndexed { index, s ->
        Points.findAll(s).mapNotNull {
            if (it.value[0].isDigit()) {
                Data.Number(it.value.toInt(), index, it.range)
            } else if (it.value == "*") {
                Data.Symbol(index, it.range.first)
            } else {
                null
            }
        }
    }.groupBy { it is Data.Number }

    val numbers = data[true]!!
    val symbols = data[false]!!
    var total = 0

    external@ for (symbol in symbols) {
        val (line, row) = symbol as Data.Symbol
        var power = 1
        var count = 0
        for (number in numbers) {
            val (lines, rows) = (number as Data.Number).boundingBox()
            if (line in lines && row in rows) {
                if (count == 2) {
                    continue@external
                } else {
                    power *= number.value
                    count++
                }
            }
        }
        if (count == 2) {
            total += power
        }
    }

    return total
}

val Points = "(\\d+|[^.\\d])".toRegex()

sealed interface Data {
    data class Number(val value: Int, val line: Int, val range: IntRange): Data {
        fun boundingBox() = (line-1)..(line+1) to (range.first-1)..(range.last+1)
    }
    data class Symbol(val line: Int, val column: Int): Data
}

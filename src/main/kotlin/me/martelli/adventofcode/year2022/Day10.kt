package me.martelli.adventofcode.year2022

fun main() {
    val input = resource("day10.txt")
    val lines = input.readLines()
    val instructions = lines.map { it.toCpuInstruction() }
    val sequence = instructions.runCpuInstructions()
    val totalSignalStrengths = sequence.filter { (cycle, _) -> cycle in listOf(20, 60, 100, 140, 180, 220) }
        .sumOf { (cycle, value) -> cycle * value }
    val crt = sequence.chunked(40) {
        it.mapIndexed { i, (_, value) -> if (i in value-1..value+1) "#" else "." }.joinToString(separator = "")
    }.joinToString(separator = "\n")
    println("1) Sum of signal strengths: $totalSignalStrengths")
    println("2) CRT:\n$crt")
}

private sealed class CpuInstruction {
    data class AddX(val value: Int) : CpuInstruction()

    object NoOp : CpuInstruction()
}

private fun String.toCpuInstruction(): CpuInstruction = split(" ").let {
    return when (it[0]) {
        "addx" -> CpuInstruction.AddX(it[1].toInt())
        "noop" -> CpuInstruction.NoOp
        else -> error("unreachable")
    }
}

private fun List<CpuInstruction>.runCpuInstructions() = sequence {
    var registerValue = 1
    var cycle = 1
    this@runCpuInstructions.forEach {
        yield(cycle++ to registerValue)
        if (it is CpuInstruction.AddX) {
            yield(cycle++ to registerValue)
            registerValue += it.value
        }
    }
}

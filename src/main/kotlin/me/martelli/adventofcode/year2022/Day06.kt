package me.martelli.adventofcode.year2022

fun main() {
    val input = resource("day06.txt")
    val datastream = input.readText()
    val startOfPacket = findUniqueSequence(datastream, 4)
    val startOfMessage = findUniqueSequence(datastream, 14)
    println("1) Start of packet: $startOfPacket")
    println("2) Start of message: $startOfMessage")
}

private fun findUniqueSequence(input: String, size: Int) =
    input.windowedSequence(size).indexOfFirst { it.toSet().size == size } + size

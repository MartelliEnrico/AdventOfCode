package me.martelli.adventofcode

fun main() {
    val input = resource("day6.txt")
    val datastream = input.readText()
    val startOfPacket = datastream.windowedSequence(4).indexOfFirst { it.toCharArray().toSet().size == 4 } + 4
    val startOfMessage = datastream.windowedSequence(14).indexOfFirst { it.toCharArray().toSet().size == 14 } + 14
    println("1) Start of packet: $startOfPacket")
    println("2) Start of message: $startOfMessage")
}

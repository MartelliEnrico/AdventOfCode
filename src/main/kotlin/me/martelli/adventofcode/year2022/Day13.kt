package me.martelli.adventofcode.year2022

import kotlinx.serialization.json.*

fun main() {
    val input = resource("day13.txt")
    val lines = input.readLines()
    val groups = lines.filter { it.isNotEmpty() }.map(::parsePacket)
    val sumIndicesCorrectOrder = groups.chunked(2).mapIndexed { index, (left, right) -> if (left <= right) index + 1 else 0 }.sum()
    val key1 = Packet.Group(listOf(Packet.Group(listOf(Packet.Number(2)))))
    val key2 = Packet.Group(listOf(Packet.Group(listOf(Packet.Number(6)))))
    val orderedPackets = listOf(groups, listOf(key1, key2)).flatten().sorted()
    val decoderKey = (orderedPackets.indexOf(key1) + 1) * (orderedPackets.indexOf(key2) + 1)
    println("1) Sum of indices in correct order: $sumIndicesCorrectOrder")
    println("2) Decoder key: $decoderKey")
}

private sealed class Packet: Comparable<Packet> {
    class Number(val value: Int): Packet()

    class Group(val value: List<Packet>): Packet()

    override fun compareTo(other: Packet): Int {
        when {
            this is Number && other is Number -> return this.value.compareTo(other.value)
            this is Group && other is Group -> {
                for (i in this.value.indices) {
                    if (i !in other.value.indices) return 1
                    val compare = this.value[i].compareTo(other.value[i])
                    if (compare != 0) return compare
                }
                return this.value.size.compareTo(other.value.size)
            }
            this is Number -> return Group(listOf(this)).compareTo(other)
            else -> return this.compareTo(Group(listOf(other)))
        }
    }
}

private fun parsePacket(input: String) = innerParsePacket(Json.parseToJsonElement(input))

private fun innerParsePacket(element: JsonElement): Packet = when (element) {
    is JsonArray -> Packet.Group(element.map(::innerParsePacket))
    is JsonPrimitive -> Packet.Number(element.int)
    else -> error("unreachable")
}


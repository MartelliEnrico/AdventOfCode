package me.martelli.adventofcode.year2023

import java.io.File

fun main() = test("07", ::phase1, 253313241, ::phase2, 253362743)

private fun phase1(input: File): Long {
    return input.readLines()
        .map { it.toHand() }
        .sortedWith(Phase1Comparator)
        .mapIndexed { index, hand -> (index + 1) * hand.bid }
        .sum()
}

private fun phase2(input: File): Long {
    return input.readLines()
        .map { it.toHand() }
        .sortedWith(Phase2Comparator)
        .mapIndexed { index, hand -> (index + 1) * hand.bid }
        .sum()
}

fun String.toHand(): Hand {
    val (hand, bid) = split(" ")
    val cards = hand.map { CardLabel.valueOf(it.toString()) }
    return Hand(cards, bid.toLong())
}

data class Hand(val cards: List<CardLabel>, val bid: Long)

enum class CardLabel {
    A, K, Q, J, T,
    `9`, `8`, `7`, `6`,
    `5`, `4`, `3`, `2`
}

object Phase1Comparator : Comparator<Hand> {
    private fun Hand.calculateStrength(): Int {
        val map = cards.groupingBy { it }.eachCount()
        val threeOfKind = map.containsValue(3)
        val pairs = map.count { (_, value) -> value == 2 }
        return when {
            map.containsValue(5) -> 6
            map.containsValue(4) -> 5
            threeOfKind && pairs == 1 -> 4
            threeOfKind -> 3
            else -> pairs
        }
    }

    override fun compare(o1: Hand?, o2: Hand?): Int {
        val thisStrength = o1!!.calculateStrength()
        val otherStrength = o2!!.calculateStrength()
        return when {
            thisStrength != otherStrength -> thisStrength - otherStrength
            else -> o1.cards.zip(o2.cards)
                .map { (a, b) -> b.ordinal - a.ordinal }
                .find { it != 0 }!!
        }
    }
}

object Phase2Comparator : Comparator<Hand> {
    private fun Hand.calculateStrength(): Int {
        val map = cards.groupingBy { it }.eachCount()
        val fiveOfKind = map.containsValue(5)
        val fourOfKind = map.containsValue(4)
        val threeOfKind = map.containsValue(3)
        val pairs = map.count { (_, value) -> value == 2 }
        val jokers = map[CardLabel.J] ?: 0
        return when {
            fiveOfKind || (fourOfKind && jokers == 1) || (threeOfKind && jokers == 2) || (pairs == 1 && jokers == 3) || jokers == 4 -> 6
            fourOfKind || (threeOfKind && jokers == 1) || (pairs == 2 && jokers == 2) || jokers == 3 -> 5
            (threeOfKind && pairs == 1) || (pairs == 2 && jokers == 1) -> 4
            threeOfKind || (pairs == 1 && jokers == 1) || jokers == 2 -> 3
            pairs > 0 -> pairs
            else -> jokers
        }
    }

    private val CardLabel.customOrdinal: Int
        get() = when(this) {
            CardLabel.A -> 1
            CardLabel.K -> 2
            CardLabel.Q -> 3
            CardLabel.T -> 4
            CardLabel.`9` -> 5
            CardLabel.`8` -> 6
            CardLabel.`7` -> 7
            CardLabel.`6` -> 8
            CardLabel.`5` -> 9
            CardLabel.`4` -> 10
            CardLabel.`3` -> 11
            CardLabel.`2` -> 12
            CardLabel.J -> 13
        }

    override fun compare(o1: Hand?, o2: Hand?): Int {
        val thisStrength = o1!!.calculateStrength()
        val otherStrength = o2!!.calculateStrength()
        return when {
            thisStrength != otherStrength -> thisStrength - otherStrength
            else -> o1.cards.zip(o2.cards)
                .map { (a, b) -> b.customOrdinal - a.customOrdinal }
                .find { it != 0 }!!
        }
    }
}

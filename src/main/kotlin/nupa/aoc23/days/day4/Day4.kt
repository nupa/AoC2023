package nupa.aoc23.days.day4

import nupa.aoc23.input.lines
import nupa.aoc23.input.splitAndTrim
import nupa.aoc23.input.splitToInts
import nupa.aoc23.input.splitToPair

fun day4Part1() {
    val result = matches(lines("day4/input.txt")).sumOf { matches ->
        if (matches == 0)
            0
        else
            1 shl (matches - 1)
    }
    println(result)
}

fun day4Part2() {
    val cards = matches(lines("day4/input.txt")).map {
        CardPile(it)
    }

    for ((index, pile) in cards.withIndex()) {
        val increaseStart = index + 1
        val increasedPiles = cards.subList(increaseStart, increaseStart + pile.matches)
        increasedPiles.forEach { it.increaseAmount(pile.amount) }
    }
    // sum could have been counted easily as we go in previous loop, but this was chosen for clarity
    println(cards.sumOf { it.amount })
}

private fun matches(cardLines: List<String>): List<Int> {
    val numbers = cardLines.map { card -> card.splitAndTrim(":")[1].splitToPair("|") }
    return numbers.map {
        val winningNumbers = it.first.splitToInts("\\s+".toRegex())
        val ownNumbers = it.second.splitToInts("\\s+".toRegex())
        -((ownNumbers subtract winningNumbers.toSet()).size - ownNumbers.size)
    }
}

private class CardPile(val matches: Int) {
    var amount: Long = 1
        private set

    fun increaseAmount(increase: Long) {
        amount += increase
    }
}

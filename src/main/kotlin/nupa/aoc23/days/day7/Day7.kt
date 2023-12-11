package nupa.aoc23.days.day7

import nupa.aoc23.input.lines
import nupa.aoc23.input.splitToPair

//248396258

fun day7Part1() {
    val lines = lines("day7/input.txt")

    val games = lines.map { game ->
        val handAndBid = game.splitToPair(" ")
        val cardsWithAmount = mapOf<Char, Int>().toMutableMap()
        handAndBid.first.forEach {
            val cardAmount = cardsWithAmount.getOrDefault(it,0)
            cardsWithAmount[it] = cardAmount+1
        }
        val cards = cardsWithAmount.toList().sortedBy { it.second }.reversed()
        val hand = cards.toHand(handAndBid.first)
        Game(hand, handAndBid.second.toInt())
    }
    println(games.sortedWith(SimpleGameComparator(Part1CardComparator)).reversed().withIndex().fold(0L) { acc, gameWithIndex -> acc + (gameWithIndex.index + 1) * gameWithIndex.value.bid })
}

fun day7Part2() {
    val lines = lines("day7/input.txt")

    val games = lines.map { game ->
        val handAndBid = game.splitToPair(" ")
        val cardsAsList = handAndBid.first.toList()
        val numberOfJs = cardsAsList.count { it == 'J' }
        if (numberOfJs == 5)
            return@map Game(Hand.FiveOfKind(handAndBid.first), handAndBid.second.toInt())
        val cardsWithAmount = mapOf<Char, Int>().toMutableMap()
        cardsAsList.filter { it != 'J' }.forEach {
            val cardAmount = cardsWithAmount.getOrDefault(it,0)
            cardsWithAmount[it] = cardAmount+1
        }
        val cards = cardsWithAmount.toList().sortedBy { it.second }.reversed()
        val hand = (listOf(cards[0].first to cards[0].second + numberOfJs) + cards.subList(1, cards.size)).toHand(handAndBid.first)

        Game(hand, handAndBid.second.toInt())
    }

    println(games.sortedWith(SimpleGameComparator(Part2CardComparator)).reversed().withIndex().fold(0L) { acc, gameWithIndex -> acc + (gameWithIndex.index + 1) * gameWithIndex.value.bid })
}

object Part1CardComparator: Comparator<Char> {
    private val CARD_ORDER = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    override fun compare(o1: Char, o2: Char): Int {
        return CARD_ORDER.indexOf(o1) - CARD_ORDER.indexOf(o2)
    }
}

object Part2CardComparator: Comparator<Char> {
    private val CARD_ORDER = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
    override fun compare(o1: Char, o2: Char): Int {
        return CARD_ORDER.indexOf(o1) - CARD_ORDER.indexOf(o2)
    }
}

class SimpleHandComparator(private val cardComparator: Comparator<Char>): Comparator<Hand> {
    override fun compare(o1: Hand, o2: Hand): Int {
        return if (o1::class == o2::class) {
            o1.allCards.zip(o2.allCards).compareUntilDifferent(cardComparator)
        } else {
            o1.rank - o2.rank
        }
    }
}

class SimpleGameComparator(cardComparator: Comparator<Char>): Comparator<Game> {
    private val handComparator = SimpleHandComparator(cardComparator)
    override fun compare(o1: Game, o2: Game): Int =
        handComparator.compare(o1.hand, o2.hand)
}

class Game(val hand: Hand, val bid: Int)

sealed class Hand(val allCards: String) {
    abstract val rank: Int

    class FiveOfKind(allCards: String): Hand(allCards) {
        override val rank: Int = 1
        override fun toString(): String =
            "5 of a kind with $allCards"
    }

    class FourOfKind(allCards: String): Hand(allCards) {
        override val rank: Int = 2

        override fun toString(): String =
            "4 of a kind with $allCards"
    }

    class FullHouse(allCards: String): Hand(allCards) {
        override val rank: Int = 3

        override fun toString(): String =
            "full house with 3 $allCards"
    }

    class ThreeOfKind(allCards: String): Hand(allCards) {
        override val rank: Int = 4

        override fun toString(): String =
            "3 of a kind with $allCards"
    }

    class TwoPairs(allCards: String): Hand(allCards) {
        override val rank: Int = 5

        override fun toString(): String =
            "two pairs with $allCards"
    }

    class Pair(allCards: String): Hand(allCards) {
        override val rank: Int = 6

        override fun toString(): String =
            "pair with $allCards"
    }

    class HighCard(allCards: String): Hand(allCards) {
        override val rank: Int = 7

        override fun toString(): String =
            "high card with $allCards"
    }
}

fun List<Pair<Char, Char>>.compareUntilDifferent(with: Comparator<Char>): Int =
    firstNotNullOfOrNull { (o1, o2) -> with.compare(o1, o2).takeIf { it != 0 } } ?: 0

fun List<Pair<Char, Int>>.toHand(unsortedAllCards: String): Hand {
    return when (this[0].second) {
        5 -> Hand.FiveOfKind(unsortedAllCards)
        4 -> Hand.FourOfKind(unsortedAllCards)
        3 -> if (this[1].second == 2)
            Hand.FullHouse(unsortedAllCards)
        else
            Hand.ThreeOfKind(unsortedAllCards)
        2 -> if (this[1].second == 2) Hand.TwoPairs(unsortedAllCards) else Hand.Pair(unsortedAllCards)
        else -> Hand.HighCard(unsortedAllCards)
    }
}
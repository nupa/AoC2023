package nupa.aoc23.days.day7

import nupa.aoc23.input.lines
import nupa.aoc23.input.splitToPair


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
        val hand = when (cards[0].second) {
            5 -> Hand.FiveOfKind(cards[0].first, handAndBid.first)
            4 -> Hand.FourOfKind(cards[0].first, cards[1].first, handAndBid.first)
            3 -> if (cards[1].second == 2)
                Hand.FullHouse(cards[0].first, cards[1].first, handAndBid.first)
            else
                Hand.ThreeOfKind(cards[0].first, handAndBid.first)
            2 -> if (cards[1].second == 2) {
                val card1 = cards[0].first
                val card2 = cards[1].first
                if (CardComparator.compare(card1, card2) < 0) Hand.TwoPairs(card1, card2, cards[2].first, handAndBid.first) else Hand.TwoPairs(card2, card1, cards[2].first, handAndBid.first)
            } else {
                Hand.Pair(cards[0].first, handAndBid.first)
            }
            else -> Hand.HighCard(cards.map { it.first }.sortedWith(CardComparator).first(), handAndBid.first)
        }
        Game(hand, handAndBid.second.toInt())
    }
    println(games.sortedWith(SimpleGameComparator).reversed().withIndex().fold(0L) { acc, gameWithIndex -> acc + (gameWithIndex.index + 1) * gameWithIndex.value.bid })
}


object CardComparator: Comparator<Char> {
    private val CARD_ORDER = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    override fun compare(o1: Char, o2: Char): Int {
        return CARD_ORDER.indexOf(o1) - CARD_ORDER.indexOf(o2)
    }
}
object SimpleHandComparator: Comparator<Hand> {
    override fun compare(o1: Hand, o2: Hand): Int {
        return if (o1::class == o2::class) {
            o1.allCards.zip(o2.allCards).compareUntilDifferent()
        } else {
            o1.rank - o2.rank
        }
    }
}

object SimpleGameComparator: Comparator<Game> {
    override fun compare(o1: Game, o2: Game): Int =
        SimpleHandComparator.compare(o1.hand, o2.hand)
}

class Game(val hand: Hand, val bid: Int)

sealed class Hand(val allCards: String) {
    val sortedAllCards = allCards.toList().sortedWith(CardComparator)
    abstract val rank: Int

    class FiveOfKind(val card: Char, allCards: String): Hand(allCards) {
        override val rank: Int = 1
        override fun toString(): String =
            "5 of a kind with $card ($sortedAllCards)"
    }

    class FourOfKind(val card: Char, val single: Char, allCards: String): Hand(allCards) {
        override val rank: Int = 2

        override fun toString(): String =
            "4 of a kind with $card ($sortedAllCards)"
    }

    class FullHouse(val threeOfKindCard: Char, val pairCard: Char, allCards: String): Hand(allCards) {
        override val rank: Int = 3

        override fun toString(): String =
            "full house with 3 $threeOfKindCard and 2 $pairCard ($sortedAllCards)"
    }

    class ThreeOfKind(val card: Char, allCards: String): Hand(allCards) {
        override val rank: Int = 4

        override fun toString(): String =
            "3 of a kind with $card ($sortedAllCards)"
    }

    class TwoPairs(val highPairCard: Char, val lowPairCard: Char, val single: Char, allCards: String): Hand(allCards) {
        override val rank: Int = 5

        override fun toString(): String =
            "two pairs with $highPairCard (high) and $lowPairCard (low) ($sortedAllCards)"
    }

    class Pair(val card: Char, allCards: String): Hand(allCards) {
        override val rank: Int = 6

        override fun toString(): String =
            "pair with $card ($sortedAllCards)"
    }

    class HighCard(val card: Char, allCards: String): Hand(allCards) {
        override val rank: Int = 7

        override fun toString(): String =
            "high card with $card ($sortedAllCards)"
    }
}

fun List<Pair<Char, Char>>.compareUntilDifferent(): Int =
    firstNotNullOfOrNull { (o1, o2) -> CardComparator.compare(o1, o2).takeIf { it != 0 } } ?: 0
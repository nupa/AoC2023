package nupa.aoc23.days.day2

import nupa.aoc23.input.lines
import nupa.aoc23.input.splitAndTrim
import nupa.aoc23.input.splitToIntStringPair
import kotlin.math.max

fun day2Part1() {
    val lines = lines("day2/input.txt")
    val result = lines.mapNotNull {
        line -> Game.fromGameLine(line).takeIf { game -> game.batches.all { it.isValid() } }?.id
    }.sum()
    println(result)
}

fun day2Part2() {
    val lines = lines("day2/input.txt")
    val result = lines.sumOf { Game.fromGameLine(it).power() }
    println(result)
}


class Game(val id: Int, val batches: List<Batch>) {
    fun power(): Int {
        val minimumValid = batches.reduce{acc, batch -> Batch(max(batch.red, acc.red), max(batch.green, acc.green), max(batch.blue, acc.blue)) }
        return minimumValid.red*minimumValid.green*minimumValid.blue
    }

    companion object {
        fun fromGameLine(line: String): Game {
            val idAndPatches = line.splitAndTrim(":")
            val id = idAndPatches[0].substring(5).toInt()
            val batches = idAndPatches[1].splitAndTrim(";").map { batch ->
                val colors = batch.splitAndTrim(",").associate { color -> color.splitToIntStringPair(" ").swap() }
                Batch(colors["red"] ?: 0, colors["green"] ?: 0, colors["blue"] ?: 0)
            }
            return Game(id, batches)
        }
    }
}

class Batch(val red: Int, val green: Int, val blue: Int) {
    fun isValid() =
        red <= RED_LIMIT && green <= GREEN_LIMIT && blue <= BLUE_LIMIT

    companion object {
        private const val GREEN_LIMIT = 13
        private const val RED_LIMIT = 12
        private const val BLUE_LIMIT = 14
    }
}

fun <A, B>Pair<A, B>.swap(): Pair<B, A> =
    second to first
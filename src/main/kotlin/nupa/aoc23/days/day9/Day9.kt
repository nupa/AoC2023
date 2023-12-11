package nupa.aoc23.days.day9

import nupa.aoc23.input.lines
import nupa.aoc23.input.splitToInts

fun day9Part1() {
    val lines = lines("day9/input.txt")

    val valueLines = lines.map { it.splitToInts(" ") }
    val r = valueLines.map {values ->
        var processedList = values
        val ends = listOf<Int>().toMutableList()
        while (processedList.any { it != 0 }) {
            ends.add(processedList.last())
            processedList = processedList.zipWithNext { first, second -> (second - first) }
        }
        ends.sum()
    }
    println(r.sum())
}

fun day9Part2() {
    val lines = lines("day9/input.txt")

    val valueLines = lines.map { it.splitToInts(" ") }
    val r = valueLines.map {values ->
        var processedList = values.reversed()
        val ends = listOf<Int>().toMutableList()
        while (processedList.any { it != 0 }) {
            ends.add(processedList.last())
            processedList = processedList.zipWithNext { first, second -> -(second - first) }
        }
        ends.reversed().fold(0) {acc, value -> value - acc}
    }
    println(r.sum())
}
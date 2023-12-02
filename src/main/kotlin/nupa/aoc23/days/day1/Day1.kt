package nupa.aoc23.days.day1

import nupa.aoc23.input.lines

fun day1Part1() {
    val lines = lines("day1/input.txt")
    val result = lines.sumOf { line -> "${line.first { it.isDigit() }}${line.last { it.isDigit() }}".toInt() }
    println(result)
}

fun day1Part2() {
    val lines = lines("day1/input.txt")

    val written = listOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5,
        "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9).toMap()
    val result = lines.sumOf { line ->
        val first = line.indices.toList().firstNotNullOf { index ->
            if (line[index].isDigit())
                line[index].digitToInt()
            else
                written.keys.find { line.startsWith(it, index) }?.let { written[it] }
        }
        val tails = line.indices.toList().map { line.subSequence(0, line.length - it) }
        val last = tails.firstNotNullOf { tail ->
            if (tail.last().isDigit())
                tail.last().digitToInt()
            else
                written.keys.find { tail.endsWith(it) }?.let { written[it] }
        }
        first * 10 + last
    }
    println(result)
}

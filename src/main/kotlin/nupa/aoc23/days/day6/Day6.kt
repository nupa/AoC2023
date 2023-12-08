package nupa.aoc23.days.day6

import nupa.aoc23.input.lines
import nupa.aoc23.input.splitToInts
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun day6Part1() {
    val lines = lines("day6/input.txt")
    val times = lines[0].removePrefix("Time:").trim().splitToInts("\\s+".toRegex()).map { it.toLong() }
    val distances = lines[1 ].removePrefix("Distance:").trim().splitToInts("\\s+".toRegex()).map { it.toLong() }
    println(times.zip(distances).map { (time, distance) ->
        val s = solutions(distance, time)
        ceil(s.second-1.0).toLong()- floor(s.first+1.0).toLong()+1L
    }.fold(1L) {acc, winningOptions -> acc*winningOptions}
    )
}

fun day6Part2() {
    val solutions = solutions(295173412781210, 45988373)
    println(ceil(solutions.second-1.0).toLong()- floor(solutions.first+1.0).toLong()+1L)
}

fun solutions(distance: Long, time: Long): Pair<Double, Double> {
    val timeAsDouble = time.toDouble()
    val distanceAsDouble = distance.toDouble()
    val first = (timeAsDouble - sqrt(timeAsDouble*timeAsDouble - 4*distanceAsDouble))/2
    val second = (timeAsDouble + sqrt(timeAsDouble*timeAsDouble - 4*distanceAsDouble))/2
    return if (first < second) first to second else second to first
}
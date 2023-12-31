@file:Suppress("unused")

package nupa.aoc23.input

import java.io.File

private val inputDir = "inputs"

fun lines(file: String): List<String> =
    File(inputDir, file).readLines()

fun fileAsString(file: String): String =
    File(inputDir, file).readText()

fun String.splitAndTrim(vararg separators: String, limit: Int = 0):List<String> =
    split(*separators, limit = limit).map { it.trim() }

fun String.splitAndTrim(separator: Regex, limit: Int = 0):List<String> =
    split(separator, limit = limit).map { it.trim() }

fun String.splitToInts(vararg separators: String): List<Int> =
    splitAndTrim(*separators).map { it.toInt() }

fun String.splitToLongs(vararg separators: String): List<Long> =
    splitAndTrim(*separators).map { it.toLong() }

fun String.splitToInts(separator: Regex): List<Int> =
    splitAndTrim(separator).map { it.toInt() }

fun String.splitToPair(separator: String): Pair<String, String> =
    splitAndTrim(separator, limit = 2).let { it[0] to it[1] }

fun String.splitToIntStringPair(separator: String): Pair<Int, String> =
    splitToPair(separator).let { it.first.toInt() to it.second }


@file:Suppress("unused")

package nupa.aoc23.input

import java.io.File

private val inputDir = "inputs"

fun lines(file: String): List<String> =
    File(inputDir, file).readLines()

fun fileAsString(file: String): String =
    File(inputDir, file).readText()

fun splitToLines(text: String): List<String> =
    text.lines()

fun String.splitAndTrim(vararg separators: String, limit: Int = 0):List<String> =
    split(*separators, limit = limit).map { it.trim() }

fun String.splitToPair(separator: String): Pair<String, String> =
    splitAndTrim(separator, limit = 2).let { it[0] to it[1] }

fun String.splitToIntStringPair(separator: String): Pair<Int, String> =
    splitToPair(separator).let { it.first.toInt() to it.second }


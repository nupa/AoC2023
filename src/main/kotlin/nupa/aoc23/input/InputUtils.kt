package nupa.aoc23.input

import java.io.File

private val inputDir = "inputs"

fun lines(file: String): List<String> =
    File(inputDir, file).readLines()

fun fileAsString(file: String): String =
    File(inputDir, file).readText()

fun splitToLines(text: String): List<String> =
    text.lines()
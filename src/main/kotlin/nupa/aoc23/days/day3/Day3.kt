package nupa.aoc23.days.day3

import nupa.aoc23.input.CharacterMap
import kotlin.math.max
import kotlin.math.min

fun day3Part1() {
    val map = CharacterMap.fromFile("day3/input.txt")
    val parts = map.parts()
    println(parts.mapNotNull { if (map.checkForSurroundingSymbol(it.startX, it.startY, it.numberOfDigits)) it.value else null }.sum())
}

fun day3Part2() {
    val map = CharacterMap.fromFile("day3/input.txt")
    val parts = map.parts()

    var sumOfGearRations = 0
    map.walkthrough{value, x, y, _ ->
        if (value == '*') {
            // parts checking could be easily optimized as small part of the parts have to be checked
            val adjacentParts = parts.filter { it.isAdjacentTo(x, y) }
            if (adjacentParts.size == 2) {
                sumOfGearRations += adjacentParts[0].value * adjacentParts[1].value
            }
        }
    }
    println(sumOfGearRations)
}

private fun CharacterMap.parts(): List<PartNumber> {
    val parts = emptyList<PartNumber>().toMutableList()
    walkthrough {value, x, y, end ->
        val latestPart = parts.lastOrNull()
        if (value.isDigit()) {
            val part = if (latestPart == null || latestPart.closed) {
                val newPart = PartNumber(x, y, value)
                parts.add(newPart)
                newPart
            } else {
                latestPart.addDigit(value)
            }
            if (end)
                part.close()
        }else {
            if (latestPart != null && !latestPart.closed) {
                latestPart.close()
            }
        }
    }
    return parts
}

private fun CharacterMap.checkForSurroundingSymbol(startX: Int, startY: Int, length: Int): Boolean {
    if (startY > 0) {
        if (rows[startY-1].substring(max(startX-1, 0), min(startX+length+1, rowLength)).any { it.isSymbol() })
            return true
    }
    if (startX > 0 && rows[startY][startX-1].isSymbol())
        return true
    if (startX + length < rowLength && rows[startY][startX + length].isSymbol())
        return true
    if (startY + 1 < rows.size) {
        if (rows[startY+1].substring(max(startX-1, 0), min(startX+length+1, rowLength)).any { it.isSymbol() })
            return true
    }
    return false
}

private fun Char.isSymbol(): Boolean =
    !isDigit() && this != '.'


private class PartNumber(val startX: Int, val startY: Int, firstDigit: Char) {

    var closed: Boolean = false
        private set

    var value: Int = firstDigit.digitToInt()
        private set

    var numberOfDigits = 1
        private set

    fun addDigit(digit: Char): PartNumber {
        value = (10*value)+digit.digitToInt()
        numberOfDigits++
        return this
    }

    fun close() {
        closed = true
    }

    fun isAdjacentTo(x: Int, y: Int): Boolean {
        // here we know that (x,y) is not overlapping
        return (x >= startX - 1 && x <= startX + numberOfDigits && y >= startY-1 && y <= startY+1)
    }
}

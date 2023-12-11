package nupa.aoc23.days.day8

import nupa.aoc23.input.lines

fun day8Part1() {
    val lines = lines("day8/input.txt")

    val turns = lines[0]
    val places = lines.linesToPlacesMap()

    val path = PathState(places, turns, "AAA", "ZZZ")
    val steps = path.go()
    println(steps)
}

fun day8Part2() {
    val lines = lines("day8/input.txt")

    val turns = lines[0]
    val places = lines.linesToPlacesMap()

    val path = PathState(places, turns, "A", "Z")
    val steps = path.go()
    println(steps)
}

private class Place(val id: String, val leftTarget: String, val rightTarget: String) {
    fun target(turn: Char): String =
        if (turn == 'L') {
            leftTarget
        } else {
            rightTarget
        }

    companion object {
        private val parser = "([A-Z1-9]+) = \\(([A-Z1-9]+), ([A-Z1-9]+)\\)".toRegex()

        fun fromLine(line: String): Place? {
            val matchResult = parser.matchEntire(line) ?: return null
            return Place(matchResult.groupValues[1], matchResult.groupValues[2], matchResult.groupValues[3])
        }
    }
}

private class PathState(private val map: Map<String, Place>,
                        private val turns: String,
                        private val startingIdPrefix: String,
                        private val endingIdPrefix: String) {
    fun go(): Long {
        val startingPlaces: List<Place> = map.values.filter { it.id.endsWith(startingIdPrefix) }
        val stepsForPlaces = startingPlaces.map {
            var steps = 0
            var currentPlace = it
            while (!currentPlace.id.endsWith(endingIdPrefix)) {
                currentPlace = map[currentPlace.target(turns[(steps%turns.length)])]!!
                steps++
            }
            steps.toLong()
        }
        val smallest = stepsForPlaces.min()
        var commonSteps: Long = smallest
        while (stepsForPlaces.any { commonSteps%it != 0L }) {
            commonSteps += smallest
        }
        return commonSteps
    }
}


private fun List<String>.linesToPlacesMap() =
    mapNotNull { Place.fromLine(it) }.associateBy(Place::id)
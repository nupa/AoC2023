package nupa.aoc23.days.day5

import nupa.aoc23.input.lines
import nupa.aoc23.input.splitToLongs
import kotlin.math.min

fun day5Part1() {
    val lines = lines("day5/input.txt")

    val seeds = lines[0].removePrefix("seeds:").trim().splitToLongs(" ")
    val mapChain = mapChain(lines)

    val mappedSeeds = seeds.map {
        mapChain.fold(it) { acc, map ->
            map.map(acc)
        }
    }
    println(mappedSeeds.min())
}

fun day5Part2() {
    val lines = lines("day5/input.txt")

    val seedData = lines[0].removePrefix("seeds:").trim().splitToLongs(" ")
    val seedRanges = listOf<LongRange>().toMutableList()
    for (i in 0..seedData.lastIndex step 2) {
        seedRanges.add(seedData[i]..(seedData[i]+seedData[i+1]))
    }

    val mapChain = mapChain(lines)
    val result = seedRanges.flatMap { seed ->
        mapChain.fold(listOf(seed)) { acc, map ->
            acc.flatMap { map.mapRange(it) }
        }
    }.minBy { it.first }.first
    println(result)
}

private fun mapChain(lines: List<String>): List<Map> {
    val mapChain = listOf<Map>().toMutableList()
    mapChain.add(lines.sectionToMap("seed-to-soil"))
    mapChain.add(lines.sectionToMap("soil-to-fertilizer"))
    mapChain.add(lines.sectionToMap("fertilizer-to-water"))
    mapChain.add(lines.sectionToMap("water-to-light"))
    mapChain.add(lines.sectionToMap("light-to-temperature"))
    mapChain.add(lines.sectionToMap("temperature-to-humidity"))
    mapChain.add(lines.sectionToMap("humidity-to-location"))

    return mapChain
}

private class Map {
    private val mappings = listOf<Mapping>().toMutableList()

    fun addMapping(sourceStart: Long, destinationStart: Long, rangeLength: Long) {
        mappings.add(Mapping(sourceStart..<sourceStart+rangeLength, destinationStart))
    }

    fun map(source: Long): Long {
        val mapping = mappings.find { it.sourceRange.contains(source) }
        return if (mapping != null) {
            mapping.destinationStart + (source - mapping.sourceRange.first)
        } else {
            source
        }
    }

    fun mapRange(range: LongRange): List<LongRange> {
        val destinationRanges = listOf<LongRange>().toMutableList()
        var nextStart = range.first

        while (nextStart < range.last) {
            val nextSourceRange = nextStart .. range.last
            val nextDestinationRange = processRange(nextSourceRange)
            destinationRanges.add(nextDestinationRange)
            nextStart += nextDestinationRange.length() + 1
        }
        return destinationRanges
    }

    private fun processRange(range: LongRange): LongRange {
        val mapping = mappings.find { it.sourceRange.contains(range.first) }
        return if (mapping != null) {
            mapping.destinationStart + (range.first - mapping.sourceRange.first)..min(
                mapping.destinationStart + (range.last - mapping.sourceRange.first),
                mapping.destinationStart + mapping.sourceRange.length()
            )
        } else {
            val nextMapping = mappings.filter { range.contains(it.sourceRange.first) }.minByOrNull { it.sourceRange.first }
            if (nextMapping == null) {
                range
            } else {
                range.first..nextMapping.sourceRange.first
            }
        }
    }

    private class Mapping(val sourceRange: LongRange, val destinationStart: Long)
}

private fun LongRange.length(): Long =
    last - first

private fun List<String>.sectionToMap(mapName: String): Map {
    val sectionStart = indexOfFirst { it.startsWith(mapName) }
    val map = Map()
    subList(sectionStart + 1, size).takeWhile { it.trim().isNotEmpty() }.forEach {
        val mappingData = it.splitToLongs(" ")
        map.addMapping(mappingData[1], mappingData[0], mappingData[2])
    }
    return map
}
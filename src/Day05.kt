fun main() {
    fun part1(input: List<String>): Long =
        input.toAlmanac(
            seedList = input
                .first()
                .replace("seeds: ", "")
                .split(" ")
                .map { it.toLong() }
        ).locations.min()

    fun part2(input: List<String>): Long =
        input
            .let {
                var lowestLocation: Long = Long.MAX_VALUE
                val almanac = input.toAlmanac(listOf())
                it.first()
                    .replace("seeds: ", "")
                    .split(" ")
                    .map { it.toLong() }
                    .chunked(2)
                    .forEach {
                        var seed = it.first()
                        while ( seed < it.first() + it.last() ){
                            almanac.getSeedLocation(seed).let { location -> if (location < lowestLocation) lowestLocation = location }
                            seed++
                        }
                    }
                lowestLocation
            }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

fun List<String>.toAlmanac(seedList: List<Long>): Almanac {
    return (mapIndexed { index, s -> if (s.isBlank()) index else -1 }.filter { it > 0 } + listOf(this.size))
        .zipWithNext { fromIndex, toIndex -> subList(fromIndex + 2, toIndex) }.let {
            Almanac(
                seedList,
                it[0].toAlmanacMaps(),
                it[1].toAlmanacMaps(),
                it[2].toAlmanacMaps(),
                it[3].toAlmanacMaps(),
                it[4].toAlmanacMaps(),
                it[5].toAlmanacMaps(),
                it[6].toAlmanacMaps(),
            )
        }
}

fun List<String>.toAlmanacMaps() = map { it.split(" ").map { it.toLong() }.let { AlmanacMap(it[0], it[1], it[2]) } }

data class Almanac(
    val seeds: List<Long>,
    val seedToSoilMaps: List<AlmanacMap>,
    val soilToFertilizerMaps: List<AlmanacMap>,
    val fertilizerToWaterMaps: List<AlmanacMap>,
    val waterToLightMaps: List<AlmanacMap>,
    val lightToTemperatureMaps: List<AlmanacMap>,
    val temperatureToHumidityMap: List<AlmanacMap>,
    val humidityToLocationMap: List<AlmanacMap>,
) {
    fun Long.toDestination(almanacMaps: List<AlmanacMap>): Long {
        almanacMaps.forEach { almanacMap ->
            almanacMap.destination(this).let {
                if (it > -1) return it
            }
        }
        return this
    }

    fun getSeedLocation(seed: Long) =
        seed.toDestination(seedToSoilMaps)
            .toDestination(soilToFertilizerMaps)
            .toDestination(fertilizerToWaterMaps)
            .toDestination(waterToLightMaps)
            .toDestination(lightToTemperatureMaps)
            .toDestination(temperatureToHumidityMap)
            .toDestination(humidityToLocationMap)

    val locations: List<Long>
        get() = seeds.map { getSeedLocation(it) }
}

data class AlmanacMap(
    val destinationRangeStart: Long,
    val sourceRangeStart: Long,
    val rangeLength: Long
) {
    // Return -1 if source is not in the sourceRange, otherwise returns destination
    fun destination(source: Long): Long =
        if (source >= sourceRangeStart && source < sourceRangeStart + rangeLength ) source - sourceRangeStart + destinationRangeStart else -1
}

import kotlin.math.ceil

fun main() {
    fun part1(input: List<String>): Int = input.map {
        it.whitespaceToComma()
            .replace("^([A-Za-z:,]*)".toRegex(), "")
            .csvToInts()
    }.let {
        it[0].mapIndexed { index, time -> Pair(time, it[1][index]) }
    }.map {
        numberOfPossibilities(it.first.toLong(), it.second.toLong())
    }.productOf { it.toInt() }

    fun part2(input: List<String>): Long = input
        .map {
            it.filter { it.isDigit() }.toLong()
        }.let { numberOfPossibilities(it.first(), it.last()) }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

fun findSmallestSpeedThatBeatsRecord(time: Long, recordDistance: Long): Long {
    var low = 0L
    var high = time / 2
    while (low + 1 < high) {
        val middle = (high - low) / 2 + low
        if (beatsRecord(middle, time - middle, recordDistance))
            high = middle
        else low = middle
    }
    return high
}

fun beatsRecord(speed: Long, travelTime: Long, recordDistance: Long): Boolean =
    speed * travelTime > recordDistance

fun numberOfPossibilities(time: Long, recordDistance: Long) =
    time - (2 * findSmallestSpeedThatBeatsRecord(time, recordDistance)) + 1

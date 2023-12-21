fun main() {
    fun part1(input: List<String>): Int = input.map {
        it.split(" ")
            .let { Pair(it.first().trim('.').replace("\\.+".toRegex(), "."), it.last().split(',').map { it.toInt() }) }
    }.map {
        Pair(it.first, determinePossibleArrangements(it.first.length, it.second))
    }.map {
        it.first.countPossibleMatches(it.second)
    }.sum()

    fun part2(input: List<String>): Long = input.map {
        it.split(" ")
            .let { Pair(it.first().repeat(5).trim('.').replace("\\.+".toRegex(), "."), "${it.last()},".repeat(5).trimEnd(',').split(',').map { it.toInt() }) }
    }.also { it.map { it.println()} }.toString().length.toLong()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 525152L)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}

private fun determinePossibleArrangements(length: Int, groupsOfDamagedSprings: List<Int>): List<String> =
    mutableSetOf<String>().apply {
        var baseArrangement = ""
        groupsOfDamagedSprings.forEachIndexed { index, groupLength ->
            baseArrangement += "#".repeat(groupLength)
            if (index < groupsOfDamagedSprings.size - 1) baseArrangement += "."
        }
        var expandedArrangement = listOf(baseArrangement)
        repeat(length - baseArrangement.length) {
            expandedArrangement = expandedArrangement.map { getArrangementsWithExtraPeriod(it) }.flatten()
        }
        addAll(expandedArrangement)
    }.toList()

private fun getArrangementsWithExtraPeriod(baseArrangement: String): List<String> = mutableListOf<String>().apply {
    add(".$baseArrangement")
    if (baseArrangement.contains('#')) add("$baseArrangement.")
    Regex("#\\.").findAll(baseArrangement.trimEnd('.')).map { it.range.last }.toList().filter { it > 0 }.forEach {
        add(StringBuilder(baseArrangement).apply { insert(it, '.') }.toString())
    }
}

private fun String.countPossibleMatches(possibleArrangements: List<String>) = possibleArrangements.filter {
    this.charsMatchOrQuestionMark(it)
}.size

private fun String.charsMatchOrQuestionMark(other: String): Boolean {
    if (this.length != other.length) return false
    this.forEachIndexed { index, c ->
        if (c != '?' && c != other[index]) return false
    }
    return true
}

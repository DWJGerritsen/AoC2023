fun main() {
    fun part1(input: List<String>): Int = input
        .map {
            it.toGameNumber() to it.toGameMap().toMaxPairs().isValidGame()
        }.sumValidGameNumbers()

    fun part2(input: List<String>): Int = input
        .map { it.toGameMap().toMaxPairs().toPower() }
        .sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

val supposedCubeCount = setOf(
    "red" to 12,
    "green" to 13,
    "blue" to 14,
)
val colours = supposedCubeCount.map { it.first }

fun String.toGameNumber() =
    substringBefore(":").replace("Game ", "").toInt()

private fun String.toGameMap() = substringAfter(":").replace(" ", "").split(';').map {
    it.split(',').map { it.toColourMap() }
}

fun String.toColourMap(): Pair<String, Int> {
    colours.map { colour ->
        if (contains(colour)) return colour to replace(colour, "").toInt()
    }
    error("$this does not contain any of the defined colours $colours")
}

fun List<List<Pair<String, Int>>>.toMaxPairs() = flatten().let {
    colours.map { colour ->
        (it + listOf(colour to 0)).filter { it.first === colour }.maxBy { it.second }
    }
}

fun List<Pair<String, Int>>.isValidGame(): Boolean = all { actual ->
    actual.second <= (supposedCubeCount.find { actual.first === it.first }?.second ?: 0)
}

fun List<Pair<Int, Boolean>>.sumValidGameNumbers(): Int =
    filter { it.second }.sumOf { it.first }

fun List<Pair<String, Int>>.toPower() = productOf { it.second }

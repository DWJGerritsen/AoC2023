import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>, expansionFactor: Int = 2): Long = input.let {
        val emptySpaceRows = it.toEmptySpaceRows()
        val starMap = it.map { it.toList() }
        val emptySpaceColumns = starMap.toEmptySpaceColumns()
        starMap.toStarPositions().toDistancesBetweenStarPairs(expansionFactor, emptySpaceRows, emptySpaceColumns)
            .map { it.toLong() }
    }.sum()

    fun part2(input: List<String>, expansionFactor: Int = 1000000): Long = part1(input, expansionFactor)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)
    check(part2(testInput, 10) == 1030L)
    check(part2(testInput, 100) == 8410L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

fun List<String>.toEmptySpaceRows(): List<Int> = mutableListOf<Int>().also {
    val emptyRow = ".".repeat(first().length)
    forEachIndexed { index, s -> if (s == emptyRow) it.add(index) }
}

fun List<List<Char>>.toEmptySpaceColumns(): List<Int> = mutableListOf<Int>().also {
    for (index in 0 until first().size) {
        if (all { it[index] == '.' }) it.add(index)
    }
}

data class StarPosition(val x: Int, val y: Int) {
    fun distanceTo(
        other: StarPosition,
        expansionFactor: Int,
        emptySpaceRows: List<Int>,
        emptySpaceColumns: List<Int>
    ): Int {
        val minX = min(x, other.x)
        val maxX = max(x, other.x)
        val minY = min(y, other.y)
        val maxY = max(y, other.y)
        return maxX - minX + maxY - minY +
            (expansionFactor - 1) * (
            (minX until maxX).count { it in emptySpaceRows } +
                (minY until maxY).count { it in emptySpaceColumns }

            )
    }
}

fun List<List<Char>>.toStarPositions(): List<StarPosition> = mutableListOf<StarPosition>().also {
    forEachIndexed { x, row ->
        row.forEachIndexed { y, char -> if (char == '#') it.add(StarPosition(x, y)) }
    }
}

fun List<StarPosition>.toDistancesBetweenStarPairs(
    expansionFactor: Int = 1,
    emptySpaceRows: List<Int> = listOf(),
    emptySpaceColumns: List<Int> = listOf()
): List<Int> = mutableListOf<Int>().also {
    for (i in 0 until size - 1) {
        for (j in i + 1 until size) {
            it.add(this[i].distanceTo(this[j], expansionFactor, emptySpaceRows, emptySpaceColumns))
        }
    }
}

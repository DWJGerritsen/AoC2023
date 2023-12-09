fun main() {
    fun part1(input: List<String>): Int = input
        .map { it.split(' ').map { it.toInt() } }.map {
            listOf(it).addDifferenceLists().sumOf {
                it.last()
            }
        }.sum()

    fun part2(input: List<String>): Int = input
        .map { it.split(' ').map { it.toInt() } }.map {
            listOf(it).addDifferenceLists().let {
                var difference = 0
                it.reversed().forEach { difference = it.first() - difference }
                difference
            }
        }.sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

fun List<List<Int>>.addDifferenceLists(): List<List<Int>> =
    last().zipWithNext {l,r -> r-l }.let {differences ->
        if (differences.all { it == 0 }) this
        else this + listOf(differences).addDifferenceLists()
    }

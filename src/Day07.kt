fun main() {
    fun part1(input: List<String>): Int = input.map { it.toHand() }.scoreHands()

    fun part2(input: List<String>): Int = input.map {
        it.toHand(true)
    }.scoreHands()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

fun String.toHand(jIsJoker: Boolean = false) = split(" ").let {
    Hand(
        it.first().toList().map {
            when (it) {
                'A' -> 14
                'K' -> 13
                'Q' -> 12
                'J' -> if (jIsJoker) 0 else 11
                'T' -> 10
                else -> it.digitToInt()
            }
        }, it.last().toInt()
    )
}

fun List<Hand>.scoreHands() =
    map {
        var orderingValue = it.strength().toLong()
        it.cards.forEach { orderingValue = orderingValue * 100 + it }
        Pair(orderingValue, it.bid)
    }.sortedBy {
        it.first
    }.mapIndexed { index, pair ->
        (index + 1) * pair.second
    }.sum()

data class Hand(
    val cards: List<Int>, val bid: Int
) {
    fun strength(): Int {
        var numberOfJokers = 0
        return cards
            .groupingBy { it }
            .eachCount()
            .let {
                if (it.keys.contains(0)) {
                    numberOfJokers = it[0] ?: 0
                    it.filterNot { it.key == 0 }
                } else it
            }.map { it.value }
            .sorted()
            .toMutableList()
            .apply { if (size > 0) this[size - 1] += numberOfJokers else add(numberOfJokers) }
            .let {
                when (it.size) {
                    5 -> 0
                    4 -> 1
                    3 -> if (it.containsAll(listOf(2, 1))) 2 else 3
                    2 -> if (it.containsAll(listOf(3, 2))) 4 else 5
                    1 -> 6
                    else -> error("Malformed Hand with cards $cards.")
                }
            }
    }
}

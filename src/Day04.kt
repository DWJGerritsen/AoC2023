import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int = input.mapToScratchCards().sumOf { it.points }

    fun part2(input: List<String>): Int = input.mapToScratchCards().apply {
        addCopiesForWins()
    }.sumOf { it.copies }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

data class ScratchCard(
    val cardNumber: Int, val winningNumbers: List<Int>, val numbersYouHave: List<Int>, val totalCards: Int
) {
    val points: Int
        get() = 2.0.pow(matchingNumbers - 1).toInt()

    val matchingNumbers
        get() = winningNumbers.intersect(numbersYouHave).size

    val freeScratchCardNumbers: List<Int>
        get() = (cardNumber..cardNumber + matchingNumbers).toList().filter { it in cardNumber + 1..totalCards }

    var copies: Int = 1
}

fun List<ScratchCard>.addCopiesForWins() = forEach {scratchCard ->
    filter { it.cardNumber in scratchCard.freeScratchCardNumbers }.forEach {
        it.copies += scratchCard.copies
     }
}

fun List<String>.mapToScratchCards() = map {
    it.whitespaceToComma().split(':', '|').let {
        ScratchCard(it[0].filter { it.isDigit() }.toInt(), it[1].csvToInts(), it[2].csvToInts(), this.size)
    }
}

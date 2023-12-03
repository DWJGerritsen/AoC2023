fun main() {
    fun part1(input: List<String>): Int = input
        .map { it.numberIndexPairs() to it.symbolIndexes() }.let {
            val partNumbers = mutableListOf<Int>()
            it.forEachIndexed { index, pair ->
                val previousSymbols = if (index > 0) it[index - 1].second else listOf()
                val currentSymbols = pair.second
                val nextSymbols = if (index < it.size - 1) it[index + 1].second else listOf()
                val possibleNeighbouringSymbols = previousSymbols + currentSymbols + nextSymbols
                partNumbers.addAll(
                    pair.first.getNumbersWhen { it.anyIndexOrNeigbourMatches(possibleNeighbouringSymbols) }
                )
            }
            partNumbers
        }
        .sum()

    fun part2(input: List<String>): Int = input
        .map {
            it.numberIndexPairs() to it.gearIndexes()
        }.let {
            val partNumbers = mutableListOf<List<Int>>()
            it.forEachIndexed { index, pair ->
                val previousParts = if (index > 0) it[index - 1].first else listOf()
                val currentParts = pair.first
                val nextParts = if (index < it.size - 1) it[index + 1].first else listOf()
                val possibleNeighbouringParts = previousParts + currentParts + nextParts
                pair.second.forEach { gearIndex ->
                    partNumbers.add(
                        possibleNeighbouringParts.getNumbersWhen { it.anyIndexOrNeigbourMatches(listOf(gearIndex)) }
                    )
                }
            }
            partNumbers
        }.filter {
            it.size == 2
        }.sumOf {
            it.first() * it.last()
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

data class IndexesNumberPair(
    val indexes: List<Int>,
    val number: Int,
) {
    val indexesWithNeighbours: List<Int>
        get() = indexes + listOf(indexes.first() - 1, indexes.last() + 1)
}

fun List<IndexesNumberPair>.getNumbersWhen(condition: (IndexesNumberPair) -> Boolean): List<Int> =
    mutableListOf<Int>().also { list ->
        forEach {
            if (condition(it)) list.add(it.number)
        }
    }

fun IndexesNumberPair.anyIndexOrNeigbourMatches(other: List<Int>) =
    indexesWithNeighbours.any { it in other }

fun String.numberIndexPairs(): List<IndexesNumberPair> {
    val indexLists = mutableListOf<IndexesNumberPair>()
    val indexList = mutableListOf<Int>()
    forEachIndexed { i, c ->
        if (c.isDigit()) {
            indexList.add(i)
            if (i == length - 1)
                indexLists.add(IndexesNumberPair(indexList.toList(), extractNumber(indexList.first(), indexList.last())))
        } else if (indexList.isNotEmpty()) {
            indexLists.add(IndexesNumberPair(indexList.toList(), extractNumber(indexList.first(), indexList.last())))
            indexList.clear()
        }
    }
    return indexLists.toList()
}

fun String.extractNumber(startIndex: Int, endIndex: Int) = substring(startIndex, endIndex + 1).toInt()

fun String.symbolIndexes(): List<Int> {
    val indexList = mutableListOf<Int>()
    forEachIndexed { i, c ->
        if (c != '.' && !c.isDigit()) indexList.add(i)
    }
    return indexList.toList()
}

fun String.gearIndexes(): List<Int> {
    val indexList = mutableListOf<Int>()
    forEachIndexed { i, c ->
        if (c == '*') indexList.add(i)
    }
    return indexList.toList()
}

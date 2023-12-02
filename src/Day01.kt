fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            it.extractDigits().concatenateFirstAndLastChar().toInt()
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return part1(input.map { it.overlappingTextToDigits() })
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

val digitStrings = setOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9",
)

val overlappingDigitStrings = setOf(
    "oneight" to "oneeight",
    "twone" to "twoone",
    "threeight" to "threeeight",
    "fiveight" to "fiveeight",
    "sevenine" to "sevennine",
    "eightwo" to "eighttwo",
    "eighthree" to "eightthree",
    "nineight" to "nineeight",
)

fun String.extractDigits() = filter { it.isDigit() }

fun String.concatenateFirstAndLastChar() = listOf(first(), last()).joinToString("")

fun String.overlappingTextToDigits() = replace(overlappingDigitStrings).replace(digitStrings)

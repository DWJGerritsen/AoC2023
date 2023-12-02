import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Returns a new string obtained by replacing all occurrences of the [replacementPair].first substring in this string
 * with the specified [replacementPair].second string.
 */
fun String.replace(replacementPair: Pair<String, String>): String = replace(replacementPair.first, replacementPair.second)

/**
 * Returns a new string obtained by sequentially replacing all occurrences of the pair.first substring in this
 * string with the specified pair.second string of each pair in the [iterableOfReplacementPairs].
 */
fun String.replace(iterableOfReplacementPairs: Iterable<Pair<String, String>>): String {
    var result = this
    iterableOfReplacementPairs.forEach {
        result = result.replace(it)
    }
    return result
}

/**
 * Returns the product of all values produced by [selector] function applied to each element in the collection.
 */
inline fun <T> Iterable<T>.productOf(selector: (T) -> Int): Int {
    var product = 1
    for (element in this) {
        product *= selector(element)
    }
    return product
}

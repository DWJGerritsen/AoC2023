fun main() {
    fun part1(input: List<String>): Int = input.let {
        Pair(it.first(), it.subList(2, it.size).toNodes().link())
    }.let {
        stepsFromAAAToZZZ(it.first, it.second)
    }

    fun part2(input: List<String>): Long = input.let {
        Pair(it.first(), it.subList(2, it.size).toNodes().link())
    }.let {
        calculateStepsFrom__ATo__Z(it.first, it.second)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 6)
    val testInput2 = readInput("Day08_test2")
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()


}

fun List<String>.toNodes() = map {
    it.filter { it.isLetterOrDigit() }.let { Node(it.substring(0, 3), it.substring(3, 6), it.substring(6)) }
}

fun List<Node>.link(): List<Node> = apply {
    forEach { node ->
        node.left = find { it.name == node.leftName }!!
        node.right = find { it.name == node.rightName }!!
    }
}

fun stepsFromAAAToZZZ(stepOrder: String, nodes: List<Node>): Int {
    var currentNode = nodes.find { it.name == "AAA" }!!
    val targetNode = nodes.find { it.name == "ZZZ" }!!
    var steps = 0
    while (currentNode != targetNode) {
        if (stepOrder[steps.mod(stepOrder.length)] == 'L') currentNode = currentNode.left
        else currentNode = currentNode.right
        steps++
    }
    return steps
}

fun calculateStepsFrom__ATo__Z(stepOrder: String, nodes: List<Node>): Long {
    val nodesEndingInA = nodes.filter { it.name[2] == 'A' }
    var steps = 0L
    val initialStepsLoopLength = mutableListOf<StepCounter>()
    nodesEndingInA.forEach {
        var currentNode = it
        var nodesEndingInZSteps: MutableList<Pair<Node, Int>> = mutableListOf()
        while (nodesEndingInZSteps.size <= 1 || !nodesEndingInZSteps.any { nodeIntPair -> nodeIntPair.first == nodesEndingInZSteps.last().first && (nodesEndingInZSteps.last().second - nodeIntPair.second).mod(stepOrder.length) == 0 && steps.toInt() != nodeIntPair.second } ){
            if (stepOrder[steps.mod(stepOrder.length)] == 'L') currentNode = currentNode.left
            else currentNode = currentNode.right
            steps++
            if(currentNode.name[2] == 'Z') nodesEndingInZSteps.add(Pair(currentNode, steps.toInt()))
        }
        initialStepsLoopLength.add(StepCounter(nodesEndingInZSteps.first().second, nodesEndingInZSteps.last().second-nodesEndingInZSteps.first().second))
        steps = 0
    }
    initialStepsLoopLength.sortBy { it.loopLength }
    while (!initialStepsLoopLength.all { (initialStepsLoopLength.first().steps - it.initialSteps).mod(it.loopLength) == 0 }){
        initialStepsLoopLength.first().step()
    }

    return initialStepsLoopLength.first().steps
}

data class StepCounter(val initialSteps: Int, val loopLength: Int){
    var steps: Long = initialSteps.toLong()

    fun step() { steps += loopLength }
}

data class Node(val name: String, val leftName: String, val rightName: String) {
    var left: Node = this
    var right: Node = this
}

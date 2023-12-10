import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int = input.map { it.toList() }.let {
        PipeMaze(it)
    }.findLargestDistanceFromStart()

    fun part2(input: List<String>): Int = input.map { it.toList() }.let {
        PipeMaze(it)
    }.countEnclosedTiles()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)
    val testInput2 = readInput("Day10_test2")
    check(part2(testInput2) == 10)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

data class PipeMaze(val pipes: List<List<Char>>) {

    private fun findStartPosition() = pipes.indexOfFirst { it.contains('S') }.let {
        Position(it, pipes[it].indexOf('S'))
    }

    private fun findStepOneNodes(): List<Node> = findStartPosition().let { startPosition ->
        val stepOneNodes = mutableListOf<Node>()
        if (startPosition.x > 0) Position(startPosition.x - 1, startPosition.y).run {
            val shape = pipes[x][y]
            if (shape in listOf('|', '7', 'F')) stepOneNodes.add(Node(this, shape, Direction.SOUTH))
        }
        if (startPosition.x < pipes.size - 1) Position(startPosition.x + 1, startPosition.y).run {
            val shape = pipes[x][y]
            if (shape in listOf('|', 'L', 'J')) stepOneNodes.add(Node(this, shape, Direction.NORTH))
        }
        if (startPosition.y > 0) Position(startPosition.x, startPosition.y - 1).run {
            val shape = pipes[x][y]
            if (shape in listOf('-', 'L', 'F')) stepOneNodes.add(Node(this, shape, Direction.EAST))
        }
        if (startPosition.y < pipes.first().size - 1) Position(startPosition.x, startPosition.y + 1).run {
            val shape = pipes[x][y]
            if (shape in listOf('-', 'J', '7')) stepOneNodes.add(Node(this, shape, Direction.WEST))
        }
        stepOneNodes
    }

    fun findLargestDistanceFromStart(): Int {
        var distance = 1
        var nodesAtDistance = findStepOneNodes()
        while (nodesAtDistance.first().position != nodesAtDistance.last().position) {
            nodesAtDistance = nodesAtDistance.map { it.getNextNode(pipes) }
            distance++
        }
        return distance
    }

    private fun determineStartPipe(startPosition: Position): Char {
        val possibleChars = mutableListOf('|', '-', '7', 'F', 'L', 'J')
        if (startPosition.x > 0) Position(startPosition.x - 1, startPosition.y).run {
            val shape = pipes[x][y]
            if (shape in listOf('|', '7', 'F')) possibleChars.removeAll(listOf('-', '7', 'F'))
        }
        if (startPosition.x < pipes.size - 1) Position(startPosition.x + 1, startPosition.y).run {
            val shape = pipes[x][y]
            if (shape in listOf('|', 'L', 'J')) possibleChars.removeAll(listOf('-', 'L', 'J'))
        }
        if (startPosition.y > 0) Position(startPosition.x, startPosition.y - 1).run {
            val shape = pipes[x][y]
            if (shape in listOf('-', 'L', 'F')) possibleChars.removeAll(listOf('|', 'L', 'F'))
        }
        if (startPosition.y < pipes.first().size - 1) Position(startPosition.x, startPosition.y + 1).run {
            val shape = pipes[x][y]
            if (shape in listOf('-', 'J', '7')) possibleChars.removeAll(listOf('|', '7', 'J'))
        }
        return possibleChars.first()
    }

    private fun findStartNode(): Node = findStartPosition().let {
        val startPipe = determineStartPipe(it)
        Node(it, startPipe, Node.getExitDirections(startPipe).first())
    }

    private fun findNodesPartOfTheMaze(): List<Node> {
        val startNode = findStartNode()
        val nodesPartOfMaze = mutableListOf(startNode)
        var nextNode = startNode.getNextNode(pipes)
        while (nextNode.position != startNode.position) {
            nodesPartOfMaze.add(nextNode)
            nextNode = nextNode.getNextNode(pipes)
        }
        return nodesPartOfMaze
    }

    fun countEnclosedTiles(): Int {
        var enclosedTiles = 0
        val mazeNodes = findNodesPartOfTheMaze()
        val mazeTilePositions = mazeNodes.map { it.position }
        val tilePositionsToCheck = mutableListOf<Position>()
        for (x in mazeTilePositions.minBy { it.x }.x + 1 until mazeTilePositions.maxBy { it.x }.x) {
            for (y in mazeTilePositions.minBy { it.y }.y + 1 until mazeTilePositions.maxBy { it.y }.y)
                Position(x, y).let { if (it !in mazeTilePositions) tilePositionsToCheck.add(it) }
        }
        tilePositionsToCheck.forEach {
            enclosedTiles += it.lineUntilX(pipes.size).filter { it in mazeTilePositions }
                .map { position -> mazeNodes.find { it.position == position }!!.shape }.let {
                    abs(
                        (
                            (it.count { it == '7' }
                                + it.count { it == 'L' }
                                - it.count { it == 'J' }
                                - it.count { it == 'F' }
                            ) / 2 + it.count { it == '-' }) % 2
                    )
                }
        }
        return enclosedTiles
    }


    data class Node(val position: Position, val shape: Char, val entranceDirection: Direction) {

        companion object {

            fun getExitDirections(shape: Char) =
                when (shape) {
                    '|' -> listOf(Direction.NORTH, Direction.SOUTH)
                    '-' -> listOf(Direction.EAST, Direction.WEST)
                    'L' -> listOf(Direction.NORTH, Direction.EAST)
                    'J' -> listOf(Direction.NORTH, Direction.WEST)
                    '7' -> listOf(Direction.WEST, Direction.SOUTH)
                    'F' -> listOf(Direction.EAST, Direction.SOUTH)
                    else -> error("Not a valid pipe")
                }
        }

        private fun getExitDirection() = getExitDirections(shape).filterNot { it == entranceDirection }.first()

        private fun getNextPosition() =
            when (getExitDirection()) {
                Direction.NORTH -> Position(position.x - 1, position.y)
                Direction.SOUTH -> Position(position.x + 1, position.y)
                Direction.WEST -> Position(position.x, position.y - 1)
                Direction.EAST -> Position(position.x, position.y + 1)
            }


        fun getNextNode(pipes: List<List<Char>>) = getNextPosition().let { nextPosition ->
            Node(nextPosition, pipes[nextPosition.x][nextPosition.y], getExitDirection().opposite())
        }
    }

    data class Position(val x: Int, val y: Int) {
        fun lineUntilX(nextX: Int): List<Position> = mutableListOf<Position>().apply {
            for (newX in x until nextX) {
                add(Position(newX, y))
            }
        }
    }

    enum class Direction {
        NORTH {
            override fun opposite() = SOUTH
        },
        SOUTH {
            override fun opposite() = NORTH
        },
        WEST {
            override fun opposite() = EAST
        },
        EAST {
            override fun opposite() = WEST
        };

        abstract fun opposite(): Direction
    }
}

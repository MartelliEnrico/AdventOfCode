package me.martelli.adventofcode.year2022

fun main() {
    val input = resource("day08.txt")
    val lines = input.readLines()
    val grid = lines.map { line -> line.toCharArray().map { it.digitToInt() } }
    val matrix = Matrix(grid)
    var hidden = 0
    var maxScenicScore = 0
    matrix.forEachIndexed { x, y, height ->
        if (x == 0 || x == matrix.width - 1 || y == 0 || y == matrix.height - 1) return@forEachIndexed
        val column = matrix.column(x)
        val row = matrix.row(y)
        val top = column.subList(0, y)
        val bottom = column.subList(y + 1, matrix.height)
        val left = row.subList(0, x)
        val right = row.subList(x + 1, matrix.width)
        if (height > top.max() && height > bottom.max() && height > left.max() && height > right.max()) return@forEachIndexed
        hidden++
        fun List<Int>.scenicScore() = this.indexOfFirst { it >= height }.let { if (it == -1) this.size else it + 1 }
        val viewTop = top.reversed().scenicScore()
        val viewBottom = bottom.scenicScore()
        val viewLeft = left.reversed().scenicScore()
        val viewRight = right.scenicScore()
        val scenicScore = viewTop * viewBottom * viewLeft * viewRight
        if (scenicScore > maxScenicScore) maxScenicScore = scenicScore
    }
    val visible = matrix.width * matrix.height - hidden
    println("1) Visible trees: $visible")
    println("2) Max scenic score: $maxScenicScore")
}

private data class Matrix(private val inner: List<List<Int>>) {
    val width = inner[0].size
    val height = inner.size

    fun row(i: Int) = inner[i]

    fun column(i: Int) = inner.map { it[i] }

    fun forEachIndexed(action: (Int, Int, Int) -> Unit) {
        inner.forEachIndexed { y, row ->
            row.forEachIndexed { x, it ->
                action(x, y, it)
            }
        }
    }
}

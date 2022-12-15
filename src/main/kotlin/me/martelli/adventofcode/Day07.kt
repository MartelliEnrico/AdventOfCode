package me.martelli.adventofcode

fun main() {
    val input = resource("day07.txt")
    val fileSystem: Entry.Dir = Entry.Dir()
    lateinit var currentDir: Entry.Dir
    input.forEachLine {
        val components = it.split(" ")
        when (components[0]) {
            "$" -> when (components[1]) {
                "cd" -> currentDir = when (components[2]) {
                    "/" -> fileSystem
                    ".." -> currentDir.parent!!
                    else -> currentDir.content[components[2]] as Entry.Dir
                }
            }
            "dir" -> currentDir.content[components[1]] = Entry.Dir(parent = currentDir)
            else -> currentDir.content[components[1]] = Entry.File(components[0].toInt())
        }
    }
    val dirSizes = fileSystem.allNestedDirs.map { it.size }
    val sumOfDirsAtMost100000 = dirSizes.filter { it <= 100_000 }.sum()
    val sizeToDelete = fileSystem.size - 40_000_000
    val dirToDelete = dirSizes.filter { it >= sizeToDelete }.min()
    println("1) Sum of dirs at most 100000: $sumOfDirsAtMost100000")
    println("2) Size of dir to delete: $dirToDelete")
}

private sealed class Entry {
    abstract val size: Int

    data class File(override val size: Int) : Entry()

    data class Dir(val content: MutableMap<String, Entry> = mutableMapOf(), val parent: Dir? = null) : Entry() {
        override val size: Int
            get() = content.values.sumOf { it.size }

        val allNestedDirs: List<Dir>
            get() = content.values.mapNotNull { it as? Dir }.let { dirs -> dirs + dirs.flatMap { it.allNestedDirs } }
    }
}

package me.martelli.adventofcode

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import kotlin.system.exitProcess
import kotlin.time.measureTimedValue

fun <T> test(year: Int, day: Int, part1: (String) -> T, part2: ((String) -> T)? = null) {
    val text = ClassLoader.getSystemResource("%d/day%02d.yaml".format(year, day)).readText()
    val parts = text.split(Regex("---(\\r\\n|\\r|\\n)"))

    for (index in 0..<parts.size/2) {
        val header = Yaml.default.decodeFromString(Header.serializer(), parts[2 * index])
        val input = parts[2 * index + 1].trim()

        try { if (index == 0) part1(input) } catch (_: Throwable) {} // cold start

        val (result1, t1) = measureTimedValue { part1(input) }
        println("Part 1 result: [$result1] in $t1")

        if (header.part1 != result1.toString()) {
            errorPrintln("Part 1 result not matching: expected [${header.part1}]")
        }

        if (part2 != null) {
            try { if (index == 0) part2(input) } catch (_: Throwable) {} // cold start

            val (result2, t2) = measureTimedValue { part2(input) }
            println("Part 2 result: [$result2] in $t2")

            if (header.part2 != null && header.part2 != result2.toString()) {
                errorPrintln("Part 2 result not matching: expected [${header.part2}]")
            }
        } else {
            println("Skipping part 2")
        }

        println("---")
    }

    println("All good!")
}

private fun errorPrintln(message: String): Nothing {
    System.err.println(message)
    exitProcess(1)
}

@Serializable
data class Header(val part1: String, val part2: String? = null)

package me.martelli.adventofcode

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import kotlin.system.exitProcess
import kotlin.time.measureTimedValue

fun <T> test(year: Int, day: Int, part1: (String) -> T, part2: ((String) -> T)? = null) {
    val text = ClassLoader.getSystemResource("%d/day%02d.yaml".format(year, day)).readText()
    val parts = text.split(Regex("---(\\r\\n|\\r|\\n)"))

    // cold start
    try { part1("") } catch (_: Throwable) {}
    try { if (part2 != null) part2("") } catch (_: Throwable) {}

    for (index in 0..<parts.size/2) {
        val header = Yaml.default.decodeFromString(Header.serializer(), parts[2 * index])
        val input = parts[2 * index + 1].trim()

        print("Part 1 ")

        val (result1, t1) = measureTimedValue { part1(input) }
        println("result: [$result1] in $t1")

        if (header.part1 != result1.toString()) {
            errorPrintln("Part 1 result not matching: expected [${header.part1}]")
        }

        print("Part 2 ")

        if (part2 != null && header.part2 != null) {
            val (result2, t2) = measureTimedValue { part2(input) }
            println("result: [$result2] in $t2")

            if (header.part2 != result2.toString()) {
                errorPrintln("Part 2 result not matching: expected [${header.part2}]")
            }
        } else {
            println("skipped")
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

package me.martelli.adventofcode

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import java.io.File

fun resource(name: String): File = File(ClassLoader.getSystemResource(name).file)

fun <T> test(fileName: String, phase1: (File) -> T, result1: T? = null, phase2: ((File) -> T)? = null, result2: T? = null) {
    val input = resource(fileName)

    val phase1Result = phase1(input)
    println("Phase 1 result: [$phase1Result]")

    if (result1 != null && result1 != phase1Result) {
        error("Phase 1 result not matching: expected [$result1]")
    }

    if (phase2 != null) {
        val phase2Result = phase2(input)
        println("Phase 2 result: [$phase2Result]")

        if (result2 != null && result2 != phase2Result) {
            error("Phase 2 result not matching: expected [$result2]")
        }
    }

    println("All good!")
}

fun <T> test(year: Int, day: Int, part1: (String) -> T, part2: ((String) -> T)? = null) {
    val text = ClassLoader.getSystemResource("%d/day%02d.yaml".format(year, day)).readText()
    val (header, input) = text.split(Regex("---(\\r\\n|\\r|\\n)"))
    val parsed = Yaml.default.decodeFromString(Header.serializer(), header)

    val phase1Result = part1(input).toString()
    println("Phase 1 result: [$phase1Result]")

    if (parsed.part1 != phase1Result) {
        error("Phase 1 result not matching: expected [${parsed.part1}]")
    }

    if (part2 != null) {
        val phase2Result = part2(input).toString()
        println("Phase 2 result: [$phase2Result]")

        if (parsed.part2 != null && parsed.part2 != phase2Result) {
            error("Phase 2 result not matching: expected [${parsed.part2}]")
        }
    }

    println("All good!")
}

@Serializable
data class Header(val part1: String, val part2: String? = null)

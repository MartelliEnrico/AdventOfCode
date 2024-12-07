package me.martelli.adventofcode.year2022

import java.io.File

internal fun resource(name: String) = File(ClassLoader.getSystemResource("2022/$name").file)

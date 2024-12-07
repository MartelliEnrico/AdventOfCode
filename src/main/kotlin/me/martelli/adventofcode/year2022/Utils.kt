package me.martelli.adventofcode.year2022

import java.io.File

fun resource(name: String) = File(ClassLoader.getSystemResource("2022/$name").file)

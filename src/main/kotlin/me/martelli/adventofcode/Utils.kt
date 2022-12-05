package me.martelli.adventofcode

import java.io.File

fun resource(name: String): File = File(ClassLoader.getSystemResource(name).file)

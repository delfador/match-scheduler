package org.ruud

import org.ruud.schedule.Problem
import org.ruud.solver.RandomInitializer

fun main() {
    val problem = Problem(10, 10, 4)
    val schedule = RandomInitializer(problem).create()
    println(schedule)
}

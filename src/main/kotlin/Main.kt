package org.ruud

import org.ruud.schedule.Problem
import org.ruud.schedule.Reporter
import org.ruud.score.HillClimbing

fun main() {
    val problem = Problem(10, 20, 4)
    val solver = HillClimbing(problem)
    val schedule = solver.solve(100_000)
    val reporter = Reporter(problem)
    println(reporter.report(schedule))
}

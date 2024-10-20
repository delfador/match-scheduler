package org.ruud

import org.ruud.schedule.Problem
import org.ruud.schedule.Reporter
import org.ruud.solver.HillClimbing

fun main() {
    print("Number of players > ")
    val numberOfPlayers = readln().toInt()

    print("Number of rounds > ")
    val numberOfRounds = readln().toInt()
    val playersPerMatch = 4

    val problem = Problem(numberOfPlayers, numberOfRounds, playersPerMatch)
    val solver = HillClimbing(problem)
    val schedule = solver.solve(10_000)
    val reporter = Reporter(problem)
    println(reporter.report(schedule))
}

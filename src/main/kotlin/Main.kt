package org.ruud

import org.ruud.schedule.Problem
import org.ruud.schedule.ProblemSolver
import org.ruud.schedule.Reporter

fun main() {
    print("Number of players > ")
    val numberOfPlayers = readln().toInt()

    print("Number of rounds > ")
    val numberOfRounds = readln().toInt()
    val playersPerMatch = 4

    val problem = Problem(numberOfPlayers, numberOfRounds, playersPerMatch)
    val solver = ProblemSolver(problem)
    val schedule = solver.solve()
    val reporter = Reporter(problem)
    println(reporter.report(schedule))
}

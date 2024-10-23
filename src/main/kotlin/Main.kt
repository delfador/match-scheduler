package org.ruud

import org.ruud.schedule.Problem
import org.ruud.schedule.RandomInitializer
import org.ruud.schedule.Reporter
import org.ruud.schedule.ScheduleSolution
import org.ruud.score.BasicScorerFactory
import org.ruud.solver.HillClimbing

fun main() {
    print("Number of players > ")
    val numberOfPlayers = readln().toInt()
    // val numberOfPlayers = 10

    print("Number of rounds > ")
    val numberOfRounds = readln().toInt()
    // val numberOfRounds = 20
    val playersPerMatch = 4

    val problem = Problem(numberOfPlayers, numberOfRounds, playersPerMatch)
    val initialSchedule = RandomInitializer(problem).create()
    val initialSolution =
        ScheduleSolution(
            problem = problem,
            schedule = initialSchedule,
            scorerFactory = BasicScorerFactory(problem),
        )
    val solution = HillClimbing<ScheduleSolution>().solve(initialSolution, maxIter = 10_000)
    val schedule = solution.schedule

    val reporter = Reporter(problem)
    println(reporter.report(schedule))
    println(solution.detailScore())
}

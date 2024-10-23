package org.ruud

import org.ruud.schedule.AnnealSolution
import org.ruud.schedule.Move
import org.ruud.schedule.MoveSelector
import org.ruud.schedule.Problem
import org.ruud.schedule.RandomInitializer
import org.ruud.schedule.Reporter
import org.ruud.schedule.Schedule
import org.ruud.score.BasicScorerFactory
import org.ruud.solver.Anneal
import schedule.MoveType

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
        AnnealSolution(
            problem = problem,
            schedule = initialSchedule,
            scorerFactory = BasicScorerFactory(problem),
            moveSelector =
                MoveSelector(
                    listOf(
                        5.0 to MoveType.SwapPlayer,
                        1.0 to MoveType.RotatePlayers,
                        1.0 to MoveType.SwapRound,
                    ),
                ),
        )
    val solution = Anneal<Schedule, Move>().solve(initialSolution, 100.0, 0.99, 10_000)
    val schedule = solution.getState()

    val reporter = Reporter(problem)
    println(reporter.report(schedule))
    println(solution.detailScore())
}

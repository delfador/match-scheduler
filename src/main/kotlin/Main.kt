package org.ruud

import org.ruud.schedule.Problem
import org.ruud.schedule.RandomInitializer
import org.ruud.schedule.Reporter
import org.ruud.schedule.Schedule
import org.ruud.schedule.ScheduleSolution
import org.ruud.schedule.move.Move
import org.ruud.schedule.move.MoveSelector
import org.ruud.schedule.move.MoveType
import org.ruud.schedule.score.BasicScorerFactory
import org.ruud.solver.Anneal

fun main() {
    print("Number of players > ")
    val numberOfPlayers = readln().toInt()
    // val numberOfPlayers = 10

    print("Number of rounds > ")
    val numberOfRounds = readln().toInt()
    // val numberOfRounds = 20
    val playersPerMatch = 4

    val problem = Problem(numberOfPlayers, numberOfRounds, playersPerMatch)
    val initialSolution =
        ScheduleSolution(
            problem = problem,
            schedule = RandomInitializer(problem).create(),
            scorerFactory = BasicScorerFactory(problem),
            moveSelector =
                MoveSelector(
                    listOf(
                        10.0 to MoveType.SwapPlayer,
                        // 1.0 to MoveType.RotatePlayers,
                        5.0 to MoveType.SwapRound,
                    ),
                ),
        )
    val solution =
        Anneal<Schedule, Move>().solve(
            initialSolution = initialSolution,
            initialTemperature = 10_000.0,
            coolingRate = 0.999,
            maxIter = 100_000,
        )
    val schedule = solution.getState()

    val reporter = Reporter(problem)
    println(reporter.report(schedule))
    println(solution.detailScore())
}

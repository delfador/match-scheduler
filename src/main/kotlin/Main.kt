package org.ruud

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.ruud.schedule.Problem
import org.ruud.schedule.Reporter
import org.ruud.schedule.Schedule
import org.ruud.schedule.ScheduleSolution
import org.ruud.schedule.move.Move
import org.ruud.schedule.move.MoveSelector
import org.ruud.schedule.move.MoveType
import org.ruud.schedule.score.BasicScorerFactory
import org.ruud.solver.Anneal
import solver.Solution

fun main() {
    print("Number of players > ")
    val numberOfPlayers = readln().toInt()

    print("Number of rounds > ")
    val numberOfRounds = readln().toInt()
    val playersPerMatch = 4

    val problem = Problem(numberOfPlayers, numberOfRounds, playersPerMatch)

    val scorerFactory =
        BasicScorerFactory(
            problem = problem,
            totalMatchesPlayedWeight = 20.0,
            playingStreakWeight = 10.0,
            pairFrequencyWeight = 2.0,
        )

    val moveSelector =
        MoveSelector(
            listOf(
                10.0 to MoveType.SwapPlayer,
                // 1.0 to MoveType.RotatePlayers,
                5.0 to MoveType.SwapRound,
            ),
        )

    val solutions =
        runBlocking(Dispatchers.Default) {
            List(Runtime.getRuntime().availableProcessors()) {
                async { optimizeSchedule(problem, scorerFactory, moveSelector) }
            }.map { it.await() }
        }

    println(solutions.map { it.score() })

    val bestSolution = solutions.minBy { it.score() }
    val schedule = bestSolution.getState()
    val reporter = Reporter(problem)
    println(reporter.report(schedule))
    println(bestSolution.detailScore())
}

private fun optimizeSchedule(
    problem: Problem,
    scorerFactory: BasicScorerFactory,
    moveSelector: MoveSelector,
): Solution<Schedule, Move> {
    val initialSolution = ScheduleSolution(problem, Schedule.random(problem), scorerFactory, moveSelector)
    return Anneal<Schedule, Move>().solve(
        initialSolution = initialSolution,
        initialTemperature = 10_000.0,
        coolingRate = 0.9999,
        maxIter = 100_000,
    )
}

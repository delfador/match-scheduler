package org.ruud

import org.ruud.schedule.Problem
import org.ruud.schedule.ProblemSolver
import org.ruud.schedule.Reporter
import org.ruud.schedule.move.MoveWeights
import org.ruud.schedule.score.ScoringWeights
import org.ruud.solver.AnnealOptions

fun main() {
    print("Number of players > ")
    val numberOfPlayers = readln().toInt()

    print("Number of rounds > ")
    val numberOfRounds = readln().toInt()
    val playersPerMatch = 4

    val problem = Problem(numberOfPlayers, numberOfRounds, playersPerMatch)

    val scoringWeights = ScoringWeights()

    val annealOptions =
        AnnealOptions.tunedFor(
            highDelta = scoringWeights.maximumWeight(),
            lowDelta = scoringWeights.minimumWeight(),
        )

    val solver =
        ProblemSolver(
            problem,
            scoringWeights = scoringWeights,
            moveWeights = MoveWeights(),
            annealOptions = annealOptions,
        )

    val result = solver.solve()
    val schedule = result.schedule
    val reporter = Reporter(problem)
    println(reporter.report(schedule))
    println(result.detailScore)
}

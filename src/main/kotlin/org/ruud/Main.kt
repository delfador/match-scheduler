package org.ruud

import org.ruud.schedule.Problem
import org.ruud.schedule.ProblemSolver
import org.ruud.schedule.Reporter
import org.ruud.schedule.move.MoveWeights
import org.ruud.schedule.score.ScoringWeights
import org.ruud.solver.AnnealOptions
import java.io.File

fun main() {
    print("Number of players > ")
    val numberOfPlayers = readln().toInt()

    print("Number of rounds > ")
    val numberOfRounds = readln().toInt()
    val playersPerMatch = 4

    println("Solving...")

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
    File("schedule.csv").writeText(result.schedule.toCsv())

    val reporter = Reporter(problem)
    val output =
        buildString {
            appendLine(reporter.report(result.schedule))
            appendLine(result.detailScore)
        }
    File("schedule-details.txt").writeText(output)
    println(output)
}

package org.ruud

import org.ruud.schedule.ParallelSolver
import org.ruud.schedule.Problem
import org.ruud.schedule.ProblemSolver
import org.ruud.schedule.Reporter
import org.ruud.solver.AnnealOptions
import java.io.File
import kotlin.random.Random

private const val OPTIONS_FILENAME = "options.json"

fun main() {
    println("\nMATCH SCHEDULER")
    val options =
        Options
            .fromFileOrNull(File(OPTIONS_FILENAME)) ?: Options()
            .withPlayersAndRoundFromUserInput()

    println("Solving...")

    val problem = Problem(options.numberOfPlayers, options.numberOfRounds, options.playersPerMatch)

    val solver =
        ProblemSolver(
            problem = problem,
            scoringWeights = options.scoringWeights,
            moveWeights = options.moveWeights,
            annealOptions =
                AnnealOptions.tunedFor(
                    highDelta = 2 * options.scoringWeights.maximumWeight(),
                    lowDelta = options.scoringWeights.minimumWeight(),
                    maxIter = options.maxIter,
                ),
        )

    val random = options.randomSeed?.let { Random(it) } ?: Random

    val result = ParallelSolver(solver, options.parallelSolvers).solve(random)

    val reporter = Reporter(problem)
    val output =
        buildString {
            appendLine()
            appendLine(reporter.report(result.schedule))
            appendLine(result.detailScore)
        }

    println(output)
    File(options.scheduleCsv).writeText(result.schedule.toCsv())
    File(options.scheduleDetails).writeText(output)
}

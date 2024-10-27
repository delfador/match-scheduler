package org.ruud

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.ruud.schedule.Problem
import org.ruud.schedule.ProblemSolver
import org.ruud.schedule.Reporter
import org.ruud.solver.AnnealOptions
import java.io.File

private val json = Json { encodeDefaults = true }

fun main() {
    val optionsFile = File("options.json")
    val defaultOptions =
        if (optionsFile.exists()) {
            try {
                json.decodeFromString<Options>(
                    optionsFile.readText(Charsets.UTF_8),
                )
            } catch (e: SerializationException) {
                println("Cannot decode options file.")
                Options()
            } catch (e: IllegalArgumentException) {
                println("Options file is not valid.")
                Options()
            }
        } else {
            Options()
        }

    print("Number of players (${defaultOptions.numberOfPlayers}) > ")
    val numberOfPlayers = readln().toIntOrNull() ?: defaultOptions.numberOfPlayers

    print("Number of rounds (${defaultOptions.numberOfRounds}) > ")
    val numberOfRounds = readln().toIntOrNull() ?: defaultOptions.numberOfRounds

    val options =
        defaultOptions.copy(
            numberOfPlayers = numberOfPlayers,
            numberOfRounds = numberOfRounds,
        )

    println("Solving...")

    val problem = Problem(options.numberOfPlayers, options.numberOfRounds, options.playersPerMatch)

    val scoringWeights = options.scoringWeights

    val annealOptions =
        AnnealOptions.tunedFor(
            highDelta = scoringWeights.maximumWeight(),
            lowDelta = scoringWeights.minimumWeight(),
            maxIter = options.maxIter,
            parallelSolvers = options.parallelSolvers,
        )

    val solver =
        ProblemSolver(
            problem,
            scoringWeights = scoringWeights,
            moveWeights = options.moveWeights,
            annealOptions = annealOptions,
        )

    val result = solver.solve()
    File(options.scheduleCsv).writeText(result.schedule.toCsv())

    val reporter = Reporter(problem)
    val output =
        buildString {
            appendLine(reporter.report(result.schedule))
            appendLine(result.detailScore)
        }
    File(options.scheduleDetails).writeText(output)
    println(output)
}

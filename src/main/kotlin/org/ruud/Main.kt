package org.ruud

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.ruud.schedule.Problem
import org.ruud.schedule.ProblemSolver
import org.ruud.schedule.Reporter
import org.ruud.solver.AnnealOptions
import java.io.File
import kotlin.random.Random

private const val OPTIONS_FILENAME = "options.json"
private val json = Json { encodeDefaults = true }

fun main() {
    println("\nMATCH SCHEDULER")
    val options = getOptions()
    println("Solving...")

    val problem = Problem(options.numberOfPlayers, options.numberOfRounds, options.playersPerMatch)

    val scoringWeights = options.scoringWeights

    val annealOptions =
        AnnealOptions.tunedFor(
            highDelta = scoringWeights.maximumWeight(),
            lowDelta = scoringWeights.minimumWeight(),
            maxIter = options.maxIter,
        )

    val random = options.randomSeed?.let { Random(it) } ?: Random

    val solver =
        ProblemSolver(
            scoringWeights = scoringWeights,
            moveWeights = options.moveWeights,
            annealOptions = annealOptions,
        )

    val result = solver.solve(problem, random)

    val reporter = Reporter(problem)
    val output =
        buildString {
            appendLine()
            appendLine(reporter.report(result.schedule))
            appendLine(result.detailScore)
        }

    println(output)
    // println(result.allScores)
    File(options.scheduleCsv).writeText(result.schedule.toCsv())
    File(options.scheduleDetails).writeText(output)
}

private fun getOptions(): Options {
    val optionsFile = File(OPTIONS_FILENAME)

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

    return defaultOptions.copy(
        numberOfPlayers = numberOfPlayers,
        numberOfRounds = numberOfRounds,
    )
}

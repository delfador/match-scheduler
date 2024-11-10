package org.ruud

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.ruud.schedule.move.MoveWeights
import org.ruud.schedule.score.ScoringWeights
import java.io.File
import java.io.FileNotFoundException

private val json = Json { encodeDefaults = true }

@Serializable
data class Options(
    val numberOfPlayers: Int = 10,
    val numberOfRounds: Int = 20,
    val playersPerMatch: Int = 4,
    val scoringWeights: ScoringWeights = ScoringWeights(),
    val moveWeights: MoveWeights = MoveWeights(),
    val maxIter: Int = 100_000,
    val parallelSolvers: Int = Runtime.getRuntime().availableProcessors(),
    val scheduleCsv: String = "schedule.csv",
    val scheduleDetails: String = "schedule-details.txt",
    val randomSeed: Int? = null,
) {
    fun withPlayersAndRoundFromUserInput(): Options {
        print("Number of players ($numberOfPlayers) > ")
        val numberOfPlayers = readln().toIntOrNull() ?: numberOfPlayers

        print("Number of rounds ($numberOfRounds) > ")
        val numberOfRounds = readln().toIntOrNull() ?: numberOfRounds

        return this.copy(
            numberOfPlayers = numberOfPlayers,
            numberOfRounds = numberOfRounds,
        )
    }

    companion object {
        fun fromFileOrNull(file: File): Options? {
            try {
                return json
                    .decodeFromString<Options>(
                        file.readText(Charsets.UTF_8),
                    )
            } catch (e: FileNotFoundException) {
                println("Cannot read file: ${file.name}.")
            } catch (e: SerializationException) {
                println("Cannot decode options file: ${file.name}.")
            } catch (e: IllegalArgumentException) {
                println("Options file is not valid: ${file.name}.")
            }

            return null
        }
    }
}

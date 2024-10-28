package org.ruud

import kotlinx.serialization.Serializable
import org.ruud.schedule.move.MoveWeights
import org.ruud.schedule.score.ScoringWeights

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
)

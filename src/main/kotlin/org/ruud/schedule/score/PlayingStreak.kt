package org.ruud.schedule.score

import org.ruud.schedule.Schedule

class PlayingStreak(
    private val schedule: Schedule,
) {
    fun playingStreaksByPlayer(): Map<Int, List<Int>> {
        val idleRoundsByPlayer =
            schedule.rounds
                .flatMapIndexed { index, round ->
                    round.idle.map { it to index }
                }.groupBy { (player, _) -> player }
                .mapValues { (_, playerIdlePairs) ->
                    playerIdlePairs.map { it.second }
                }

        return idleRoundsByPlayer.mapValues { (_, idleRounds) ->
            idleRounds.zipWithNext { a, b -> b - a - 1 }
        }
    }

    private fun allStreaks() = playingStreaksByPlayer().values.flatten()

    fun scorer(range: IntRange): Scorer =
        ObservationScorer(observationScore = inRange(range), label = "Streaks in $range", observations = ::allStreaks)
}

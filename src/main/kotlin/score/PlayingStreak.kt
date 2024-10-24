package org.ruud.score

import org.ruud.schedule.Schedule

class PlayingStreak(
    private val schedule: Schedule,
    private val targetStreak: Int,
    override val label: String = "Playing streak in $targetStreak..${targetStreak + 2}",
) : Scorer {
    val evaluator = inRange(targetStreak..targetStreak + 2)

    override fun invoke(): Double {
        val playingStreaksByPlayer = playingStreaksByPlayer(schedule)

        val streakShortages =
            playingStreaksByPlayer
                .values
                .flatten()
                .map { evaluator(it) }

        return streakShortages.sum().toDouble()
    }

    companion object {
        fun playingStreaksByPlayer(schedule: Schedule): Map<Int, List<Int>> {
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
    }
}

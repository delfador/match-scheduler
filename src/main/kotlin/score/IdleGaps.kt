package org.ruud.score

import org.ruud.schedule.Schedule

class IdleGaps(
    private val schedule: Schedule,
    private val allowedGap: Int,
    override val label: String = "Idle Gaps",
) : Scorer {
    override fun invoke(): Double {
        val idleRoundsByPlayer =
            schedule.rounds
                .flatMapIndexed { index, round ->
                    round.idle.map { it to index }
                }.groupBy { (player, _) -> player }
                .mapValues { (_, playerIdlePairs) ->
                    playerIdlePairs.map { it.second }
                }

        val idleGaps =
            idleRoundsByPlayer.values.flatMap { indices ->
                indices.zipWithNext { a, b -> b - a }
            }
        val idleGapExcess = idleGaps.map { maxOf(allowedGap - it, 0) }

        return idleGapExcess.sum().toDouble()
    }

    private fun minimumGap(indices: List<Int>): Int = indices.zipWithNext { a, b -> b - a }.minOrNull() ?: Int.MAX_VALUE
}

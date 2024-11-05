package org.ruud.schedule.move

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class MoveWeights(
    val swapPlayerWeight: Double = 10.0,
    val rotatePlayersWeight: Double = 0.0,
    val swapRoundWeight: Double = 5.0,
    val swapAdjacentRoundWeight: Double = 0.0,
) {
    fun toMoveSelector(random: Random = Random): MoveSelector {
        val moveWeights =
            mapOf(
                MoveType.SwapPlayer to swapPlayerWeight,
                MoveType.RotatePlayers to rotatePlayersWeight,
                MoveType.SwapRound to swapRoundWeight,
                MoveType.SwapAdjacentRound to swapAdjacentRoundWeight,
            ).filterValues { it > 0.0 }

        return MoveSelector(moveWeights, random)
    }
}

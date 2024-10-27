package org.ruud.schedule.move

import kotlinx.serialization.Serializable

@Serializable
data class MoveWeights(
    val swapPlayerWeight: Double = 10.0,
    val rotatePlayersWeight: Double = 0.0,
    val swapRoundWeight: Double = 5.0,
) {
    fun toMoveSelector(): MoveSelector {
        val moveWeights =
            mapOf(
                MoveType.SwapPlayer to swapPlayerWeight,
                MoveType.RotatePlayers to rotatePlayersWeight,
                MoveType.SwapRound to swapRoundWeight,
            ).filterValues { it > 0.0 }

        return MoveSelector(moveWeights)
    }
}

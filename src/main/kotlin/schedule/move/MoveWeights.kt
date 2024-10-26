package schedule.move

import org.ruud.schedule.move.MoveSelector
import org.ruud.schedule.move.MoveType

data class MoveWeights(
    val swapPlayerWeight: Double,
    val rotatePlayersWeight: Double,
    val swapRoundWeight: Double,
) {
    fun toMoveSelector(): MoveSelector {
        val moveWeights =
            mapOf(
                MoveType.SwapPlayer to swapPlayerWeight,
                MoveType.RotatePlayers to rotatePlayersWeight,
                MoveType.SwapRound to swapRoundWeight,
            )
        return MoveSelector(moveWeights)
    }
}

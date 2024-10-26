package schedule.move

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import org.ruud.schedule.Problem
import org.ruud.schedule.Schedule
import org.ruud.schedule.move.Move
import org.ruud.schedule.move.MoveSelector
import org.ruud.schedule.move.MoveType
import org.ruud.schedule.move.RotatePlayers
import org.ruud.schedule.move.SwapPlayerPositions
import org.ruud.schedule.move.SwapRounds

class MoveSelectorTest {
    @Test
    fun `should yield move fractions close to weight fractions`() {
        val problem = Problem(6, 2, 4)
        val schedule = Schedule.random(problem)
        val moveSelector =
            MoveSelector(
                mapOf(
                    MoveType.SwapPlayer to 6.0,
                    MoveType.RotatePlayers to 1.0,
                    MoveType.SwapRound to 3.0,
                ),
            )

        val numberOfMoves = 1_000
        val movesFractions =
            List(numberOfMoves) { moveSelector.select(schedule) }
                .groupingBy { it.moveType() }
                .eachCount()
                .mapValues { (_, count) -> count / numberOfMoves.toDouble() }

        assertThat(movesFractions[MoveType.SwapPlayer]).isCloseTo(0.6, within(0.03))
        assertThat(movesFractions[MoveType.RotatePlayers]).isCloseTo(0.1, within(0.03))
        assertThat(movesFractions[MoveType.SwapRound]).isCloseTo(0.3, within(0.03))
    }

    private fun Move.moveType(): MoveType =
        when (this) {
            is SwapPlayerPositions -> MoveType.SwapPlayer
            is RotatePlayers -> MoveType.RotatePlayers
            is SwapRounds -> MoveType.SwapRound
            else -> throw NotImplementedError("Unknown move type: $this")
        }
}

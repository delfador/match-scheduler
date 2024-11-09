package org.ruud.schedule.move

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import org.ruud.schedule.Problem
import org.ruud.schedule.Schedule

class MoveSelectorTest {
    @Test
    fun `should yield move fractions close to weight fractions`() {
        val problem = Problem(6, 2, 4)
        val schedule = Schedule.random(problem)
        val moveWeights =
            MoveWeights(
                swapPlayerWeight = 6.0,
                rotatePlayersWeight = 1.0,
                swapRoundWeight = 3.0,
            )
        val moveSelector = moveWeights.toMoveSelector()

        val numberOfMoves = 10_000
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
            is SwapPlayerPositions -> org.ruud.schedule.move.MoveType.SwapPlayer
            is RotatePlayers -> org.ruud.schedule.move.MoveType.RotatePlayers
            is SwapRounds -> org.ruud.schedule.move.MoveType.SwapRound
            else -> throw NotImplementedError("Unknown move type: $this")
        }
}

package org.ruud.schedule.move

import org.ruud.schedule.Schedule

enum class MoveType {
    SwapPlayer,
    RotatePlayers,
    SwapRound,
    ;

    companion object {
        fun create(
            schedule: Schedule,
            moveType: MoveType,
        ): Move =
            when (moveType) {
                SwapPlayer ->
                    SwapPlayerPositions.random(
                        numberOfPlayers = schedule.numerOfPlayers,
                        numberOfRounds = schedule.numberOfRounds,
                        playersPerMatch = schedule.playersPerMatch,
                    )

                RotatePlayers ->
                    org.ruud.schedule.move.RotatePlayers.random(
                        numberOfRounds = schedule.numerOfPlayers,
                        playersPerMatch = schedule.playersPerMatch,
                    )

                SwapRound -> SwapRounds.random(numberOfRounds = schedule.numberOfRounds)
            }
    }
}

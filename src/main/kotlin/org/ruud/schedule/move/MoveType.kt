package org.ruud.schedule.move

import org.ruud.schedule.Schedule
import kotlin.random.Random

enum class MoveType {
    SwapPlayer,
    RotatePlayers,
    SwapRound,
    ;

    companion object {
        fun create(
            schedule: Schedule,
            moveType: MoveType,
            random: Random = Random,
        ): Move =
            when (moveType) {
                SwapPlayer ->
                    SwapPlayerPositions.random(
                        numberOfPlayers = schedule.numerOfPlayers,
                        numberOfRounds = schedule.numberOfRounds,
                        playersPerMatch = schedule.playersPerMatch,
                        random = random,
                    )

                RotatePlayers ->
                    org.ruud.schedule.move.RotatePlayers.random(
                        numberOfRounds = schedule.numerOfPlayers,
                        playersPerMatch = schedule.playersPerMatch,
                        random = random,
                    )

                SwapRound -> SwapRounds.random(numberOfRounds = schedule.numberOfRounds, random = random)
            }
    }
}

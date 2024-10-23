package schedule

import org.ruud.schedule.Move
import org.ruud.schedule.Schedule
import org.ruud.schedule.SwapPlayerPositions
import org.ruud.schedule.SwapRounds

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
                    org.ruud.schedule.RotatePlayers.random(
                        numberOfRounds = schedule.numerOfPlayers,
                        playersPerMatch = schedule.playersPerMatch,
                    )

                SwapRound -> SwapRounds.random(numberOfRounds = schedule.numberOfRounds)
            }
    }
}

package org.ruud.schedule.move

import org.ruud.schedule.Schedule
import kotlin.random.Random

class RotatePlayers(
    val round: Int,
    val steps: Int,
) : Move {
    override fun execute(schedule: Schedule) {
        schedule.rounds[round].rotate(steps)
    }

    override fun undo(schedule: Schedule) {
        schedule.rounds[round].rotate(-steps)
    }

    companion object {
        fun random(
            numberOfRounds: Int,
            playersPerMatch: Int,
            random: Random = Random,
        ): RotatePlayers {
            val roundIndex = random.nextInt(numberOfRounds)
            val steps = random.nextInt(1, playersPerMatch - 1)
            return RotatePlayers(roundIndex, steps)
        }
    }
}

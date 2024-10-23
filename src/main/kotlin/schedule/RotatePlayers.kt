package org.ruud.schedule

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
        ): RotatePlayers {
            val roundIndex = Random.nextInt(numberOfRounds)
            val steps = Random.nextInt(1, playersPerMatch - 1)
            return RotatePlayers(roundIndex, steps)
        }
    }
}

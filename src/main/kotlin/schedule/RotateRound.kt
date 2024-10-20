package org.ruud.schedule

import kotlin.random.Random

class RotateRound(
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
        fun random(schedule: Schedule): RotateRound {
            val roundIndex = Random.nextInt(schedule.numerOfPlayers)
            val steps = Random.nextInt(1, schedule.playersPerMatch - 1)
            return RotateRound(roundIndex, steps)
        }
    }
}

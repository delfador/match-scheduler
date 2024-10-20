package org.ruud.schedule

import kotlin.random.Random

class SwapRounds(
    private val round1: Int,
    private val round2: Int,
) : Move {
    override fun execute(schedule: Schedule) {
        schedule.swapRounds(round1, round2)
    }

    override fun undo(schedule: Schedule) {
        schedule.swapRounds(round2, round1)
    }

    companion object {
        fun random(schedule: Schedule): SwapRounds {
            val round1 = Random.nextInt(schedule.numberOfRounds)
            var round2 = Random.nextInt(schedule.numberOfRounds)

            while (round2 == round1) {
                round2 = Random.nextInt(schedule.numberOfRounds)
            }

            return SwapRounds(round1, round2)
        }
    }
}

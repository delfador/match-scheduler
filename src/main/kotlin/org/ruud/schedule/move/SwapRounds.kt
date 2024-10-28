package org.ruud.schedule.move

import org.ruud.schedule.Schedule
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
        fun random(
            numberOfRounds: Int,
            random: Random = Random,
        ): SwapRounds {
            val round1 = random.nextInt(numberOfRounds)
            var round2 = random.nextInt(numberOfRounds)

            while (round2 == round1) {
                round2 = random.nextInt(numberOfRounds)
            }

            return SwapRounds(round1, round2)
        }
    }
}

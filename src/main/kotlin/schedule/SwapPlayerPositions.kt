package org.ruud.schedule

import kotlin.random.Random

class SwapPlayerPositions(
    val roundIndex: Int,
    val index1: Int,
    val index2: Int,
) : Move {
    override fun execute(schedule: Schedule) {
        schedule.rounds[roundIndex].swapPositions(index1, index2)
    }

    override fun undo(schedule: Schedule) {
        schedule.rounds[roundIndex].swapPositions(index2, index1)
    }

    companion object {
        fun random(schedule: Schedule): SwapPlayerPositions {
            val roundIndex = Random.nextInt(schedule.numberOfRounds)
            val index1 = Random.nextInt(schedule.numerOfPlayers)
            var index2 = Random.nextInt(schedule.numerOfPlayers)

            while (inSameMatch(index1, index2, schedule.playersPerMatch)) {
                index2 = Random.nextInt(schedule.numerOfPlayers)
            }
            return SwapPlayerPositions(roundIndex, index1, index2)
        }

        private fun inSameMatch(
            index1: Int,
            index2: Int,
            playersPerMatch: Int,
        ): Boolean = index1 / playersPerMatch == index2 / playersPerMatch
    }
}

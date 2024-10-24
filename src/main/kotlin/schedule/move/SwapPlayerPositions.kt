package org.ruud.schedule.move

import org.ruud.schedule.Schedule
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
        fun random(
            numberOfPlayers: Int,
            numberOfRounds: Int,
            playersPerMatch: Int,
        ): SwapPlayerPositions {
            val roundIndex = Random.nextInt(numberOfRounds)
            val index1 = Random.nextInt(numberOfPlayers)
            var index2 = Random.nextInt(numberOfPlayers)

            while (inSameMatch(index1, index2, playersPerMatch)) {
                index2 = Random.nextInt(numberOfPlayers)
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
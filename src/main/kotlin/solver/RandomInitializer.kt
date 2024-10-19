package org.ruud.solver

import org.ruud.schedule.Problem
import org.ruud.schedule.Round
import org.ruud.schedule.Schedule

class RandomInitializer(
    private val problem: Problem,
) : ScheduleInitializer {
    override fun create(): Schedule {
        with(problem) {
            val rounds =
                List(numberOfRounds) {
                    Round.random(numberOfPlayers = numberOfPlayers, playersPerMatch = playersPerMatch)
                }
            return Schedule(rounds)
        }
    }
}

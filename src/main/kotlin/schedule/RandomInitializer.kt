package org.ruud.schedule

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

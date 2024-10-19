package org.ruud.score

import org.ruud.schedule.Round
import org.ruud.schedule.Schedule

class RoundFrequency<T>(
    private val schedule: Schedule,
    private val observations: (Round) -> Collection<T>,
) {
    /**
     * Returns the frequencies of the union of all observations in a sublist of the rounds.
     */
    fun frequencies(
        from: Int = 0,
        to: Int = schedule.rounds.size, // Not including
    ): Map<T, Int> =
        schedule.rounds
            .subList(from, to)
            .flatMap { observations(it) }
            .groupingBy { it }
            .eachCount()
}

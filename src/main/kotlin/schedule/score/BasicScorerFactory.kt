package org.ruud.schedule.score

import org.ruud.schedule.Problem
import org.ruud.schedule.RoundFrequency
import org.ruud.schedule.Schedule

class BasicScorerFactory(
    private val problem: Problem,
    private val totalMatchesPlayedWeight: Double,
    private val playingStreakWeight: Double,
    private val pairFrequencyWeight: Double,
) : ScorerFactory {
    override fun create(schedule: Schedule): Scorer =
        WeightedSumScorer().apply {
            add(totalMatchesPlayedWeight, minimumTotalMatchesPlayed(schedule))
            if (problem.idlePlayersPerRound > 0) {
                add(playingStreakWeight, PlayingStreak(schedule).scorer(problem.acceptablePlayingStreak))
            }
            add(pairFrequencyWeight, minimumTotalPairFrequency(schedule))
        }

    private fun minimumTotalPairFrequency(schedule: Schedule): ObservationScorer {
        val pairs = RoundFrequency(schedule) { it.pairs }
        val target = problem.averagePairs(problem.numberOfRounds).toInt()
        val range = target..target + 1
        val minimumTotalPairFrequency =
            ObservationScorer(
                observationScore = inRange(range),
                label = "Final pair frequency in $range",
            ) { pairs.frequencies().values }
        return minimumTotalPairFrequency
    }

    private fun minimumTotalMatchesPlayed(schedule: Schedule): ObservationScorer {
        val matchesPlayed = RoundFrequency(schedule) { it.playing }
        val targetMatchesPlayed = problem.averageMatchesPlayed(problem.numberOfRounds).toInt()
        val range = targetMatchesPlayed..targetMatchesPlayed + 1
        val minimumTotalMatchesPlayed =
            ObservationScorer(
                observationScore = inRange(range),
                label = "Total matches played in $range",
            ) { matchesPlayed.frequencies().values }
        return minimumTotalMatchesPlayed
    }
}

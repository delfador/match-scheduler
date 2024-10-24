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
            add(playingStreakWeight, PlayingStreak(schedule).scorer(problem.acceptablePlayingStreak))
            add(pairFrequencyWeight, minimumTotalPairFrequency(schedule))
        }

    private fun minimumTotalPairFrequency(schedule: Schedule): ObservationScorer {
        val pairs = RoundFrequency(schedule) { it.pairs }
        val minimumTotalPairFrequency =
            run {
                val target = problem.averagePairs(problem.numberOfRounds).toInt()
                ObservationScorer(
                    observationScore = greaterThanOrEqualTo(target),
                    label = "Final pair frequency >= $target",
                ) { pairs.frequencies().values }
            }
        return minimumTotalPairFrequency
    }

    private fun minimumTotalMatchesPlayed(schedule: Schedule): ObservationScorer {
        val matchesPlayed = RoundFrequency(schedule) { it.playing }
        val targetMatchesPlayed = problem.averageMatchesPlayed(problem.numberOfRounds).toInt()
        val minimumTotalMatchesPlayed =
            ObservationScorer(
                observationScore = greaterThanOrEqualTo(targetMatchesPlayed),
                label = "Total matches played >= $targetMatchesPlayed",
            ) { matchesPlayed.frequencies().values }
        return minimumTotalMatchesPlayed
    }
}

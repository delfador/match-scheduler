package org.ruud.score

import org.ruud.schedule.Problem
import org.ruud.schedule.RoundFrequency
import org.ruud.schedule.Schedule

class BasicScorerFactory(
    private val problem: Problem,
) : ScorerFactory {
    override fun create(schedule: Schedule): Scorer {
        val matchesPlayed = RoundFrequency(schedule) { it.playing }
        val pairs = RoundFrequency(schedule) { it.pairs }

        val targetMatchesPlayed = problem.averageMatchesPlayed(problem.numberOfRounds).toInt()
        val minimumTotalMatchesPlayed =
            ObservationScorer(
                observationScore = greaterThanOrEqualTo(targetMatchesPlayed),
                label = "Total matches played >= $targetMatchesPlayed",
            ) { matchesPlayed.frequencies().values }

        val playingStreak = PlayingStreak(schedule)
        val playingStreakInRange = playingStreak.scorer(problem.acceptablePlayingStreak)

        val minimumTotalPairFrequency =
            run {
                val target = problem.averagePairs(problem.numberOfRounds).toInt()
                ObservationScorer(
                    observationScore = greaterThanOrEqualTo(target),
                    label = "Final pair frequency >= $target",
                ) { pairs.frequencies().values }
            }

        val weightedSumScorer =
            WeightedSumScorer().apply {
                add(10.0, minimumTotalMatchesPlayed)
                add(10.0, playingStreakInRange)
                add(2.0, minimumTotalPairFrequency)
            }

        return weightedSumScorer
    }

    private val intermediateRounds =
        run {
            val step = problem.cycleLength.coerceAtLeast(5)
            (step until problem.numberOfRounds step step)
        }
}

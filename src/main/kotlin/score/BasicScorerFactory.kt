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
            FrequencyScorer(
                scoreFun = greaterThanOrEqualTo(targetMatchesPlayed),
                label = "Total matches played < $targetMatchesPlayed",
            ) { matchesPlayed.frequencies().values }

        val playingStreak = PlayingStreak(schedule, targetStreak = problem.acceptablePlayingStreak)

        val minimumTotalPairFrequency =
            run {
                val target = problem.averagePairs(problem.numberOfRounds).toInt()
                FrequencyScorer(
                    scoreFun = greaterThanOrEqualTo(target),
                    label = "Final pair frequency < $target",
                ) { pairs.frequencies().values }
            }

        val intermediatePairFrequency =
            intermediateRounds.map { roundNumber ->
                val target = problem.averagePairs(roundNumber).toInt()
                FrequencyScorer(
                    scoreFun = greaterThanOrEqualTo(target),
                    label = "Pair frequency until round $roundNumber",
                ) { pairs.frequencies(0, roundNumber).values }
            }

        val weightedSumScorer =
            WeightedSumScorer().apply {
                add(10.0, minimumTotalMatchesPlayed)
                add(10.0, playingStreak)

                add(2.0, minimumTotalPairFrequency)
                // intermediatePairFrequency.forEach { add(0.5, it) }
            }

        return weightedSumScorer
    }

    private val intermediateRounds =
        run {
            val step = problem.cycleLength.coerceAtLeast(5)
            (step until problem.numberOfRounds step step)
        }
}

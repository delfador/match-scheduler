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

        val minimumTotalMatchesPlayed =
            FrequencyScorer(
                greaterThanOrEqualTo(problem.averageMatchesPlayed(problem.numberOfRounds).toInt()),
            ) { matchesPlayed.frequencies().values }

        val intermediateMatchesPlayed =
            intermediateRounds.map { roundNumber ->
                val target = problem.averageMatchesPlayed(roundNumber).toInt() - 1
                FrequencyScorer(greaterThanOrEqualTo(target)) { matchesPlayed.frequencies(0, roundNumber).values }
            }

        val minimumTotalPairFrequency =
            run {
                val target = problem.averagePairs(problem.numberOfRounds).toInt()
                FrequencyScorer(greaterThanOrEqualTo(target)) { pairs.frequencies().values }
            }

        val intermediatePairFrequency =
            intermediateRounds.map { roundNumber ->
                val target = problem.averagePairs(roundNumber).toInt()
                FrequencyScorer(greaterThanOrEqualTo(target)) { pairs.frequencies(0, roundNumber).values }
            }

        val weightedSumScorer =
            WeightedSumScorer().apply {
                add(100.0, minimumTotalMatchesPlayed)
                intermediateMatchesPlayed.forEach { add(10.0, it) }

                add(2.0, minimumTotalPairFrequency)
                intermediatePairFrequency.forEach { add(0.5, it) }
            }

        return weightedSumScorer
    }

    private val intermediateRounds =
        run {
            val step = problem.cycleLength.coerceAtLeast(5)
            (step until problem.numberOfRounds step step)
        }
}

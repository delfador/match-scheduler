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
                scoreFun = greaterThanOrEqualTo(problem.averageMatchesPlayed(problem.numberOfRounds).toInt()),
                label = "Total matches played",
            ) { matchesPlayed.frequencies().values }

        val intermediateMatchesPlayed =
            if (problem.cycleLength > 2) {
                val subListSize = problem.cycleLength + 1
                val target = problem.averageMatchesPlayed(problem.cycleLength).toInt()
                (0 until problem.numberOfRounds - subListSize).map { roundIndex ->
                    FrequencyScorer(
                        scoreFun = greaterThanOrEqualTo(target),
                        label = "Matches played in $roundIndex-${roundIndex + subListSize - 1} >= $target",
                    ) { matchesPlayed.frequencies(roundIndex, roundIndex + subListSize).values }
                }
            } else {
                emptyList()
            }

        val minimumTotalPairFrequency =
            run {
                val target = problem.averagePairs(problem.numberOfRounds).toInt()
                FrequencyScorer(
                    scoreFun = greaterThanOrEqualTo(target),
                    label = "Final pair frequency",
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

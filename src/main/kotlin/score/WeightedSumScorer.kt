package org.ruud.score

class WeightedSumScorer : Scorer {
    private val scorers = mutableListOf<Pair<Double, () -> Double>>()

    fun add(
        weight: Double,
        scorer: () -> Double,
    ) = apply {
        scorers.add(weight to scorer)
    }

    override fun invoke(): Double =
        scorers.sumOf { (weight, scorer) ->
            weight * scorer()
        }
}

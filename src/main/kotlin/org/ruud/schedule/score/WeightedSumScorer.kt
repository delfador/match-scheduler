package org.ruud.schedule.score

import org.ruud.common.format

class WeightedSumScorer(
    override val label: String = "WEIGHTED SUM",
) : Scorer {
    private val scorers = mutableListOf<Pair<Double, Scorer>>()

    fun add(
        weight: Double,
        scorer: Scorer,
    ) = apply {
        scorers.add(weight to scorer)
    }

    override fun invoke(): Double =
        scorers.sumOf { (weight, scorer) ->
            weight * scorer()
        }

    override fun reportString() =
        buildString {
            appendLine(label)
            scorers.forEach { (weight, scorer) ->
                val score = scorer.invoke()
                val weightedScore = weight * score
                append("  ${scorer.label.padEnd(35)}:")
                append(" weight = ${weight.format(2).padStart(6)}")
                append(", score = ${score.format(4).padStart(8)}")
                appendLine(", weighted score = ${weightedScore.format(4).padStart(8)}")
            }
            appendLine()
            appendLine("  TOTAL SCORE: ${invoke().format(4)}")
        }
}

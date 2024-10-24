package org.ruud.schedule.score

class ObservationScorer(
    private val observationScore: (Int) -> Double,
    override val label: String = "scorer",
    private val observations: () -> Iterable<Int>,
) : Scorer {
    override fun invoke(): Double = observations().sumOf { observationScore(it) }
}

fun lessThanOrEqualTo(threshold: Int) = { x: Int -> maxOf(x - threshold, 0).toDouble() }

fun greaterThanOrEqualTo(threshold: Int) = { x: Int -> maxOf(threshold - x, 0).toDouble() }

fun inRange(range: IntRange) = { x: Int -> maxOf(range.first - x, x - range.last, 0).toDouble() }

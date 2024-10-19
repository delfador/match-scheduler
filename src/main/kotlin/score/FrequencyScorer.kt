package org.ruud.score

class FrequencyScorer(
    private val scoreFun: (Int) -> Double,
    private val frequencies: () -> Iterable<Int>,
) : Scorer {
    override fun invoke(): Double = frequencies().sumOf { scoreFun(it) }.toDouble()
}

fun lessThanOrEqualTo(threshold: Int) = { x: Int -> maxOf(x - threshold, 0).toDouble() }

fun greaterThanOrEqualTo(threshold: Int) = { x: Int -> maxOf(threshold - x, 0).toDouble() }

fun inRange(range: IntRange) = { x: Int -> maxOf(range.first - x, x - range.last, 0).toDouble() }

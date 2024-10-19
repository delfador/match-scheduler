package org.ruud.score

class FrequencyScorer(
    private val scoreFun: (Int) -> Int,
    private val frequencies: () -> Iterable<Int>,
) {
    fun score(): Int = frequencies().sumOf { scoreFun(it) }
}

fun lessThanOrEqualTo(threshold: Int) = { x: Int -> maxOf(x - threshold, 0) }

fun greaterThanOrEqualTo(threshold: Int) = { x: Int -> maxOf(threshold - x, 0) }

fun inRange(range: IntRange) = { x: Int -> maxOf(range.first - x, x - range.last, 0) }

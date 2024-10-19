package org.ruud.score

class LessThanOrEqual(
    private val threshold: Int,
    private val frequencies: () -> Iterable<Int>,
) {
    fun score(): Int =
        frequencies().sumOf { frequency ->
            (frequency - threshold).coerceAtLeast(0)
        }
}

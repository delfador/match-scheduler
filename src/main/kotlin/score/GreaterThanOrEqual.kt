package org.ruud.score

class GreaterThanOrEqual(
    private val threshold: Int,
    private val frequencies: () -> Iterable<Int>,
) {
    fun score(): Int =
        frequencies().sumOf { frequency ->
            (threshold - frequency).coerceAtLeast(0)
        }
}

package org.ruud.score

interface Scorer : () -> Double {
    val label: String

    fun reportString(): String = "$label: ${this()}"
}

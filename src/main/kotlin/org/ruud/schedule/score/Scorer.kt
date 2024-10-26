package org.ruud.schedule.score

interface Scorer : () -> Double {
    val label: String

    fun reportString(): String = "$label: ${this()}"
}

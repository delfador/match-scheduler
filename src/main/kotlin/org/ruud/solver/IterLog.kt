package org.ruud.solver

data class IterLog(
    val iter: Int,
    val score: Double,
) {
    companion object {
        fun Iterable<IterLog>.toCsv(): String =
            buildString {
                appendLine("iter,score")
                this@toCsv.forEach {
                    appendLine("${it.iter} ,${it.score}")
                }
            }
    }
}

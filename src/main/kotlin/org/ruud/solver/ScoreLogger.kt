package org.ruud.solver

class ScoreLogger : Observer {
    private val _scores = mutableListOf<IterLog>()

    val scores: MutableList<IterLog>
        get() = _scores

    override fun accept(iterLog: IterLog) {
        scores.add(iterLog)
    }
}

package org.ruud.schedule

import org.ruud.score.ScorerFactory
import org.ruud.solver.Solution

class ScheduleSolution(
    val problem: Problem,
    val schedule: Schedule,
    private val scorerFactory: ScorerFactory,
) : Solution<ScheduleSolution> {
    private val scorer by lazy { scorerFactory.create(schedule) }

    private lateinit var lastMove: Move

    override fun score(): Double = scorer()

    override fun copy(): ScheduleSolution = ScheduleSolution(problem, schedule.copy(), scorerFactory)

    override fun move() {
        lastMove = SwapPlayerPositions.random(schedule)
        lastMove.execute(schedule)
    }

    override fun undoMove() {
        lastMove.undo(schedule)
    }

    fun detailScore(): String = scorer.reportString()
}

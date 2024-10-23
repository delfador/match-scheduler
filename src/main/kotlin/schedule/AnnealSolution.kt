package org.ruud.schedule

import org.ruud.score.ScorerFactory
import solver.Solution

class AnnealSolution(
    private val problem: Problem,
    private val schedule: Schedule,
    private val scorerFactory: ScorerFactory,
    private val moveSelector: MoveSelector,
) : Solution<Schedule, Move> {
    override fun getState(): Schedule = schedule

    private val scorer by lazy { scorerFactory.create(schedule) }

    override fun score(): Double = scorer()

    override fun detailScore(): String = scorer.reportString()

    override fun randomMove(): Move = moveSelector.select(schedule)

    override fun execute(move: Move) {
        move.execute(schedule)
    }

    override fun undo(move: Move) {
        move.undo(schedule)
    }

    override fun copy(): Solution<Schedule, Move> = AnnealSolution(problem, schedule.copy(), scorerFactory, moveSelector)
}

package org.ruud.schedule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.ruud.schedule.move.Move
import org.ruud.schedule.move.MoveWeights
import org.ruud.schedule.score.BasicScorerFactory
import org.ruud.schedule.score.ScoringWeights
import org.ruud.solver.Anneal
import org.ruud.solver.AnnealOptions
import org.ruud.solver.Solution

class ProblemSolver(
    private val problem: Problem,
    scoringWeights: ScoringWeights = ScoringWeights(),
    moveWeights: MoveWeights = MoveWeights(),
    annealOptions: AnnealOptions = AnnealOptions(),
) {
    data class Result(
        val schedule: Schedule,
        val score: Double,
        val detailScore: String,
    )

    private val scorerFactory = BasicScorerFactory(problem, scoringWeights)

    private val moveSelector = moveWeights.toMoveSelector()

    private val parallelSolvers = annealOptions.parallelSolvers

    private val annealSolver =
        with(annealOptions) {
            Anneal<Schedule, Move>(
                initialTemperature = initialTemperature,
                coolingRate = coolingRate,
                maxIter = maxIter,
            )
        }

    fun solve(): Result {
        val solutions =
            runBlocking(Dispatchers.Default) {
                List(parallelSolvers) {
                    async { solveWithAnneal() }
                }.map { it.await() }
            }

        val scores = solutions.joinToString(", ") { it.score().toString() }
        println("Scores: $scores")

        val solution = solutions.minBy { it.score() }
        val schedule = solution.getState()

        return Result(
            schedule,
            solution.score(),
            solution.detailScore(),
        )
    }

    private fun solveWithAnneal(): Solution<Schedule, Move> {
        val schedule = Schedule.random(problem)
        val initialSolution = ScheduleSolution(schedule, scorerFactory, moveSelector)
        return annealSolver.solve(initialSolution = initialSolution)
    }
}

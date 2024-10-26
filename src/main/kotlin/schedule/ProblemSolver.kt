package org.ruud.schedule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.ruud.schedule.move.Move
import org.ruud.schedule.move.MoveSelector
import org.ruud.schedule.move.MoveType
import org.ruud.schedule.score.BasicScorerFactory
import org.ruud.schedule.score.ScoringWeights
import org.ruud.solver.Anneal
import solver.Solution

class ProblemSolver(
    private val problem: Problem,
    scoringWeights: ScoringWeights = ScoringWeights(),
    private val parallelSolvers: Int = Runtime.getRuntime().availableProcessors(),
) {
    data class Result(
        val schedule: Schedule,
        val score: Double,
        val detailScore: String,
    )

    private val scorerFactory = BasicScorerFactory(problem, scoringWeights)

    private val moveSelector =
        MoveSelector(
            mapOf(
                MoveType.SwapPlayer to 10.0,
                // 1.0 to MoveType.RotatePlayers,
                MoveType.SwapRound to 5.0,
            ),
        )

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

        return Anneal<Schedule, Move>().solve(
            initialSolution = initialSolution,
            initialTemperature = 56.0,
            coolingRate = 0.994,
            coolingInterval = 100,
            maxIter = 100_000,
        )
    }
}

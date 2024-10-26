package org.ruud.schedule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.ruud.schedule.move.Move
import org.ruud.schedule.move.MoveSelector
import org.ruud.schedule.move.MoveType
import org.ruud.schedule.score.BasicScorerFactory
import org.ruud.solver.Anneal
import solver.Solution

class ProblemSolver(
    private val problem: Problem,
) {
    private val scorerFactory =
        BasicScorerFactory(
            problem = problem,
            totalMatchesPlayedWeight = 6.0,
            playingStreakWeight = 5.0,
            pairFrequencyWeight = 1.0,
        )

    private val moveSelector =
        MoveSelector(
            listOf(
                10.0 to MoveType.SwapPlayer,
                // 1.0 to MoveType.RotatePlayers,
                5.0 to MoveType.SwapRound,
            ),
        )

    fun solve(): Schedule {
        val solutions =
            runBlocking(Dispatchers.Default) {
                List(Runtime.getRuntime().availableProcessors()) {
                    async { solveWithAnneal() }
                }.map { it.await() }
            }

        val solution = solutions.minBy { it.score() }
        val schedule = solution.getState()
        return schedule
    }

    private fun solveWithAnneal(): Solution<Schedule, Move> {
        val schedule = Schedule.random(problem)
        val initialSolution = ScheduleSolution(problem, schedule, scorerFactory, moveSelector)

        val solution =
            Anneal<Schedule, Move>().solve(
                initialSolution = initialSolution,
                initialTemperature = 56.0,
                coolingRate = 0.994,
                coolingInterval = 100,
                maxIter = 100_000,
            )
        return solution
    }
}

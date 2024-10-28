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
import kotlin.random.Random

class ProblemSolver(
    private val problem: Problem,
    scoringWeights: ScoringWeights = ScoringWeights(),
    private val moveWeights: MoveWeights = MoveWeights(),
    private val annealOptions: AnnealOptions = AnnealOptions(),
    private val random: Random = Random,
) {
    data class Result(
        val schedule: Schedule,
        val score: Double,
        val detailScore: String,
    )

    private val scorerFactory = BasicScorerFactory(problem, scoringWeights)

    private val parallelSolvers = annealOptions.parallelSolvers

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
        val seed = random.nextInt()
        val random = Random(seed)
        val schedule = Schedule.random(problem, random)
        val moveSelector = moveWeights.toMoveSelector(random)
        val initialSolution = ScheduleSolution(schedule, scorerFactory, moveSelector)
        val solver = getAnnealSolver(annealOptions, random)
        return solver.solve(initialSolution = initialSolution)
    }

    private fun getAnnealSolver(
        annealOptions: AnnealOptions,
        random: Random,
    ) = with(annealOptions) {
        Anneal<Schedule, Move>(
            initialTemperature = initialTemperature,
            coolingRate = coolingRate,
            maxIter = maxIter,
            random = random,
        )
    }
}

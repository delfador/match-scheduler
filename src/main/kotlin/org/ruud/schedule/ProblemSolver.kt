package org.ruud.schedule

import org.ruud.schedule.move.Move
import org.ruud.schedule.move.MoveWeights
import org.ruud.schedule.score.BasicScorerFactory
import org.ruud.schedule.score.ScoringWeights
import org.ruud.solver.Anneal
import org.ruud.solver.AnnealOptions
import org.ruud.solver.ScoreLogger
import kotlin.random.Random

class ProblemSolver(
    private val problem: Problem,
    private val scoringWeights: ScoringWeights = ScoringWeights(),
    private val moveWeights: MoveWeights = MoveWeights(),
    private val annealOptions: AnnealOptions = AnnealOptions(),
) {
    fun solve(
        random: Random = Random,
        iterLog: Boolean = false,
    ): Result {
        val solver = getAnnealSolver(random)
        val scoreLogger = if (iterLog) ScoreLogger() else null
        scoreLogger?.let { solver.addObserver(it) }

        val initialSolution = getInitialSolution(problem, random)
        val solution = solver.solve(initialSolution)

        return Result(
            schedule = solution.getState(),
            score = solution.score(),
            detailScore = solution.detailScore(),
            iterLogs = scoreLogger?.scores,
        )
    }

    private fun getInitialSolution(
        problem: Problem,
        random: Random,
    ): ScheduleSolution {
        val schedule = Schedule.random(problem, random)
        val moveSelector = moveWeights.toMoveSelector(random)
        val scorerFactory = BasicScorerFactory(problem, scoringWeights)
        return ScheduleSolution(schedule, scorerFactory, moveSelector)
    }

    private fun getAnnealSolver(random: Random) =
        with(annealOptions) {
            Anneal<Schedule, Move>(
                initialTemperature = initialTemperature,
                coolingRate = coolingRate,
                maxIter = maxIter,
                random = random,
            )
        }
}

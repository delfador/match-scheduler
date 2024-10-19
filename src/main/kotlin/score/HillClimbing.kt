package org.ruud.score

import org.ruud.schedule.Problem
import org.ruud.schedule.Schedule
import org.ruud.solver.RandomInitializer
import kotlin.random.Random

class HillClimbing(
    private val problem: Problem,
    private val scorerFactory: ScorerFactory = BasicScorerFactory(problem),
) {
    fun solve(maxIter: Int = 1_000): Schedule {
        val schedule = RandomInitializer(problem).create()
        val scorer = scorerFactory.create(schedule)

        var bestScore = scorer()
        var incumbent = schedule.copy()
        var iter = 0

        while (iter < maxIter) {
            iter++

            val round = schedule.rounds.random()
            val (i, j) = randomIndices()
            round.swapPositions(i, j)

            val score = scorer()

            if (score < bestScore) {
                incumbent = schedule.copy()
                bestScore = score
                println("$iter: $score")
            } else {
                round.swapPositions(j, i)
            }
        }

        return incumbent
    }

    private fun randomIndices(): Pair<Int, Int> {
        val index1 = Random.nextInt(problem.numberOfPlayers)
        var index2 = Random.nextInt(problem.numberOfPlayers)
        while (index2 == index1) {
            index2 = Random.nextInt(problem.numberOfPlayers)
        }

        return Pair(index1, index2)
    }
}

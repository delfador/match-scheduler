package org.ruud.schedule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class ParallelSolver(
    private val problemSolver: ProblemSolver,
    private val parallelSolvers: Int,
) {
    fun solve(random: Random): Result {
        val results = solveParallel(random)
        val scores = results.map { it.score }
        println("Scores: $scores")
        return results.minBy { it.score }
    }

    fun solveParallel(random: Random = Random): Collection<Result> =
        runBlocking(Dispatchers.Default) {
            val seeds = List(parallelSolvers) { random.nextLong() }

            seeds
                .map { seed ->
                    async { problemSolver.solve(Random(seed)) }
                }.map { it.await() }
        }
}

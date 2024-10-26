package org.ruud.solver

import solver.Solution
import kotlin.math.exp
import kotlin.random.Random

class Anneal<S, M> {
    fun solve(
        initialSolution: Solution<S, M>,
        initialTemperature: Double,
        coolingRate: Double,
        coolingInterval: Int,
        maxIter: Int,
    ): Solution<S, M> {
        val solution = initialSolution.copy()
        var currentScore = solution.score()
        var bestSolution = initialSolution.copy()
        var bestScore = bestSolution.score()

        var iter = 0
        var temperature = initialTemperature

        while (iter < maxIter) {
            val move = solution.randomMove()
            solution.execute(move)
            val newScore = solution.score()

            if (acceptMove(newScore, currentScore, temperature)) {
                currentScore = newScore
            } else {
                solution.undo(move)
            }

            if (newScore < bestScore) {
                bestScore = newScore
                bestSolution = solution.copy()
            }

            iter++
            if (iter % coolingInterval == 0) {
                temperature *= coolingRate
            }
        }

        return bestSolution
    }

    private fun acceptMove(
        newScore: Double,
        currentScore: Double,
        temperature: Double,
    ): Boolean {
        if (newScore <= currentScore) {
            return true
        }

        val probability = exp(-(newScore - currentScore) / temperature)
        val uniform = Random.nextDouble()
        return uniform <= probability
    }
}

package org.ruud.solver

import kotlin.math.exp
import kotlin.random.Random

class Anneal<S, M>(
    private val initialTemperature: Double,
    private val coolingRate: Double,
    private val maxIter: Int,
    private val random: Random = Random,
) {
    private val observers = mutableListOf<Observer>()

    fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    fun solve(initialSolution: Solution<S, M>): Solution<S, M> {
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
                observers.forEach { it.accept(IterLog(iter, newScore)) }
            } else {
                solution.undo(move)
            }

            if (newScore < bestScore) {
                bestScore = newScore
                bestSolution = solution.copy()
            }

            iter++
            temperature *= coolingRate
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

        val probability = acceptanceProbability(newScore, currentScore, temperature)
        val uniform = random.nextDouble()
        return uniform <= probability
    }

    companion object {
        fun acceptanceProbability(
            newScore: Double,
            currentScore: Double,
            temperature: Double,
        ): Double {
            val probability = exp(-(newScore - currentScore) / temperature)
            return probability
        }
    }
}

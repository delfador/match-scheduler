package org.ruud.solver

class HillClimbing<T : Solution<T>> {
    fun solve(
        initialSolution: T,
        maxIter: Int = 1_000,
    ): T {
        val solution = initialSolution.copy()
        var bestSolution = initialSolution.copy()
        var bestScore = bestSolution.score()
        var iter = 0

        while (iter < maxIter) {
            iter++

            solution.move()
            val score = solution.score()

            if (score < bestScore) {
                println("$iter: $score")
                bestScore = score
                bestSolution = solution.copy()
            } else {
                solution.undoMove()
            }
        }

        return bestSolution
    }
}

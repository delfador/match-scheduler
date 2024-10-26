package org.ruud.schedule

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ProblemSolverTest {
    @Test
    fun `should solve a simple problem`() {
        val problem = Problem(numberOfPlayers = 6, numberOfRounds = 8, playersPerMatch = 4)
        val problemSolver = ProblemSolver(problem, parallelSolvers = 1)
        val result = problemSolver.solve()

        val schedule = result.schedule

        assertThat(schedule.playersPerMatch).isEqualTo(4)
        assertThat(schedule.numberOfRounds).isEqualTo(8)
        assertThat(schedule.playersPerMatch).isEqualTo(4)

        val score = result.score

        assertThat(score).`as` { "Score should not be too bad." }.isLessThan(10.0)
    }
}

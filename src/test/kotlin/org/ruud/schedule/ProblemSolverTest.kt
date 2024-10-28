package org.ruud.schedule

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ruud.solver.AnnealOptions
import kotlin.random.Random

class ProblemSolverTest {
    @Test
    fun `should solve a simple problem`() {
        val problem = Problem(numberOfPlayers = 6, numberOfRounds = 8, playersPerMatch = 4)
        val problemSolver = ProblemSolver(problem)
        val result = problemSolver.solve()

        val schedule = result.schedule

        assertThat(schedule.playersPerMatch).isEqualTo(4)
        assertThat(schedule.numberOfRounds).isEqualTo(8)
        assertThat(schedule.playersPerMatch).isEqualTo(4)

        val score = result.score

        assertThat(score).`as` { "Score should not be too bad." }.isLessThan(10.0)
    }

    @Test
    fun `should be deterministic for fixed seed`() {
        val problem = Problem(numberOfPlayers = 6, numberOfRounds = 8, playersPerMatch = 4)
        val schedules =
            List(2) {
                val solver =
                    ProblemSolver(
                        problem,
                        annealOptions = AnnealOptions(coolingRate = 0.99, maxIter = 100),
                        random = Random(seed = 42),
                    )
                val solution = solver.solve()
                solution.schedule
            }

        assertThat(schedules[0]).isEqualTo(schedules[1])
    }
}

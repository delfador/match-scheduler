package schedule

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ruud.schedule.Problem

class ProblemTest {
    @Test
    fun testMatchesPerRound() {
        assertThat(Problem(4, 1, 4).matchesPerRound).isEqualTo(1)
        assertThat(Problem(6, 1, 4).matchesPerRound).isEqualTo(1)
        assertThat(Problem(10, 1, 4).matchesPerRound).isEqualTo(2)
    }

    @Test
    fun testPlayerPerRound() {
        assertThat(Problem(4, 1, 4).playersPerRound).isEqualTo(4)
        assertThat(Problem(6, 1, 4).playersPerRound).isEqualTo(4)
        assertThat(Problem(10, 1, 4).playersPerRound).isEqualTo(8)
    }

    @Test
    fun testCycleLength() {
        assertThat(Problem(4, 1, 4).cycleLength).isEqualTo(1)
        assertThat(Problem(5, 1, 4).cycleLength).isEqualTo(5)
        assertThat(Problem(6, 1, 4).cycleLength).isEqualTo(3)
        assertThat(Problem(10, 1, 4).cycleLength).isEqualTo(5)
    }
}

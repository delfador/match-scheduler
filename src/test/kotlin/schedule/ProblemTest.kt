package schedule

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ruud.schedule.Problem

class ProblemTest {
    @Test
    fun testMatchesPerRound() {
        assertThat(Problem(6, 2, 4).matchesPerRound).isEqualTo(1)
        assertThat(Problem(8, 2, 4).matchesPerRound).isEqualTo(2)
        assertThat(Problem(10, 2, 4).matchesPerRound).isEqualTo(2)
    }

    @Test
    fun testPlayersPerRound() {
        assertThat(Problem(6, 2, 4).playersPerRound).isEqualTo(4)
        assertThat(Problem(8, 2, 4).playersPerRound).isEqualTo(8)
        assertThat(Problem(10, 2, 4).playersPerRound).isEqualTo(8)
    }

    @Test
    fun testCycleLength() {
        assertThat(Problem(8, 2, 4).cycleLength).isEqualTo(1)
        assertThat(Problem(5, 2, 4).cycleLength).isEqualTo(5)
        assertThat(Problem(6, 2, 4).cycleLength).isEqualTo(3)
        assertThat(Problem(10, 2, 4).cycleLength).isEqualTo(5)
    }

    @Test
    fun `should compute average matches played`() {
        assertThat(Problem(8, 2, 4).averageMatchesPlayed(4)).isEqualTo(4.0)
        assertThat(Problem(5, 2, 4).averageMatchesPlayed(5)).isEqualTo(4.0)
        assertThat(Problem(10, 2, 4).averageMatchesPlayed(10)).isEqualTo(8.0)
    }
}

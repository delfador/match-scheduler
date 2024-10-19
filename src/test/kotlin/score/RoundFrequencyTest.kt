package score

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ruud.schedule.Round
import org.ruud.schedule.Schedule
import org.ruud.score.RoundFrequency

class RoundFrequencyTest {
    private val rounds =
        listOf(
            Round.regular(6, 4),
            Round.regular(6, 4, 1),
            Round.regular(6, 4, 2),
        )
    private val schedule = Schedule(rounds)

    @Test
    fun `returns number of matches played by player`() {
        val matchesPlayed = RoundFrequency(schedule) { it.playing }

        assertThat(matchesPlayed.frequencies(0, 1))
            .containsEntry(0, 1)
            .containsEntry(1, 1)
            .containsEntry(2, 1)
            .containsEntry(3, 1)
            .doesNotContainKeys(4, 5)

        assertThat(matchesPlayed.frequencies(0, 2))
            .containsEntry(0, 2)
            .containsEntry(1, 2)
            .containsEntry(2, 2)
            .containsEntry(3, 1)
            .doesNotContainKeys(4)
            .containsEntry(5, 1)

        assertThat(matchesPlayed.frequencies(0, 3))
            .containsEntry(0, 3)
            .containsEntry(1, 3)
            .containsEntry(2, 2)
            .containsEntry(3, 1)
            .containsEntry(4, 1)
            .containsEntry(5, 2)

        assertThat(matchesPlayed.frequencies(1, 3))
            .containsEntry(0, 2)
            .containsEntry(1, 2)
            .containsEntry(2, 1)
            .doesNotContainKeys(3)
            .containsEntry(4, 1)
            .containsEntry(5, 2)
    }

    @Test
    fun `return player pair frequencies`() {
        val pairFrequency = RoundFrequency(schedule) { it.pairs }

        val frequencies = pairFrequency.frequencies(0, 3)

        // Pairs for player 0 only.
        assertThat(frequencies)
            .containsEntry(Pair(0, 1), 3)
            .containsEntry(Pair(0, 2), 2)
            .containsEntry(Pair(0, 3), 1)
            .containsEntry(Pair(0, 4), 1)
            .containsEntry(Pair(0, 5), 2)

        val totalPairs = 3 * 6
        assertThat(frequencies.values.sum()).isEqualTo(totalPairs)
    }
}

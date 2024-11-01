package org.ruud.schedule

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

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
            .containsEntry(1, 1)
            .containsEntry(2, 1)
            .containsEntry(3, 1)
            .containsEntry(4, 1)
            .doesNotContainKeys(5, 6)

        assertThat(matchesPlayed.frequencies(0, 2))
            .containsEntry(1, 2)
            .containsEntry(2, 2)
            .containsEntry(3, 2)
            .containsEntry(4, 1)
            .doesNotContainKeys(5)
            .containsEntry(6, 1)

        assertThat(matchesPlayed.frequencies(0, 3))
            .containsEntry(1, 3)
            .containsEntry(2, 3)
            .containsEntry(3, 2)
            .containsEntry(4, 1)
            .containsEntry(5, 1)
            .containsEntry(6, 2)

        assertThat(matchesPlayed.frequencies(1, 3))
            .containsEntry(1, 2)
            .containsEntry(2, 2)
            .containsEntry(3, 1)
            .doesNotContainKeys(4)
            .containsEntry(5, 1)
            .containsEntry(6, 2)
    }

    @Test
    fun `return player pair frequencies`() {
        val pairFrequency = RoundFrequency(schedule) { it.pairs }

        val frequencies = pairFrequency.frequencies(0, 3)

        // Pairs for player 1 only.
        assertThat(frequencies)
            .containsEntry(Pair(1, 2), 3)
            .containsEntry(Pair(1, 3), 2)
            .containsEntry(Pair(1, 4), 1)
            .containsEntry(Pair(1, 5), 1)
            .containsEntry(Pair(1, 6), 2)

        val totalPairs = 3 * 6
        assertThat(frequencies.values.sum()).isEqualTo(totalPairs)
    }
}

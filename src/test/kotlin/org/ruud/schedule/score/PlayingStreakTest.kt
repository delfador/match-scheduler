package org.ruud.schedule.score

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ruud.schedule.Round
import org.ruud.schedule.Schedule

class PlayingStreakTest {
    @Test
    fun `should return correct streaks`() {
        val rounds =
            listOf(
                Round.regular(6, 4, 0),
                Round.regular(6, 4, 2),
                Round.regular(6, 4, 4),
                Round.regular(6, 4, 0),
                Round.regular(6, 4, 1),
            )
        val schedule = Schedule(rounds)

        val playingStreak = PlayingStreak(schedule)
        val playingStreaksByPlayer = playingStreak.playingStreaksByPlayer()

        assertThat(playingStreaksByPlayer)
            .containsEntry(1, emptyList())
            .containsEntry(2, emptyList())
            .containsEntry(3, emptyList())
            .containsEntry(4, listOf(2))
            .containsEntry(5, listOf(2, 0))
            .containsEntry(6, listOf(2))
    }
}

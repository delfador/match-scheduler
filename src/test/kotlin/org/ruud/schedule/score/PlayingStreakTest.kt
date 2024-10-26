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
        /*
             0: [0, 1, 2, 3], [4, 5]
             1: [4, 5, 0, 1], [2, 3]
             2: [2, 3, 4, 5], [0, 1]
             3: [0, 1, 2, 3], [4, 5]
             4: [5, 0, 1, 2], [3, 4]
         */

        val playingStreak = PlayingStreak(schedule)
        val playingStreaksByPlayer = playingStreak.playingStreaksByPlayer()

        assertThat(playingStreaksByPlayer)
            .containsEntry(0, emptyList())
            .containsEntry(1, emptyList())
            .containsEntry(2, emptyList())
            .containsEntry(3, listOf(2))
            .containsEntry(4, listOf(2, 0))
            .containsEntry(5, listOf(2))
    }
}

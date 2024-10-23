package schedule

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ruud.schedule.RotatePlayers
import org.ruud.schedule.Round
import org.ruud.schedule.Schedule

class RotatePlayersTest {
    private val rounds =
        listOf(
            Round.regular(10, 4),
            Round.regular(10, 4, 1),
            Round.regular(10, 4, 2),
        )
    private val schedule = Schedule(rounds)

    @Test
    fun `should perform the configured swap`() {
        val move = RotatePlayers(round = 0, steps = 2)

        move.execute(schedule)

        assertThat(schedule.rounds[0]).isEqualTo(Round(listOf(8, 9, 0, 1, 2, 3, 4, 5, 6, 7), 4))
        assertThat(schedule.rounds[1]).isEqualTo(rounds[1])
        assertThat(schedule.rounds[2]).isEqualTo(rounds[2])
    }

    @Test
    fun `should return to original schedule after execute and undo`() {
        val scheduleCopy = schedule.copy()

        val move = RotatePlayers(round = 0, steps = 2)
        move.execute(schedule)
        move.undo(schedule)

        assertThat(schedule).isEqualTo(scheduleCopy)
    }
}

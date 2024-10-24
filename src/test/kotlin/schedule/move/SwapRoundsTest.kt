package schedule.move

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ruud.schedule.Round
import org.ruud.schedule.Schedule
import org.ruud.schedule.move.SwapRounds

class SwapRoundsTest {
    private val rounds =
        listOf(
            Round.regular(10, 4),
            Round.regular(10, 4, 1),
            Round.regular(10, 4, 2),
        )
    private val schedule = Schedule(rounds)

    @Test
    fun `should perform the configured swap`() {
        val move = SwapRounds(0, 2)

        move.execute(schedule)

        assertThat(schedule.rounds).isEqualTo(
            listOf(
                rounds[2],
                rounds[1],
                rounds[0],
            ),
        )
    }

    @Test
    fun `should return to original schedule after execute and undo`() {
        val scheduleCopy = schedule.copy()

        val move = SwapRounds(0, 2)
        move.execute(schedule)
        move.undo(schedule)

        assertThat(schedule).isEqualTo(scheduleCopy)
    }
}

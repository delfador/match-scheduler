package schedule.move

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ruud.schedule.Round
import org.ruud.schedule.Schedule
import org.ruud.schedule.move.SwapPlayerPositions

class SwapPlayerPositionsTest {
    private val rounds =
        listOf(
            Round.regular(10, 4),
            Round.regular(10, 4, 1),
            Round.regular(10, 4, 2),
        )
    private val schedule = Schedule(rounds)

    @Test
    fun `should perform the configured swap`() {
        val move = SwapPlayerPositions(roundIndex = 0, index1 = 0, index2 = 9)

        move.execute(schedule)

        assertThat(schedule.rounds[0]).isEqualTo(Round(listOf(9, 1, 2, 3, 4, 5, 6, 7, 8, 0), 4))
        assertThat(schedule.rounds[1]).isEqualTo(rounds[1])
        assertThat(schedule.rounds[2]).isEqualTo(rounds[2])
    }

    @Test
    fun `should return to original schedule after execute and undo`() {
        val scheduleCopy = schedule.copy()

        val move = SwapPlayerPositions(0, 0, 9)
        move.execute(schedule)
        move.undo(schedule)

        assertThat(schedule).isEqualTo(scheduleCopy)
    }
}

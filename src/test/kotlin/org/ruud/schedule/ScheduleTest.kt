package org.ruud.schedule

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ScheduleTest {
    @Test
    fun `initialize with list of rounds`() {
        val rounds =
            listOf(
                Round.regular(6, 4, 0),
                Round.regular(6, 4, 1),
            )
        val schedule = Schedule(rounds)

        assertThat(schedule.rounds).isEqualTo(rounds)
    }

    @Test
    fun `throws exception if rounds have different number of players`() {
        val rounds =
            listOf(
                Round.regular(6, 4, 0),
                Round.regular(7, 4, 1),
            )

        assertThrows<IllegalArgumentException> {
            Schedule(rounds)
        }
    }

    @Test
    fun `throws exception if rounds have different numbers of players per match`() {
        val rounds =
            listOf(
                Round.regular(6, 4, 0),
                Round.regular(6, 3, 1),
            )

        assertThrows<IllegalArgumentException> {
            Schedule(rounds)
        }
    }

    @Test
    fun `allows swapping of rounds`() {
        val rounds =
            listOf(
                Round.regular(6, 4),
                Round.regular(6, 4, 1),
            )
        val schedule = Schedule(rounds)

        schedule.swapRounds(0, 1)

        assertThat(schedule.rounds).isEqualTo(rounds.reversed())
    }

    @Test
    fun `construction should make copies of rounds`() {
        val rounds =
            listOf(
                Round.regular(6, 4),
                Round.regular(6, 4, 1),
            )
        val schedule = Schedule(rounds)

        rounds[0].swapPositions(0, 4)
        assertThat(schedule.rounds[0]).isEqualTo(Round.regular(6, 4))
    }

    @Test
    fun `should create copy`() {
        val rounds =
            listOf(
                Round.regular(6, 4),
                Round.regular(6, 4, 1),
            )
        val schedule = Schedule(rounds)
        val scheduleCopy = schedule.copy()
        assertThat(scheduleCopy).isEqualTo(schedule)
        assertThat(scheduleCopy).isNotSameAs(schedule)
    }
}

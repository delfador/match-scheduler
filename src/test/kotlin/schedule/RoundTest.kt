package schedule

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.ruud.schedule.Round

class RoundTest {
    @Test
    fun `should correctly derive the playing players`() {
        assertThat(Round((0..9).toList(), 4).playing)
            .containsExactlyInAnyOrder(0, 1, 2, 3, 4, 5, 6, 7)

        assertThat(Round((0..9).toList().reversed(), 4).playing)
            .containsExactlyInAnyOrder(2, 3, 4, 5, 6, 7, 8, 9)
    }

    @Test
    fun `should correctly derive the idle players`() {
        assertThat(Round((0..9).toList(), 4).idle)
            .containsExactlyInAnyOrder(8, 9)

        assertThat(Round((0..9).toList().reversed(), 4).idle)
            .containsExactlyInAnyOrder(0, 1)
    }

    @Test
    fun `should throw exception if there are duplicate players`() {
        assertThrows<IllegalArgumentException> {
            Round(listOf(0, 1, 2, 3, 3), 4)
        }
    }

    @Test
    fun `should throw exception if there set of players is not 0 to n`() {
        assertThrows<IllegalArgumentException> {
            Round(listOf(1, 2, 3, 4, 5), 4)
        }
    }

    @Test
    fun `should throw exception players per match too large`() {
        assertThrows<IllegalArgumentException> {
            Round((0..2).toList(), 4)
        }
    }

    @Test
    fun `should return correct pairs for round with only one match without idle players`() {
        val round = Round((0..2).toList(), 3)
        assertThat(round.pairs).containsExactlyInAnyOrder(
            Pair(0, 1),
            Pair(0, 2),
            Pair(1, 2),
        )
    }

    @Test
    fun `should return correct pairs for round with only one match with idle players`() {
        val round = Round((0..3).toList(), 3)
        assertThat(round.pairs).containsExactlyInAnyOrder(
            Pair(0, 1),
            Pair(0, 2),
            Pair(1, 2),
        )
    }

    @Test
    fun `should return correct pairs for round with multiple matches`() {
        val round = Round((0..7).toList(), 3)
        assertThat(round.pairs).containsExactlyInAnyOrder(
            Pair(0, 1),
            Pair(0, 2),
            Pair(1, 2),
            Pair(3, 4),
            Pair(3, 5),
            Pair(4, 5),
        )
    }

    @Test
    fun `should return correct playing and idle players after swapping`() {
        val round = Round((0..4).toList(), 4)
        round.swapPositions(0, 4)
        assertThat(round.playing).containsExactlyInAnyOrder(1, 2, 3, 4)
        assertThat(round.idle).containsExactlyInAnyOrder(0)
    }

    @Test
    fun `should return correct playing and idle players after rotating one step`() {
        val round = Round((0..4).toList(), 4)
        round.rotate(1)
        assertThat(round.playing).containsExactlyInAnyOrder(4, 0, 1, 2)
        assertThat(round.idle).containsExactlyInAnyOrder(3)
    }

    @Test
    fun `should return correct playing and idle players after rotating two steps`() {
        val round = Round((0..4).toList(), 4)
        round.rotate(2)
        assertThat(round.playing).containsExactlyInAnyOrder(3, 4, 0, 1)
        assertThat(round.idle).containsExactlyInAnyOrder(2)
    }

    @Test
    fun `should return equals based state of player list`() {
        val round1 = Round.regular(numberOfPlayers = 6, playersPerMatch = 4)
        val round2 = Round.regular(numberOfPlayers = 6, playersPerMatch = 4)
        val round3 = Round.regular(numberOfPlayers = 6, playersPerMatch = 4, rotate = 1)

        assertThat(round1).isEqualTo(round2)
        assertThat(round1).isNotEqualTo(round3)

        round1.rotate(1)

        assertThat(round1).isNotEqualTo(round2)
        assertThat(round1).isEqualTo(round3)
    }
}

package score

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ruud.score.GreaterThanOrEqual

class GreaterThanOrEqualTest {
    @Test
    fun `returns zero if all frequencies greater than or equal to threshold`() {
        val greaterThanOrEqual = GreaterThanOrEqual(2) { listOf(2, 3, 4) }
        assertThat(greaterThanOrEqual.score()).isEqualTo(0)
    }

    @Test
    fun `should return the total excess above threshold`() {
        val greaterThanOrEqual = GreaterThanOrEqual(2) { listOf(0, 1, 2, 3, 4) }
        assertThat(greaterThanOrEqual.score()).isEqualTo(1 + 2)
    }
}

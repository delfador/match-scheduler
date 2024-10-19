package score

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ruud.score.LessThanOrEqual

class LessThanOrEqualTest {
    @Test
    fun `returns zero if all frequencies less than or equal to threshold`() {
        val lessThanOrEqual = LessThanOrEqual(4) { listOf(0, 1, 2, 3, 4) }
        assertThat(lessThanOrEqual.score()).isEqualTo(0)
    }

    @Test
    fun `should return the total excess above threshold`() {
        val lessThanOrEqual = LessThanOrEqual(2) { listOf(0, 1, 2, 3, 4) }
        assertThat(lessThanOrEqual.score()).isEqualTo(1 + 2)
    }
}

package schedule.score

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ruud.schedule.score.ObservationScorer
import org.ruud.schedule.score.greaterThanOrEqualTo
import org.ruud.schedule.score.inRange
import org.ruud.schedule.score.lessThanOrEqualTo

class ObservationScorerTest {
    @Test
    fun lessThanOrEqualToTest() {
        val scoreFun = lessThanOrEqualTo(3)

        assertThat(scoreFun(2)).isEqualTo(0.0)
        assertThat(scoreFun(3)).isEqualTo(0.0)
        assertThat(scoreFun(4)).isEqualTo(1.0)
        assertThat(scoreFun(5)).isEqualTo(2.0)
    }

    @Test
    fun greaterThanOrEqualToTest() {
        val scoreFun = greaterThanOrEqualTo(3)

        assertThat(scoreFun(1)).isEqualTo(2.0)
        assertThat(scoreFun(2)).isEqualTo(1.0)
        assertThat(scoreFun(3)).isEqualTo(0.0)
        assertThat(scoreFun(4)).isEqualTo(0.0)
    }

    @Test
    fun inRangeTest() {
        val scoreFun = inRange(1..3)

        assertThat(scoreFun(0)).isEqualTo(1.0)
        assertThat(scoreFun(1)).isEqualTo(0.0)
        assertThat(scoreFun(2)).isEqualTo(0.0)
        assertThat(scoreFun(3)).isEqualTo(0.0)
        assertThat(scoreFun(4)).isEqualTo(1.0)
        assertThat(scoreFun(5)).isEqualTo(2.0)
    }

    @Test
    fun `returns total score of frequencies`() {
        val scoreFun = lessThanOrEqualTo(2)
        val scorer = ObservationScorer(scoreFun) { listOf(0, 1, 2, 3, 4) }

        assertThat(scorer()).isEqualTo((1 + 2).toDouble())
    }
}

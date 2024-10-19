package score

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ruud.score.WeightedSumScorer

class WeightedSumScorerTest {
    @Test
    fun `should return weighted score of single scorer`() {
        var value = 2.0
        val scorer = { value }
        val weight = 3.0

        val weightedSumScorer = WeightedSumScorer().add(weight, scorer)

        assertThat(weightedSumScorer()).isEqualTo(2 * 3.0)

        value = 3.0
        assertThat(weightedSumScorer()).isEqualTo(3 * 3.0)
    }

    @Test
    fun `should return weighted score of multiple scorers`() {
        val weightedSumScorer =
            WeightedSumScorer()
                .add(weight = 1.0) { 2.0 }
                .add(weight = 3.0) { 4.0 }

        assertThat(weightedSumScorer()).isEqualTo(1.0 * 2.0 + 3.0 * 4.0)
    }
}

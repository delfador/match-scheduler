package schedule.score

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ruud.schedule.score.Scorer
import org.ruud.schedule.score.WeightedSumScorer

class WeightedSumScorerTest {
    class ScorerStub(
        var value: Double,
    ) : Scorer {
        override val label: String = "stub"

        override fun invoke(): Double = value
    }

    @Test
    fun `should return weighted score of single scorer`() {
        val scorer = ScorerStub(2.0)
        val weight = 3.0

        val weightedSumScorer = WeightedSumScorer().add(weight, scorer)

        assertThat(weightedSumScorer()).isEqualTo(2 * 3.0)

        scorer.value = 3.0
        assertThat(weightedSumScorer()).isEqualTo(3 * 3.0)
    }

    @Test
    fun `should return weighted score of multiple scorers`() {
        val weightedSumScorer =
            WeightedSumScorer()
                .add(weight = 1.0, ScorerStub(2.0))
                .add(weight = 3.0, ScorerStub(4.0))

        assertThat(weightedSumScorer()).isEqualTo(1.0 * 2.0 + 3.0 * 4.0)
    }
}

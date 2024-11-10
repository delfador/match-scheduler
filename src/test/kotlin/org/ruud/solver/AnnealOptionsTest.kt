package org.ruud.solver

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.withinPercentage
import org.junit.jupiter.api.Test
import kotlin.math.pow

class AnnealOptionsTest {
    private val highDelta = 10.0
    private val highProb = 0.8
    private val lowDelta = 1.0
    private val lowProb = 0.01
    private val maxIter = 100

    @Test
    fun `should return options with correct initial acceptance probability`() {
        val annealOptions =
            AnnealOptions.tunedFor(
                highDelta = highDelta,
                lowDelta = lowDelta,
                highProb = highProb,
                lowProb = lowProb,
                maxIter = maxIter,
            )

        val probability =
            Anneal.acceptanceProbability(
                newScore = highDelta,
                currentScore = 0.0,
                temperature = annealOptions.initialTemperature,
            )

        assertThat(probability).isCloseTo(highProb, withinPercentage(1.0))
    }

    @Test
    fun `should return options with correct final acceptance probability`() {
        val annealOptions =
            AnnealOptions.tunedFor(
                highDelta = highDelta,
                lowDelta = lowDelta,
                highProb = highProb,
                lowProb = lowProb,
                maxIter = maxIter,
            )

        val temperature = annealOptions.initialTemperature * annealOptions.coolingRate.pow(annealOptions.maxIter - 1)

        val probability =
            Anneal.acceptanceProbability(
                newScore = lowDelta,
                currentScore = 0.0,
                temperature = temperature,
            )

        assertThat(probability).isCloseTo(lowProb, withinPercentage(1.0))
    }
}

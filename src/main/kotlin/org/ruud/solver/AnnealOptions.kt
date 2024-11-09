package org.ruud.solver

import kotlinx.serialization.Serializable
import kotlin.math.exp
import kotlin.math.ln

@Serializable
data class AnnealOptions(
    val initialTemperature: Double = 50.0,
    val coolingRate: Double = 0.9999,
    val maxIter: Int = 100_000,
) {
    companion object {
        /**
         * Returns [AnnealOptions] that is tuned with the following objective:
         * - The initial acceptance probability for an increase in objective value equal to twice [highDelta] is
         * [highProb].
         * - The final acceptance probability for an increase in objective value equal to [lowDelta] is [lowProb].
         *
         * Assumes a geometric cooling schedule is used for [maxIter] iterations with cooling rate [coolingRate].
         */
        fun tunedFor(
            highDelta: Double,
            lowDelta: Double,
            highProb: Double = 0.8,
            lowProb: Double = 0.01,
            maxIter: Int = 100_000,
        ): AnnealOptions {
            val initialTemperature = -2 * highDelta / ln(highProb)

            val coolingRate =
                exp(
                    ln(
                        -lowDelta / initialTemperature / ln(lowProb),
                    ) / (maxIter - 1),
                )

            return AnnealOptions(
                initialTemperature = initialTemperature,
                coolingRate = coolingRate,
                maxIter = maxIter,
            )
        }
    }
}

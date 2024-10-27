package org.ruud.schedule.score

data class ScoringWeights(
    val totalMatchesPlayedWeight: Double = 6.0,
    val playingStreakWeight: Double = 5.0,
    val pairFrequencyWeight: Double = 1.0,
) {
    fun maximumWeight() =
        maxOf(
            totalMatchesPlayedWeight,
            playingStreakWeight,
            pairFrequencyWeight,
        )

    fun minimumWeight() =
        minOf(
            totalMatchesPlayedWeight,
            playingStreakWeight,
            pairFrequencyWeight,
        )
}

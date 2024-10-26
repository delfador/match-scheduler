package org.ruud.schedule.move

import org.ruud.schedule.Schedule
import kotlin.random.Random

class MoveSelector(
    moveWeights: Map<MoveType, Double>,
) {
    init {
        moveWeights.forEach { (moveType, weight) ->
            require(weight >= 0) {
                "Move weights should be non-negative: $moveType -> $weight"
            }
        }
    }

    private val moveWeightPairs = moveWeights.toList()

    private val totalWeight = moveWeights.values.sum()

    private val cumulativeProbability =
        moveWeightPairs
            .runningFold(0.0) { probability, pair ->
                val weight = pair.second
                probability + weight / totalWeight
            }.drop(1)

    fun select(schedule: Schedule): Move {
        val uniform = Random.nextDouble()
        val index = cumulativeProbability.indexOfFirst { probability -> uniform <= probability }
        val type = moveWeightPairs[index].first
        return MoveType.create(schedule, type)
    }
}

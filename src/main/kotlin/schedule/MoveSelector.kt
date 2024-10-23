package org.ruud.schedule

import schedule.MoveType
import kotlin.random.Random

class MoveSelector(
    private val weightMovePairs: List<Pair<Double, MoveType>>,
) {
    private val totalWeight = weightMovePairs.sumOf { (weight, _) -> weight }

    private val cumulativeProbability =
        weightMovePairs
            .runningFold(0.0) { probability, pair ->
                val weight = pair.first
                probability + weight / totalWeight
            }.drop(1)

    fun select(schedule: Schedule): Move {
        val uniform = Random.nextDouble()
        val index = cumulativeProbability.indexOfFirst { probability -> uniform <= probability }
        val type = weightMovePairs[index].second
        return MoveType.create(schedule, type)
    }
}

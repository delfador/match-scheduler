package org.ruud.schedule

class Schedule(
    rounds: List<Round>,
) {
    init {
        rounds.subList(1, rounds.size).forEach { round ->
            require(round.size == rounds.first().size) {
                "Rounds should have same number of players."
            }
            require(round.playersPerMatch == rounds.first().playersPerMatch) {
                "Rounds should have the same number of players per match."
            }
        }
    }

    private val _rounds = rounds.mapTo(mutableListOf()) { it.copy() }
    val rounds: List<Round> = _rounds

    fun swapRounds(
        i: Int,
        j: Int,
    ) {
        _rounds[i] = _rounds[j].also { _rounds[j] = _rounds[i] }
    }

    fun copy(): Schedule = Schedule(_rounds)

    override fun equals(other: Any?): Boolean = other is Schedule && _rounds == other._rounds

    override fun hashCode(): Int = _rounds.hashCode()

    override fun toString() =
        buildString {
            appendLine("SCHEDULE")
            rounds.forEachIndexed { index, round ->
                val roundLabel = "$index".padStart(2)
                appendLine("$roundLabel: $round")
            }
        }
}

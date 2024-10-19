package org.ruud.schedule

class Schedule(
    rounds: List<Round>,
) {
    fun swapRounds(
        i: Int,
        j: Int,
    ) {
        _rounds[i] = _rounds[j].also { _rounds[j] = _rounds[i] }
    }

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

    private val _rounds = rounds.toMutableList()
    val rounds: List<Round> = _rounds
}

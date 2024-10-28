package org.ruud.schedule

import kotlin.random.Random

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

    val numberOfRounds = rounds.size
    val numerOfPlayers = rounds.first().size
    val playersPerMatch = rounds.first().playersPerMatch

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

    fun toCsv(): String = rounds.joinToString("\n") { it.toCsv() }

    companion object {
        fun random(
            problem: Problem,
            random: Random = Random,
        ): Schedule =
            with(problem) {
                val rounds =
                    List(numberOfRounds) {
                        Round.random(
                            numberOfPlayers = numberOfPlayers,
                            playersPerMatch = playersPerMatch,
                            random = random,
                        )
                    }
                Schedule(rounds)
            }
    }
}

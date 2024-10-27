package org.ruud.schedule

import org.ruud.common.format
import org.ruud.common.lcm

data class Problem(
    val numberOfPlayers: Int,
    val numberOfRounds: Int,
    val playersPerMatch: Int,
) {
    init {
        check(numberOfPlayers > playersPerMatch)
        check(numberOfRounds > 1)
        check(playersPerMatch > 1)
    }

    val matchesPerRound = numberOfPlayers / playersPerMatch
    val playersPerRound = matchesPerRound * playersPerMatch
    val idlePlayersPerRound = numberOfPlayers - playersPerRound

    /** The number of rounds in which, ideally, every player should have exactly one idle round. */
    val cycleLength = lcm(numberOfPlayers, playersPerRound) / playersPerRound

    private val idealPlayingStreak =
        if (idlePlayersPerRound == 0) {
            Double.POSITIVE_INFINITY
        } else {
            numberOfPlayers.toDouble() / idlePlayersPerRound - 1.0
        }

    val acceptablePlayingStreak = idealPlayingStreak.toInt().let { it - 1..it + 1 }

    fun averageMatchesPlayed(numberOfRounds: Int): Double =
        numberOfRounds * matchesPerRound * playersPerMatch /
            numberOfPlayers.toDouble()

    fun averagePairs(numberOfRounds: Int): Double = averageMatchesPlayed(numberOfRounds) * (playersPerMatch - 1) / (numberOfPlayers - 1)

    override fun toString() =
        buildString {
            appendLine("PROBLEM")
            appendLine("Number of players: $numberOfPlayers")
            appendLine("Number of rounds: $numberOfRounds")
            appendLine("Payers per match: $playersPerMatch")
            appendLine("Playing players per round: $playersPerRound")
            appendLine("Idle players per round: $idlePlayersPerRound")
            if (idlePlayersPerRound > 0) {
                appendLine("Ideal playing streak: ${idealPlayingStreak.format(2)} rounds")
                appendLine("Acceptable playing streak: $acceptablePlayingStreak (excluding start/end boundaries)")
            }
            appendLine("Average pair frequency after $numberOfRounds rounds: ${averagePairs(numberOfRounds).format(2)}")
        }
}

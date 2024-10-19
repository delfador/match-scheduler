package org.ruud.schedule

import org.ruud.common.lcm

data class Problem(
    val numberOfPlayers: Int,
    val numberOfRounds: Int,
    val playersPerMatch: Int,
) {
    init {
        check(numberOfPlayers >= playersPerMatch)
        check(numberOfRounds > 0)
        check(playersPerMatch > 0)
    }

    val matchesPerRound = numberOfPlayers / playersPerMatch
    val playersPerRound = matchesPerRound * playersPerMatch
    val idlePlayersPerRound = numberOfPlayers - playersPerMatch

    /** The number of rounds in which, ideally, every player should have exactly one idle round. */
    val cycleLength = lcm(numberOfPlayers, playersPerRound) / playersPerRound

    fun averageMatchesPlayed(numberOfRounds: Int): Double = numberOfRounds * matchesPerRound * playersPerMatch / numberOfPlayers.toDouble()

    fun averagePairs(numberOfRounds: Int): Double =
        numberOfRounds.toDouble() / cycleLength * (cycleLength - 1) * (playersPerMatch - 1) / (numberOfPlayers - 1)

    override fun toString() =
        buildString {
            appendLine("PROBLEM")
            appendLine("Number of players: $numberOfPlayers")
            appendLine("Number of rounds: $numberOfRounds")
            appendLine("Number of players per match: $playersPerMatch")
            appendLine("Average pair frequency after $numberOfRounds rounds: ${averagePairs(numberOfRounds)}")
        }
}

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
}

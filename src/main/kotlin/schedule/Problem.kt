package org.ruud.schedule

import org.ruud.common.lcm

data class Problem(
    val numberOfPlayers: Int,
    val numberOfRounds: Int,
    val playersPerMatch: Int,
) {
    init {
        check(numberOfPlayers >= playersPerMatch)
    }

    val matchesPerRound = numberOfPlayers / playersPerMatch
    val playersPerRound = matchesPerRound * playersPerMatch
    val idlePlayersPerRound = numberOfPlayers - playersPerMatch

    /** The number of rounds in which, ideally, every player should have exactly one idle round. */
    val cycleLength = lcm(numberOfPlayers, playersPerRound) / playersPerRound
}

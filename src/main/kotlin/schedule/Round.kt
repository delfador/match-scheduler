package org.ruud.schedule

import org.ruud.common.pairs
import java.util.Collections

class Round(
    players: List<Int>,
    private val playersPerMatch: Int,
) {
    init {
        require(players.toSet() == players.indices.toSet())
        require(players.size >= playersPerMatch)
    }

    private val numberOfMatches = players.size / playersPerMatch
    private val numberPlaying = playersPerMatch * numberOfMatches
    private val players = players.toMutableList()

    val playing: List<Int>
        get() = players.subList(0, numberPlaying)

    val idle: List<Int>
        get() = players.subList(numberPlaying, players.size)

    val pairs: Collection<Pair<Int, Int>>
        get() =
            (0 until numberOfMatches).flatMap { index ->
                playersInMatch(index).pairs()
            }

    private fun playersInMatch(index: Int) = players.subList(index * playersPerMatch, (index + 1) * playersPerMatch)

    private fun allMatches() = List(numberOfMatches) { playersInMatch(it) }

    override fun toString() = (allMatches() + listOf(idle)).joinToString(", ") { it.toString() }

    fun swapPositions(
        i: Int,
        j: Int,
    ) {
        players[i] = players[j].also { players[j] = players[i] }
    }

    fun rotate(steps: Int) {
        Collections.rotate(players, steps)
    }
}

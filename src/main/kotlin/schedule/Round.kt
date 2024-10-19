package org.ruud.schedule

import org.ruud.common.pairs
import java.util.Collections
import kotlin.random.Random

class Round(
    players: List<Int>,
    val playersPerMatch: Int,
) {
    init {
        require(players.toSet() == players.indices.toSet())
        require(players.size >= playersPerMatch)
    }

    val size = players.size
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

    override fun equals(other: Any?): Boolean = other is Round && players == other.players

    override fun hashCode(): Int = players.hashCode()

    companion object {
        fun regular(
            numberOfPlayers: Int,
            playersPerMatch: Int,
            rotate: Int = 0,
        ): Round {
            val players = (0 until numberOfPlayers).toList()
            Collections.rotate(players, rotate)
            return Round(players, playersPerMatch)
        }

        fun random(
            numberOfPlayers: Int,
            playersPerMatch: Int,
            random: Random = Random,
        ): Round {
            val players = (0 until numberOfPlayers).shuffled()
            return Round(players, playersPerMatch)
        }
    }
}

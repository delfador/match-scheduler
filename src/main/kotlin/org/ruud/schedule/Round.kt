package org.ruud.schedule

import org.ruud.common.pairs
import java.util.Collections
import kotlin.random.Random

class Round(
    players: List<Int>,
    val playersPerMatch: Int,
) {
    init {
        require(players.distinct().size == players.size)
        require(players.size >= playersPerMatch)
    }

    val size = players.size

    private val numberOfMatches = players.size / playersPerMatch

    private val numberPlaying = playersPerMatch * numberOfMatches

    private val players = players.toMutableList()

    var playing: List<Int> = emptyList()
        private set

    var idle: List<Int> = emptyList()
        private set

    var pairs: Collection<Pair<Int, Int>> = emptyList()
        private set

    private fun update() {
        playing = players.subList(0, numberPlaying)

        idle = players.subList(numberPlaying, players.size)

        pairs =
            (0 until numberOfMatches).flatMap { index ->
                playersInMatch(index).pairs()
            }
    }

    init {
        update()
    }

    private fun playersInMatch(index: Int): List<Int> = players.subList(index * playersPerMatch, (index + 1) * playersPerMatch)

    fun allMatches() = List(numberOfMatches) { playersInMatch(it) }

    override fun toString() = (allMatches() + listOf(idle)).joinToString(", ") { it.toString() }

    fun toMarkdown() =
        buildString {
            append("|")
            (allMatches() + listOf(idle)).forEach { match ->
                append(match.joinToString(", ") { "$it" })
                append("|")
            }
        }

    fun toCsv() = players.joinToString(", ") { it.toString() }

    fun swapPositions(
        i: Int,
        j: Int,
    ) {
        players[i] = players[j].also { players[j] = players[i] }
        update()
    }

    fun rotate(steps: Int) {
        Collections.rotate(players, steps)
        update()
    }

    override fun equals(other: Any?): Boolean = other is Round && players == other.players

    override fun hashCode(): Int = players.hashCode()

    fun copy(): Round = Round(players, playersPerMatch)

    companion object {
        fun regular(
            numberOfPlayers: Int,
            playersPerMatch: Int,
            rotate: Int = 0,
        ): Round {
            val players = (1..numberOfPlayers).toList()
            Collections.rotate(players, rotate)
            return Round(players, playersPerMatch)
        }

        fun random(
            numberOfPlayers: Int,
            playersPerMatch: Int,
            random: Random = Random,
        ): Round {
            val players = (1..numberOfPlayers).shuffled(random)
            return Round(players, playersPerMatch)
        }
    }
}

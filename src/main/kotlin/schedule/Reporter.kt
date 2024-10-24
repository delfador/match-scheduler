package org.ruud.schedule

import org.ruud.score.PlayingStreak

class Reporter(
    private val problem: Problem,
) {
    fun report(schedule: Schedule) =
        buildString {
            appendLine(problem)

            appendLine(schedule.toString())

            appendLine("MATCHES PLAYED")
            val matchesPlayed = RoundFrequency(schedule) { it.playing }
            for (index in schedule.rounds.indices) {
                val roundLabel = "$index".padStart(2)
                appendLine("$roundLabel: ${matchesPlayed.frequencies(0, index + 1)}")
            }

            appendLine()
            appendLine("PLAYING STREAKS (excluding start/end boundaries)")
            val playingStreaksByPlayer = PlayingStreak.playingStreaksByPlayer(schedule).toSortedMap()
            playingStreaksByPlayer.forEach { (player, streaks) ->
                val minimumStreak = streaks.minOrNull()
                val maximumStreak = streaks.maxOrNull()
                appendLine("${player.toString().padStart(2)}: $streaks (min: $minimumStreak, max: $maximumStreak)")
            }

            val pairsFrequency = RoundFrequency(schedule) { it.pairs }

            appendLine()
            appendLine(pairFrequencyReport(pairsFrequency, problem.numberOfRounds))
        }

    private fun pairFrequencyReport(
        pairsFrequency: RoundFrequency<Pair<Int, Int>>,
        numberOfRounds: Int,
    ) = buildString {
        val frequencies = pairsFrequency.frequencies(to = numberOfRounds).toList()
        val pairsByFrequency =
            frequencies
                .groupBy { (_, frequency) -> frequency }
                .toSortedMap()
                .mapValues { (_, freqPair) ->
                    freqPair.map { it.first }.sortedBy { it.first }
                }

        appendLine("PAIR FREQUENCY AFTER $numberOfRounds ROUNDS")
        pairsByFrequency.forEach { (frequency, pairs) ->
            appendLine("$frequency: $pairs")
        }
    }
}

package org.ruud.schedule

import org.ruud.schedule.score.PlayingStreak

class Reporter(
    private val problem: Problem,
) {
    fun report(schedule: Schedule) =
        buildString {
            appendLine(problem)
            appendLine(schedule)
            appendLine(matchesPlayed(schedule))
            appendLine(playingStreaks(schedule))
            appendLine(pairFrequency(schedule))
            appendLine(matchFrequency(schedule))
        }

    private fun matchesPlayed(schedule: Schedule) =
        buildString {
            appendLine("MATCHES PLAYED")
            val matchesPlayed = RoundFrequency(schedule) { it.playing }
            for (index in schedule.rounds.indices) {
                val roundLabel = "$index".padStart(2)
                appendLine("$roundLabel: ${matchesPlayed.frequencies(0, index + 1)}")
            }
        }

    private fun playingStreaks(schedule: Schedule) =
        buildString {
            appendLine("PLAYING STREAKS (excluding start/end boundaries)")
            val playingStreaksByPlayer = PlayingStreak(schedule).playingStreaksByPlayer()
            playingStreaksByPlayer.forEach { (player, streaks) ->
                val minimumStreak = streaks.minOrNull()
                val maximumStreak = streaks.maxOrNull()
                appendLine("${player.toString().padStart(2)}: $streaks (min: $minimumStreak, max: $maximumStreak)")
            }
        }

    private fun pairFrequency(schedule: Schedule) =
        buildString {
            val pairsFrequency = RoundFrequency(schedule) { it.pairs }
            val frequencies = pairsFrequency.frequencies().toList()
            val pairsByFrequency =
                frequencies
                    .groupBy { (_, frequency) -> frequency }
                    .toSortedMap()
                    .mapValues { (_, freqPair) ->
                        freqPair.map { it.first }.sortedBy { it.first }
                    }

            appendLine("PAIR FREQUENCY")
            pairsByFrequency.forEach { (frequency, pairs) ->
                appendLine("$frequency: $pairs")
            }
        }

    private fun matchFrequency(schedule: Schedule): String {
        val matchFrequencies =
            schedule
                .rounds
                .flatMap { round ->
                    round.allMatches().map { it.sorted() }
                }.groupingBy { it }
                .eachCount()

        val matchesByFrequency =
            matchFrequencies
                .toList()
                .groupBy { (_, frequency) -> frequency }
                .toSortedMap()
                .mapValues { (_, freqPair) ->
                    val matches = freqPair.map { it.first }
                    matches
                }

        return buildString {
            appendLine("MATCH FREQUENCIES")
            matchesByFrequency.forEach { (frequency, matches) ->
                val matchString =
                    if (matches.size > 4) {
                        "${matches.size} matches"
                    } else {
                        matches.toString()
                    }
                appendLine("$frequency: $matchString")
            }
        }
    }
}

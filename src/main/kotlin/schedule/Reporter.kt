package org.ruud.schedule

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

            val pairsFrequency = RoundFrequency(schedule) { it.pairs }

            val roundsToReport =
                (
                    (problem.cycleLength until problem.numberOfRounds step problem.cycleLength).toList() +
                        listOf(problem.numberOfRounds)
                ).distinct()
            appendLine()
            roundsToReport.forEach { numberOfRounds ->
                appendLine(pairFrequencyReport(pairsFrequency, numberOfRounds))
            }
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

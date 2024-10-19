package org.ruud.score

import org.ruud.schedule.Schedule

interface ScorerFactory {
    fun create(schedule: Schedule): Scorer
}

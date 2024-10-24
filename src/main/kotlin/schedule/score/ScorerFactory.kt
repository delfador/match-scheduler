package org.ruud.schedule.score

import org.ruud.schedule.Schedule

interface ScorerFactory {
    fun create(schedule: Schedule): Scorer
}

package org.ruud.schedule.move

import org.ruud.schedule.Schedule

interface Move {
    fun execute(schedule: Schedule)

    fun undo(schedule: Schedule)
}

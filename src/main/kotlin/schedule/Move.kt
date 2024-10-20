package org.ruud.schedule

interface Move {
    fun execute(schedule: Schedule)

    fun undo(schedule: Schedule)
}

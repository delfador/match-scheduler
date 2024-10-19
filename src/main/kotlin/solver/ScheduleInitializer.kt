package org.ruud.solver

import org.ruud.schedule.Schedule

interface ScheduleInitializer {
    fun create(): Schedule
}

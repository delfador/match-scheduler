package org.ruud.schedule

import org.ruud.solver.IterLog

data class Result(
    val schedule: Schedule,
    val score: Double,
    val detailScore: String,
    val iterLogs: List<IterLog>? = null,
)

package org.ruud.solver

interface Observer {
    fun accept(iterLog: IterLog)
}

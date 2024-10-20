package org.ruud.solver

interface Solution<T> {
    fun score(): Double

    fun copy(): T

    fun move()

    fun undoMove()
}

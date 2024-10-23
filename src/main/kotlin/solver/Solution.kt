package solver

interface Solution<S, M> {
    fun score(): Double

    fun detailScore(): String

    fun randomMove(): M

    fun execute(move: M)

    fun undo(move: M)

    fun copy(): Solution<S, M>

    fun getState(): S
}

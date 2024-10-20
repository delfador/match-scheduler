package org.ruud.common

import java.util.Locale

fun <T : Comparable<T>> Iterable<T>.pairs(): Collection<Pair<T, T>> {
    val sortedList = sorted()

    if (sortedList.size < 2) {
        return emptyList()
    }

    return buildList {
        sortedList.forEachIndexed { index, value1 ->
            sortedList.subList(index + 1, sortedList.size).forEach { value2 ->
                add(Pair(value1, value2))
            }
        }
    }
}

fun Double.format(decimals: Int = 2): String = "%.${decimals}f".format(locale = Locale.ENGLISH, this)

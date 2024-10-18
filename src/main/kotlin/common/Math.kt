package org.ruud.common

/** Greatest common divisor. */
fun gcd(
    a: Int,
    b: Int,
): Int {
    var x = a
    var y = b
    while (y > 0) {
        y = (x % y).also { x = y }
    }

    return x
}

/** Least common multiple. */
fun lcm(
    a: Int,
    b: Int,
): Int = a * (b / gcd(a, b))

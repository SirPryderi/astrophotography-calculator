package io.github.sirpryderi.astrophotographycalculator.model

import kotlin.math.log2
import kotlin.math.pow

fun exposureValue(aperture: Double, speed: Double, iso: Int): Double {
    return log2(aperture.pow(2.0) / speed / (iso / 100.0))
}
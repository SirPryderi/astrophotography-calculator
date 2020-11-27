package com.inducesmile.astrophotographycalculator.model

import kotlin.math.abs

private fun IntArray.findClosest(value: Int) = minByOrNull { abs(value - it) }!!
private val roundedValues = intArrayOf(100, 125, 160, 200, 250, 320, 400, 500, 640, 800, 1000, 1250, 1600, 2000, 2500, 3200, 4000, 5100, 6400, 8100, 10200, 12800, 16100, 20300, 25600)

class Iso(value: Int) {
    val value = roundedValues.findClosest(value)
}
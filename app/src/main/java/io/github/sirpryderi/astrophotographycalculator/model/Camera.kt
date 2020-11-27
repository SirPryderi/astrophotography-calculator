package io.github.sirpryderi.astrophotographycalculator.model

import kotlin.math.cos
import kotlin.math.log2
import kotlin.math.pow

class Camera(val brand: String,
             val model: String,
             val maximumPdr: Double?,
             val lowLightIso: Int?,
             val lowLightEv: Double?,
             val readNoiseIso: Int?,
             val sensorWidth: Double,
             val sensorHeight: Double,
             val pixelWidth: Int,
             val pixelHeight: Int,
) {
    fun megaPixels(): Double {
        return pixelHeight * pixelWidth / 1_000.0
    }

    fun pixelPitch(): Double {
        return sensorWidth / pixelWidth * 1_000.0
    }

    fun circleOfConfusion(): Double {
        return sensorWidth / pixelWidth * 2
    }

    fun diffraction(): Double {
        return circleOfConfusion() * 1_000 / (2.43932 * 0.00000055 * 1000_000)
    }

    fun exposureValue(aperture: Double, speed: Double, iso: Int): Double {
        return log2(aperture.pow(2.0)/speed/(iso/100.0))
    }

    // https://sahavre.fr/wp/regle-npf-rule/
    fun maxExposureTime(aperture: Double, focalLength: Double, accuracy: Int = 1, declination: Double = 0.0): Double {
        return accuracy *
               (16.856 * aperture + 0.0997 * focalLength + 13.713 * pixelPitch()) /
               (focalLength * cos(declination))
    }

    override fun toString(): String {
        return """$brand $model"""
    }
}
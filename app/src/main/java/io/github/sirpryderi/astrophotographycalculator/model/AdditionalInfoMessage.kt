package io.github.sirpryderi.astrophotographycalculator.model

import android.content.Context
import androidx.core.text.HtmlCompat
import io.github.sirpryderi.astrophotographycalculator.R
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

fun additionalInfoMessage(context: Context?, camera: Camera, ev: Double): Message {
    val f = DecimalFormat("0.00")
    val fs = DecimalFormat("0.00E0")

    // TODO: convert to resource
    val format = "<b>Exposure</b>" +
            "<p>Exposure Value: ${f.format(ev)} EV</p>" +
            "<p>eq. Luminance: ${fs.format(exposureValueToIlluminance(ev))} lx</p>" +
            "<p>eq. Apparent Magnitude: ${f.format(exposureValueToApparentMagnitude(ev))}</p>" +
            "<br><b>Camera</b>" +
            "<p>Resolution: ${f.format(camera.megaPixels())} MP</p>" +
            "<p>Crop Factor ${f.format(camera.cropFactor())}</p>" +
            "<p>Pixel pitch: ${f.format(camera.pixelPitch())} μm/p</p>" +
            "<p>Minimize diffraction: f/${f.format(camera.diffraction())}</p>" +
            "<p>Read Noise ISO: ${camera.readNoiseIso}</p>" +
            "Low Light ISO: ${camera.lowLightIso}"

    val text = HtmlCompat.fromHtml(format, HtmlCompat.FROM_HTML_MODE_COMPACT)
    return Message(text, R.drawable.ic_help_24)
}


fun exposureValueToIlluminance(ev: Double): Double {
    // TODO: I'm not sure if I should consider this reflected or incident light
    // return 2.5 * (2.0).pow(ev)
    return (2.0).pow(ev * 3)
}

fun exposureValueToApparentMagnitude(ev: Double): Double {
    return illuminanceToApparentMagnitude(exposureValueToIlluminance(ev));
}

// https://physics.stackexchange.com/a/340245
// https://www.wikiwand.com/en/Illuminance#Astronomy
fun illuminanceToApparentMagnitude(illuminance: Double): Double {
    return -(2.5 * log10(illuminance)) - 14.18
}

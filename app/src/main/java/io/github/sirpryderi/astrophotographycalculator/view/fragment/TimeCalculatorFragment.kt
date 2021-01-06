package io.github.sirpryderi.astrophotographycalculator.view.fragment

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.sirpryderi.astrophotographycalculator.R
import io.github.sirpryderi.astrophotographycalculator.model.*
import kotlin.math.abs
import kotlin.math.roundToInt


class TimeCalculatorFragment : AbstractCalculator() {
    private var exposureTimeWarning: View? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exposureTimeWarning = view.findViewById(R.id.message_exposure_time_warning)

        val watcher = getOnChangeWatcher()
        cameraText?.addTextChangedListener(watcher)
        apertureText?.addTextChangedListener(watcher)
        focalLengthText?.addTextChangedListener(watcher)
        declinationText?.addTextChangedListener(watcher)
        isoSlider?.addOnChangeListener {_, _, _ -> onChange()}

        calculate()
    }

    override fun calculate() {
        val camera = getCamera()

        val aperture = apertureText?.text?.toString()?.toDoubleOrNull()
        val focalLength = focalLengthText?.text?.toString()?.toDoubleOrNull()
        val declinationInDeg = declinationText?.text?.toString()?.toDoubleOrNull() ?: 0.0
        val declinationInRad = (declinationInDeg / 180 * Math.PI).coerceIn(-Math.PI / 2.0, Math.PI / 2.0)
        val iso = isoSlider?.value?.toInt()

        if (aperture == null || iso == null || focalLength == null || camera == null) return

        val speed = camera.maxExposureTime(aperture, focalLength)

        val ev = exposureValue(aperture, speed, iso)
        val evPercentage = evToPercentage(ev)

        if (speed > camera.maxExposureTime400Rule(focalLength)) {
            exposureTimeWarning?.visibility = View.VISIBLE
        } else {
            exposureTimeWarning?.visibility = View.INVISIBLE
        }

        exposureValueProgress?.setIndicatorColor(progressBarColor(evPercentage))

        isoText?.text = getString(R.string.iso, Iso(iso).value)
        exposureValue?.text = getString(R.string.exposure_value_f, ev)
        exposureValueProgress?.progress = (evPercentage * 100).roundToInt()
        exposureTimeText?.setText("%.2f".format(camera.maxExposureTime(aperture, focalLength, declination = declinationInRad)))
    }

    private fun evToPercentage(ev: Double): Float {
        val max = -2.0
        val min = -14.0

        if (ev < min) {return 1f}
        if (ev > max) {return 0f}

        return (1.0 - abs(ev-min) / abs(max - min)).toFloat()
    }

    private fun progressBarColor(evPercentage: Float): Int {
        val primaryColor = this.requireContext().resources.getColor(R.color.colorPrimary)
        var finalColor = primaryColor

        when {
            evPercentage > 0.5 -> {
                finalColor = ArgbEvaluator().evaluate(evPercentage - 0.5f, primaryColor, 0xffffff) as Int
            }
            evPercentage < 0.5  -> {
                finalColor = ArgbEvaluator().evaluate(0.5f - evPercentage, primaryColor, 0xff0000) as Int
            }
        }

        return finalColor
    }
}
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
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val watcher = getOnChangeWatcher()
        cameraText?.addTextChangedListener(watcher)
        apertureText?.addTextChangedListener(watcher)
        focalLengthText?.addTextChangedListener(watcher)
        declinationText?.addTextChangedListener(watcher)
        starTrailsText?.addTextChangedListener(watcher)
        isoSlider?.addOnChangeListener { _, _, _ -> onChange() }

        calculate()
    }

    override fun calculate() {
        val messages = mutableListOf<Message>()

        val camera = getCamera()

        val aperture = apertureText?.text?.toString()?.toDoubleOrNull()
        val focalLength = focalLengthText?.text?.toString()?.toDoubleOrNull()
        val declinationInDeg = declinationText?.text?.toString()?.toDoubleOrNull() ?: 0.0
        val declinationInRad = (declinationInDeg / 180 * Math.PI).coerceIn(-Math.PI / 2.0, Math.PI / 2.0)
        val iso = isoSlider?.value?.toInt()
        val k = kValue()

        if (camera == null) {
            setMessages(Message(getString(R.string.error_camera_not_valid), R.drawable.ic_error_24))
            return
        }

        if (aperture == null || iso == null || focalLength == null || k == null) {
            setMessages(Message(getString(R.string.error_field_not_valid), R.drawable.ic_error_24))
            return
        }

        val speed = camera.maxExposureTime(aperture, focalLength, k, declination = declinationInRad)
        val ev = exposureValue(aperture, speed, iso)
        val evPercentage = evToPercentage(ev)

        if (speed > camera.maxExposureTime400Rule(focalLength))
            messages.add(Message(getString(R.string.warning_bad_exposure_time)))
        if (ev > -8)
            messages.add(Message(getString(R.string.warning_underexposed)))
        if (ev < -11)
            messages.add(Message(getString(R.string.warning_overexposed)))
        if (messages.isEmpty())
            messages.add(Message(getString(R.string.no_warning), R.drawable.ic_check_24))

        setMessages(messages)

        exposureValueProgress?.setIndicatorColor(progressBarColor(evPercentage))

        isoText?.text = getString(R.string.iso, Iso(iso).value)
        exposureValue?.text = getString(R.string.exposure_value_f, ev)
        exposureValueProgress?.progress = (evPercentage * 100).roundToInt()
        exposureTimeText?.setText("%.2f".format(speed))
    }

    private fun evToPercentage(ev: Double): Float {
        val max = -2.0
        val min = -14.0

        when {
            (ev < min) -> return 1f
            (ev > max) -> return 0f
        }

        return (1.0 - abs(ev - min) / abs(max - min)).toFloat()
    }

    private fun progressBarColor(evPercentage: Float): Int {
        val primaryColor = this.requireContext().resources.getColor(R.color.colorPrimary)
        var finalColor = primaryColor

        when {
            evPercentage > 0.5 -> {
                finalColor = ArgbEvaluator().evaluate(evPercentage - 0.5f, primaryColor, 0xffffff) as Int
            }
            evPercentage < 0.5 -> {
                finalColor = ArgbEvaluator().evaluate(0.5f - evPercentage, primaryColor, 0xff0000) as Int
            }
        }

        return finalColor
    }
}
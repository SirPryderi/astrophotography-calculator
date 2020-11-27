package com.inducesmile.astrophotographycalculator.view.fragment

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.inducesmile.astrophotographycalculator.R
import com.inducesmile.astrophotographycalculator.model.*
import com.inducesmile.astrophotographycalculator.view.component.IsoSlider
import kotlin.math.abs
import kotlin.math.roundToInt


class CalculatorFragment : Fragment() {
    private var cameraText: AutoCompleteTextView? = null
    private var apertureText: EditText? = null
    private var focalLengthText: EditText? = null
    private var exposureTimeText: EditText? = null

    private var isoSlider: IsoSlider? = null
    private var exposureValue: TextView? = null
    private var isoText: TextView? = null
    private var exposureValueProgress: LinearProgressIndicator? = null

    private var cameras: ArrayList<Camera> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameras = loadCameras(context!!)

        val adapter = ArrayAdapter(context!!, R.layout.camera_menu_item, cameras.toCameraNames())

        cameraText = view.findViewById(R.id.textview_camera)
        apertureText = view.findViewById(R.id.textview_aperture)
        focalLengthText = view.findViewById(R.id.textview_focal_length)
        exposureTimeText = view.findViewById(R.id.textview_exposure_time)

        exposureValue = view.findViewById(R.id.textview_exposure_value)
        exposureValueProgress = view.findViewById(R.id.progress_exposure_value)
        isoSlider = view.findViewById(R.id.slider_iso)
        isoText = view.findViewById(R.id.textview_iso)

        cameraText?.setAdapter(adapter)

        if (cameraText?.text.isNullOrBlank()) {
            cameraText?.setText(cameras[0].toString())
        }

        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
//                TODO("Not yet implemented")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateEv()
            }
        }

        cameraText?.addTextChangedListener(watcher)
        apertureText?.addTextChangedListener(watcher)
        focalLengthText?.addTextChangedListener(watcher)
        isoSlider?.addOnChangeListener { _, _, _ ->  calculateEv()}

        calculateEv()

//        view.findViewById<View>(R.id.button_first).setOnClickListener {
//            NavHostFragment.findNavController(this@CalculatorFragment)
//                    .navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    private fun calculateEv() {
        val camera = cameras.find(cameraText?.text.toString())

        val aperture = apertureText?.text?.toString()?.toDoubleOrNull()
        val focalLength = focalLengthText?.text?.toString()?.toDoubleOrNull()
        val iso = isoSlider?.value?.toInt()

        if (aperture == null || iso == null || focalLength == null || camera == null) return

        val speed = camera.maxExposureTime(aperture, focalLength)

        val ev = camera.exposureValue(aperture, speed, iso)
        val evPercentage = evToPercentage(ev)

        exposureValueProgress?.setIndicatorColor(progressBarColor(evPercentage))

        isoText?.text = getString(R.string.iso, Iso(iso).value)
        exposureValue?.text = getString(R.string.exposure_value, ev)
        exposureValueProgress?.progress = (evPercentage * 100).roundToInt()
        exposureTimeText?.setText("%.2f".format(camera.maxExposureTime(aperture, focalLength)))
    }

    private fun evToPercentage(ev: Double): Float {
        val max = -2.0
        val min = -14.0

        if (ev < min) {return 1f}
        if (ev > max) {return 0f}

        return (1.0 - abs(ev-min) / abs(max - min)).toFloat()
    }

    private fun progressBarColor(evPercentage: Float): Int {
        val primaryColor = this.context!!.resources.getColor(R.color.colorPrimary)
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
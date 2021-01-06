package io.github.sirpryderi.astrophotographycalculator.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.sirpryderi.astrophotographycalculator.R
import io.github.sirpryderi.astrophotographycalculator.model.Iso
import io.github.sirpryderi.astrophotographycalculator.model.exposureValue


class EVCalculatorFragment : AbstractCalculator() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ev_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val watcher = getOnChangeWatcher()
        apertureText?.addTextChangedListener(watcher)
        exposureTimeText?.addTextChangedListener(watcher)
        isoSlider?.addOnChangeListener { _, _, _ -> onChange() }

        calculate()
    }

    override fun calculate() {
        val aperture = apertureText?.text?.toString()?.toDoubleOrNull()
        val speed = exposureTimeText?.text?.toString()?.toDoubleOrNull()
        val iso = isoSlider?.value?.toInt()

        if (aperture == null || iso == null || speed == null) return

        val ev = exposureValue(aperture, speed, iso)

        isoText?.text = getString(R.string.iso, Iso(iso).value)
        exposureValue?.text = getString(R.string.exposure_value_f, ev)
    }
}
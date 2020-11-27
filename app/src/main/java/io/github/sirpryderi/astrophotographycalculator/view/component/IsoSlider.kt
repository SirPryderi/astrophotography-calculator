package io.github.sirpryderi.astrophotographycalculator.view.component

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.slider.Slider
import io.github.sirpryderi.astrophotographycalculator.model.Iso
import kotlin.math.pow
import kotlin.math.roundToInt

class IsoSlider(context: Context, attrs: AttributeSet) : Slider(context, attrs) {
    init {
        valueFrom=0f
        valueTo=8f
        stepSize=1/3f
        setLabelFormatter { """${Iso(value.roundToInt()).value} ISO""" }
    }

    override fun getValue(): Float {
        return (2.0.pow(super.getValue().toDouble()) * 100).toFloat()
    }

    override fun getTooltipText(): CharSequence? {
        return (value * 100).toString()
    }
}
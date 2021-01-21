package io.github.sirpryderi.astrophotographycalculator.view.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import io.github.sirpryderi.astrophotographycalculator.R
import io.github.sirpryderi.astrophotographycalculator.view.helper.getStateColour
import io.github.sirpryderi.astrophotographycalculator.view.helper.getThemeColour


class LightMeter(context: Context, attrs: AttributeSet) : android.view.View(context, attrs) {
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mWidth = width
    private var mHeight = height
    private val minHeight = 210

    var targetEv = -9f
        set(value) {
            field = value
            invalidate()
        }
    var ev: Float = -9f
        set(value) {
            field = value
            invalidate()
        }

    init {
        textPaint.color = getStateColour(R.color.material_on_surface_emphasis_high_type)
        textPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = 40f

        strokePaint.color = getThemeColour(R.attr.colorPrimary)
        strokePaint.strokeWidth = 10f
        minimumHeight = minHeight
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // config
        val stops = 4
        val subStops = 2
        val tickHeight = 30
        val padding = 100
        val indicatorOffset = 10
        val labelOffset = 15

        // utility variables
        val x = mWidth / 2.0f
        val y = mHeight / 2.0f + 15f

        val barWidth = mWidth - padding
        val unit = (1f / stops) * ((barWidth) / 2f) // distance between 1 stop
        val deltaEv = (targetEv - ev).coerceIn(-stops.toFloat(), stops.toFloat())
        val needleX = x + deltaEv * unit
        var label = ""

        // stop marks
        for (stop in -stops..stops) {
            val stopX = x + stop * unit
            label = stop.toString()
            canvas?.drawText(label, 0, label.length, stopX, y - tickHeight - labelOffset, textPaint)
            canvas?.drawLine(stopX, y, stopX, y - tickHeight, strokePaint)

            // no sub stops after the last stop
            if (stop == stops) continue

            // sub stop marks
            for (subStop in 1..subStops) {
                val subUnit = unit / (subStops + 1)
                val subStopX = stopX + subStop * subUnit
                canvas?.drawLine(subStopX, y, subStopX, y - tickHeight / 2, strokePaint)
            }
        }

        canvas?.drawLine(needleX, y + indicatorOffset, needleX, y + indicatorOffset + tickHeight * 2, strokePaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w
        mHeight = h
    }
}
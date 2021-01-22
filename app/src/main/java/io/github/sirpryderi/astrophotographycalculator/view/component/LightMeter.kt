package io.github.sirpryderi.astrophotographycalculator.view.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import io.github.sirpryderi.astrophotographycalculator.R
import io.github.sirpryderi.astrophotographycalculator.view.helper.getStateColour
import io.github.sirpryderi.astrophotographycalculator.view.helper.preferences
import java.text.DecimalFormat
import kotlin.math.roundToInt


class LightMeter(context: Context, attrs: AttributeSet) : android.view.View(context, attrs) {
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokeWidth = 10f

    private var mWidth = width
    private var mHeight = height
    private val minHeight = 210

    // preferences
    private val pref = preferences()
    private val stops = pref.getInt("light_meter_steps", 4)
    private val subStops = pref.getInt("light_meter_sub_steps", 2)
    private val showNumbers = pref.getBoolean("light_meter_show_numbers", true)
    private val indicatorStops = 9
    private val tickHeight = 30
    private val padding = 100
    private val indicatorOffset = 10
    private val labelOffset = 15

    private val stopFormatting = DecimalFormat("+#;-#")

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
        minimumHeight = minHeight

        textPaint.color = getStateColour(R.color.material_on_surface_emphasis_medium)
        textPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = 35f

        strokePaint.color = getStateColour(R.color.colorPrimary)
        strokePaint.strokeWidth = strokeWidth
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // utility variables
        val x = mWidth / 2.0f
        val y = mHeight / 2.0f + 15f

        val barWidth = mWidth - padding
        val unit = (1f / stops) * ((barWidth) / 2f) // distance between 1 stop
        val deltaEv = (targetEv - ev).coerceIn(-stops.toFloat() - 1, stops.toFloat() + 1)

        var label: CharSequence

        // stop marks
        for (stop in -stops..stops) {
            val stopX = x + stop * unit
            if (showNumbers) {
                label = if (stop == 0) "0" else stopFormatting.format(stop)
                canvas?.drawText(label, 0, label.length, stopX, y - tickHeight - labelOffset, textPaint)
            }
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

        // indicator
        strokePaint.strokeWidth = strokeWidth / 2
        canvas?.drawLine(x, y + indicatorOffset, x, y + indicatorOffset + tickHeight * 2, strokePaint)
        val end = (deltaEv * indicatorStops).roundToInt()
        val range = if (deltaEv < 0) (0 downTo end) else (0..end)
        val offset = 5
        for (step in range) {
            val needleX = x + (step / indicatorStops.toFloat()) * unit
            canvas?.drawLine(needleX, y + indicatorOffset + offset, needleX, y + indicatorOffset + tickHeight * 2 - offset, strokePaint)
        }
        strokePaint.strokeWidth = strokeWidth
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w
        mHeight = h
    }
}
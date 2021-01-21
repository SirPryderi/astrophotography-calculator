package io.github.sirpryderi.astrophotographycalculator.view.helper

import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

@ColorInt
fun View.getThemeColour(@AttrRes id: Int): Int {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(id, typedValue, true)
    return typedValue.data
}

@ColorInt
fun View.getStateColour(@ColorRes colour: Int): Int {
    return ContextCompat.getColorStateList(context, colour)?.getColorForState(drawableState, colour)
            ?: ContextCompat.getColor(context, colour)
}

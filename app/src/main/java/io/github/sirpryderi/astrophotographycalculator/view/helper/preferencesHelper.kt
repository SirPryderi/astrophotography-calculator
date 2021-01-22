package io.github.sirpryderi.astrophotographycalculator.view.helper

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager

fun Context.preferences(): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(this)
}

fun View.preferences(): SharedPreferences {
    return context.preferences()
}

fun Fragment.preferences(): SharedPreferences {
    return requireContext().preferences()
}
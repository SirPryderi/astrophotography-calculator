package io.github.sirpryderi.astrophotographycalculator.view.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import io.github.sirpryderi.astrophotographycalculator.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}
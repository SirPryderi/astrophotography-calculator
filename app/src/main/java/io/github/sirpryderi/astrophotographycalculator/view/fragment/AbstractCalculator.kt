package io.github.sirpryderi.astrophotographycalculator.view.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import io.github.sirpryderi.astrophotographycalculator.R
import io.github.sirpryderi.astrophotographycalculator.model.*
import io.github.sirpryderi.astrophotographycalculator.view.component.IsoSlider

abstract class AbstractCalculator : Fragment() {
    protected var cameraText: AutoCompleteTextView? = null
    protected var apertureText: EditText? = null
    protected var focalLengthText: EditText? = null
    protected var exposureTimeText: EditText? = null
    protected var declinationText: EditText? = null
    protected var starTrailsText: AutoCompleteTextView? = null

    protected var isoSlider: IsoSlider? = null
    protected var exposureValue: TextView? = null
    protected var isoText: TextView? = null
    protected var exposureValueProgress: LinearProgressIndicator? = null

    protected var messageListFragment: RecyclerView? = null

    protected var cameras: ArrayList<Camera> = ArrayList()

    override fun onDestroyView() {
        // stores the current values in the form
        setPreviousValues()
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameras = loadCameras(requireContext())

        val cameraAdapter = ArrayAdapter(requireContext(), R.layout.camera_menu_item, cameras.toCameraNames())
        val starTrailsAdapter = ArrayAdapter(requireContext(), R.layout.camera_menu_item, trailsText().keys.toTypedArray())

        cameraText = view.findViewById(R.id.textview_camera)
        apertureText = view.findViewById(R.id.textview_aperture)
        focalLengthText = view.findViewById(R.id.textview_focal_length)
        exposureTimeText = view.findViewById(R.id.textview_exposure_time)
        declinationText = view.findViewById(R.id.textview_declination)
        starTrailsText = view.findViewById(R.id.textview_star_trails)

        exposureValue = view.findViewById(R.id.textview_exposure_value)
        exposureValueProgress = view.findViewById(R.id.progress_exposure_value)
        isoSlider = view.findViewById(R.id.slider_iso)
        isoText = view.findViewById(R.id.textview_iso)

        messageListFragment = view.findViewById(R.id.id_message_list)

        cameraText?.setAdapter(cameraAdapter)
        starTrailsText?.setAdapter(starTrailsAdapter)

        // populates the field with the last used values (or default ones)
        getPreviousValues()
    }

    private fun getPreviousValues() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        cameraText?.setText(sharedPref.getString("previous_camera", getString(R.string.default_camera)), false)
        starTrailsText?.setText(sharedPref.getString("previous_star_trails", getString(R.string.star_trails_0)), false)
        apertureText?.setText(sharedPref.getString("previous_aperture", getString(R.string.default_aperture)))
        focalLengthText?.setText(sharedPref.getString("previous_focal_length", getString(R.string.default_focal_length)))
        declinationText?.setText(sharedPref.getString("previous_declination", getString(R.string.default_declination)))
        exposureTimeText?.setText(sharedPref.getString("previous_exposure_time", getString(R.string.default_exposure_time)))
    }

    private fun setPreviousValues() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        with(sharedPref.edit()) {
            if (cameraText != null) putString("previous_camera", cameraText?.text.toString())
            if (apertureText != null) putString("previous_aperture", apertureText?.text.toString())
            if (focalLengthText != null) putString("previous_focal_length", focalLengthText?.text.toString())
            if (declinationText != null) putString("previous_declination", declinationText?.text.toString())
            if (exposureTimeText != null) putString("previous_exposure_time", exposureTimeText?.text.toString())
            if (starTrailsText != null) putString("previous_star_trails", starTrailsText?.text.toString())
            apply()
        }
    }

    protected fun getCamera(): Camera? {
        return cameras.find(cameraText?.text.toString())
    }

    protected fun onChange() {
        calculate()
        setPreviousValues()
    }

    protected fun getOnChangeWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = onChange()
    }

    protected fun setMessages(message: Message) {
        messageListFragment?.adapter = MessageListRecyclerViewAdapter(listOf(message))
    }

    protected fun setMessages(messages: List<Message>) {
        messageListFragment?.adapter = MessageListRecyclerViewAdapter(messages)
    }

    private fun trailsText(): Map<String, Float> {
        return mapOf(getString(R.string.star_trails_0) to 1.0f,
                getString(R.string.star_trails_1) to 1.5f,
                getString(R.string.star_trails_2) to 2.0f,
                getString(R.string.star_trails_3) to 2.5f,
                getString(R.string.star_trails_4) to 3.0f)
    }

    protected fun kValue(): Float? {
        if (starTrailsText == null) return null
        val strValue = starTrailsText?.text.toString()
        return trailsText()[strValue]
    }

    protected abstract fun calculate()
}
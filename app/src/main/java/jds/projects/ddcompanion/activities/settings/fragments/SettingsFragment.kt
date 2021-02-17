package jds.projects.ddcompanion.activities.settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.utils.JDPreferences

class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        viewModel =
            SettingsViewModel(
                requireContext()
            )
        val spnLanguages = root.findViewById<Spinner>(R.id.spinner_activity_settings__languages)
        spnLanguages.adapter = viewModel.languagesAdapter
        spnLanguages.setSelection(viewModel.selectedLanguagePosition)
        spnLanguages.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (viewModel.newLanguageSelection(position)) {
                    JDPreferences.getInstance(requireContext())
                        .setLanguage(viewModel.selectedLanguage)
                    DBInterface.getInstance(requireContext())
                        .setUsedLanguage(viewModel.selectedLanguage.id)
                }
            }
        }
        val switchDayNight =
            root.findViewById<SwitchCompat>(R.id.switch_activity_settings__daynight)
        switchDayNight.isChecked = viewModel.dayNightMode == AppCompatDelegate.MODE_NIGHT_YES
        switchDayNight.setOnCheckedChangeListener { _, isChecked ->
            run {
                if (isChecked)
                    prepareUIChange(AppCompatDelegate.MODE_NIGHT_YES)
                else
                    prepareUIChange(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        return root
    }

    private fun prepareUIChange(nightMode: Int) {
        JDPreferences.getInstance(requireContext()).setNightMode(nightMode)
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}
package jds.projects.ddcompanion.activities.settings.fragments

import android.content.Context
import android.widget.ArrayAdapter
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GMLanguage
import jds.projects.ddcompanion.my_classes.utils.JDPreferences

class SettingsViewModel(context: Context) {
    private var _dayNightMode = 0
    val dayNightMode: Int
        get() = _dayNightMode

    private var _languagesAdapter: ArrayAdapter<String>
    private var languages: MutableList<GMLanguage>
    val languagesAdapter: ArrayAdapter<String>
        get() = _languagesAdapter

    val selectedLanguage: GMLanguage
        get() = languages[actualLanguagePosition]

    private var actualLanguagePosition = 0
    val selectedLanguagePosition: Int
        get() = actualLanguagePosition

    init {
        val dbInstance = DBInterface.getInstance(context)
        val preferences = JDPreferences.getInstance(context)
        _dayNightMode = preferences.nightMode
        languages = dbInstance.getGMLanguages()
        _languagesAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, languages.map(GMLanguage::name))
        val preselectedLanguageID = preferences.language.id
        val preselectedLanguage = languages.find { lang -> lang.id == preselectedLanguageID }
        actualLanguagePosition = if (preselectedLanguage != null)
            languages.indexOf(preselectedLanguage)
        else
            0
    }

    fun newLanguageSelection(languagePosition: Int): Boolean {
        var result = false
        if (languagePosition != actualLanguagePosition) {
            actualLanguagePosition = languagePosition
            result = true
        }
        return result
    }
}
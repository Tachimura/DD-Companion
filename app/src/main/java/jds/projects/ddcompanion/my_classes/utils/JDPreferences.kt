package jds.projects.ddcompanion.my_classes.utils

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GMLanguage

class JDPreferences private constructor(private var context: Context) {
    companion object {
        const val PREFERENCE_LANGUAGE_ID = "PREFERENCE_LANGUAGE_ID"
        const val PREFERENCE_LANGUAGE_NAME = "PREFERENCE_LANGUAGE_NAME"
        const val PREFERENCE_DAYNIGHT = "PREFERENCE_DAYNIGHT"

        @Volatile
        private var INSTANCE: JDPreferences? = null

        @Synchronized
        fun getInstance(context: Context): JDPreferences =
            INSTANCE
                ?: JDPreferences(
                    context
                ).also { INSTANCE = it }
    }

    private val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )

    private var usedLanguage: GMLanguage
    val language: GMLanguage
        get() = usedLanguage

    private var _nightMode: Int = MODE_NIGHT_NO
    val nightMode: Int
        get() = _nightMode


    init {
        val usedLanguage = getLanguagePreference()
        this.usedLanguage =
            GMLanguage(
                usedLanguage.first,
                usedLanguage.second
            )
        _nightMode = sharedPreferences.getInt(PREFERENCE_DAYNIGHT, -1)
        //se non è mai stato impostato, prendo il valore di default che il sistema ha dato all'app
        if (_nightMode == -1) {
            val defaultDayNightMode =
                when (context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> MODE_NIGHT_NO
                    Configuration.UI_MODE_NIGHT_YES -> MODE_NIGHT_YES
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> MODE_NIGHT_NO
                    else -> MODE_NIGHT_NO
                }
            setNightMode(defaultDayNightMode)
        }
    }

    private fun getLanguagePreference(): Pair<Int, String> {
        return Pair(
            sharedPreferences.getInt(
                PREFERENCE_LANGUAGE_ID,
                context.resources.getInteger(R.integer.language_base_id1)
            ),
            sharedPreferences.getString(
                PREFERENCE_LANGUAGE_NAME,
                context.resources.getString(R.string.language_base_name1)
            )!!
        )
    }

    private fun editPreference(key: String, value: Int) {
        with(sharedPreferences.edit()) {
            putInt(key, value)
            commit()
        }
    }

    private fun editPreference(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            commit()
        }
    }

    @Synchronized
    fun setLanguage(language: GMLanguage) {
        usedLanguage = language
        editPreference(PREFERENCE_LANGUAGE_ID, language.id)
        editPreference(PREFERENCE_LANGUAGE_NAME, language.name)
        DBInterface.getInstance(context).setUsedLanguage(usedLanguage.id)
    }

    @Synchronized
    fun setNightMode(nightMode: Int) {
        _nightMode = nightMode
        editPreference(PREFERENCE_DAYNIGHT, nightMode)
    }
}
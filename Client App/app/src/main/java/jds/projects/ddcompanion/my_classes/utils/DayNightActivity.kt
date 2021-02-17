package jds.projects.ddcompanion.my_classes.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES


open class DayNightActivity : AppCompatActivity() {
    private companion object {
        private var nightMode = MODE_NIGHT_NO
        private var language = 1//inglese
        private var preferences: JDPreferences? = null
    }

    private var listener: OnLanguageChangedListener? = null
    interface OnLanguageChangedListener {
        fun onLanguageChanged()
    }
    fun setOnLanguageChangedListener(listener: OnLanguageChangedListener) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (preferences == null) {
            preferences = JDPreferences.getInstance(applicationContext)
            language = preferences!!.language.id
        }
        checkUIUpdate()
    }

    override fun onResume() {
        super.onResume()
        checkUIUpdate()
    }

    private fun checkUIUpdate() {
        if (preferences != null) {
            val savedLanguage = preferences!!.language.id
            if (language != savedLanguage) {
                language = savedLanguage
                listener?.onLanguageChanged()
            }
            val savedUIMode = preferences!!.nightMode
            if (nightMode != savedUIMode)
                changeUiMode(savedUIMode)
        }
    }

    private fun changeUiMode(uiMode: Int) {
        nightMode = uiMode
        if (uiMode == MODE_NIGHT_NO)
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
    }
}
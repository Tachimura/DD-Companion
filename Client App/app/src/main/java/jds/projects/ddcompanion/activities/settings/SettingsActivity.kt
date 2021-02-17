package jds.projects.ddcompanion.activities.settings

import android.os.Bundle
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.activities.settings.fragments.SettingsFragment
import jds.projects.ddcompanion.my_classes.utils.DayNightActivity

class SettingsActivity : DayNightActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    SettingsFragment()
                )
                .commitNow()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
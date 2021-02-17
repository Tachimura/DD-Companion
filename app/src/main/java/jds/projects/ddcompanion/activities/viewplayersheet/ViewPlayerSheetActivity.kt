package jds.projects.ddcompanion.activities.viewplayersheet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.activities.viewplayersheet.fragments.ViewPlayerSheetFragment
import jds.projects.ddcompanion.my_classes.utils.JDUtils

class ViewPlayerSheetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_player_sheet)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    ViewPlayerSheetFragment(
                        intent.extras!!.getInt(JDUtils.BUNDLES.PLAYER_SHEET_ID)
                    )
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
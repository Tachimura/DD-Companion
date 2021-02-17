package jds.projects.ddcompanion.activities.gamemanualdetails

import android.os.Bundle
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.activities.gamemanualdetails.fragments.GameManualDetailsFragment
import jds.projects.ddcompanion.my_classes.utils.DayNightActivity
import jds.projects.ddcompanion.my_classes.utils.JDUtils

class GameManualDetailsActivity : DayNightActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_manual_details)
        //mi prendo il manuale scelto
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    GameManualDetailsFragment(intent.extras!!.getInt(JDUtils.BUNDLES.GAME_MANUAL_ID))
                )
                .commitNow()
        }
        supportActionBar?.hide()
    }
}
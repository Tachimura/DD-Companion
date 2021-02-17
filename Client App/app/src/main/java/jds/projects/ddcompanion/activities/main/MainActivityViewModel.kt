package jds.projects.ddcompanion.activities.main

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.activities.main.fragments.gamesession.GameSessionFragment
import jds.projects.ddcompanion.activities.main.fragments.home.HomeFragment
import jds.projects.ddcompanion.activities.main.fragments.playersheets.PlayerSheetsFragment
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.dd_classes.*
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.COSTITUZIONE
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.FORZA
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GMLanguage
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GameManual
import jds.projects.ddcompanion.my_classes.utils.MLanguageText

class MainActivityViewModel(context: Context) {
    companion object {
        //const val POSITION_FRAGMENT_HOME: Int = 0
        const val POSITION_FRAGMENT_PLAYERSHEETS: Int = 1

        //const val POSITION_FRAGMENT_GAMESESSION: Int = 2
        const val NUM_FRAGMENTS: Int = 3
    }

    private var actualPosition: Int
    private lateinit var fragments: Array<Fragment>
    private var tabsTitles: Array<String>
    private var dbHelper: DBInterface

    init {
        //inizializzo i 3 fragments da mostrare a schermo
        actualPosition = 0
        tabsTitles = arrayOf(
            context.getString(R.string.title_fragment_home), context.getString(
                R.string.title_fragment_player_sheets
            ), context.getString(R.string.title_fragment_session)
        )
        dbHelper = DBInterface.getInstance(context)
    }

    fun createFragments() {
        val homeFragment = HomeFragment()
        val playerSheetFragment = PlayerSheetsFragment()
        val gameSessionFragment = GameSessionFragment().apply {
            setOnGameSessionListener(object : GameSessionFragment.GameSessionListener {
                override fun onPlayerSessionFinished(flagLevelUp: Int, playerSheetID: Int) {
                    if (playerSheetID > 0 && flagLevelUp > 0) {
                        (fragments[POSITION_FRAGMENT_PLAYERSHEETS] as PlayerSheetsFragment).notifyPlayerSheetLevelUpActivated(
                            playerSheetID
                        )
                    }
                }
            })
        }
        homeFragment.setOnHomeFragmentListener(object : HomeFragment.HomeFragmentListener {
            override fun onNewGameManualDownloaded() {
                gameSessionFragment.newGameManualReceived()
            }
        })
        fragments = arrayOf(homeFragment, playerSheetFragment, gameSessionFragment)
    }

    fun loadFragments(fragments: Array<Fragment>) {
        this.fragments = fragments
    }

    fun closeDBConnection() {
        dbHelper.closeDBConnection()
    }

    fun getFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun getFragments(): Array<Fragment> {
        return fragments
    }

    fun getActualPosition(): Int {
        return actualPosition
    }

    fun getTabTitle(position: Int): String {
        return tabsTitles[position]
    }

    fun updateActualPosition(position: Int) {
        if (position in 0 until NUM_FRAGMENTS)
            actualPosition = position
    }

    fun createStartingRecords(context: Context) {
        if (dbHelper.needToInitDB()) {
            val inglese =
                GMLanguage(
                    context.resources.getInteger(R.integer.language_base_id1),
                    context.resources.getString(R.string.language_base_name1)
                )
            val italiano =
                GMLanguage(
                    context.resources.getInteger(R.integer.language_base_id2),
                    context.resources.getString(R.string.language_base_name2)
                )
            var ret = dbHelper.insertNewGMLanguage(inglese)
            Log.println(Log.ASSERT, "DBCreationLanRecords", "ret: $ret")
            ret = dbHelper.insertNewGMLanguage(italiano)
            Log.println(Log.ASSERT, "DBCreationLanRecords", "ret: $ret")

            //notes del manuale di base
            val manualNotes =
                MLanguageText()
            manualNotes.addMLanguageText(
                inglese.id,
                context.getString(R.string.manual_base_note_l1)
            )
            manualNotes.addMLanguageText(
                italiano.id,
                context.getString(R.string.manual_base_note_l2)
            )
            //razza di base
            val raceNames = MLanguageText()
            raceNames.addMLanguageText(
                inglese.id,
                context.getString(R.string.manual_base_race_name_l1)
            )
            raceNames.addMLanguageText(
                italiano.id,
                context.getString(R.string.manual_base_race_name_l2)
            )
            //classe di base
            val classNames =
                MLanguageText()
            classNames.addMLanguageText(
                inglese.id,
                context.getString(R.string.manual_base_class_name_l1)
            )
            classNames.addMLanguageText(
                italiano.id,
                context.getString(R.string.manual_base_class_name_l2)
            )
            //talento di base
            val talentNames =
                MLanguageText()
            talentNames.addMLanguageText(
                inglese.id,
                context.getString(R.string.manual_base_talent_name_l1)
            )
            talentNames.addMLanguageText(
                italiano.id,
                context.getString(R.string.manual_base_talent_name_l2)
            )
            val talentDescriptions =
                MLanguageText()
            talentDescriptions.addMLanguageText(
                inglese.id,
                context.getString(R.string.manual_base_talent_description_l1)
            )
            talentDescriptions.addMLanguageText(
                italiano.id,
                context.getString(R.string.manual_base_talent_description_l2)
            )
            val manual =
                GameManual(
                    1,
                    1.0F,
                    context.resources.getString(R.string.manual_base_release_data),
                    10,
                    PlayerStats(8, 8, 8, 8, 8, 8),
                    manualNotes,
                    arrayOf(
                        DDRace(
                            1,
                            true,
                            1,
                            raceNames,
                            PlayerStats(0, 0, 1, 0, 0, 0)
                        )
                    ),
                    arrayOf(
                        DDClass(
                            1,
                            true,
                            1,
                            classNames,
                            12,
                            8,
                            FORZA,
                            FORZA,
                            COSTITUZIONE,
                            4,
                            DDAlignments(1, 1, 1, 1, 1, 1, 1, 1, 1)
                        )
                    ),
                    arrayOf(
                        DDTalent(
                            1,
                            true,
                            1,
                            1,
                            talentNames,
                            talentDescriptions,
                            PlayerStats(0, 0, 0, 0, 0, 0),
                            0, 0, 0,
                            0, 2
                        )
                    )
                )
            ret = dbHelper.insertGameManual(manual)
            Log.println(Log.ASSERT, "DBCreationTestRecords", "ret: $ret")
        }
    }
}

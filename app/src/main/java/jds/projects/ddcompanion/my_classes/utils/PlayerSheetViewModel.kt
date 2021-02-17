package jds.projects.ddcompanion.my_classes.utils

import android.content.Context
import androidx.fragment.app.Fragment
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.dd_classes.*

abstract class PlayerSheetViewModel(protected var context: Context) {
    protected var dbHelper: DBInterface = DBInterface.getInstance(context)

    //NUOVE STATS
    protected var newPlayerClass: DDClass = DDClass(-1, false)
    protected lateinit var newPlayerStats: PlayerStats
    protected var newPlayerAbilities: MutableList<DDAbility> =
        DDAbilityManager.getInstance(context).getAbilities()
    protected var newPlayerTalents: MutableList<DDTalent> = mutableListOf()
    protected var oldIntModifier: Int = -100

    //USATE X IL LEVEL-UP
    protected var newLevel: Int = 1
    protected var statsRemainingPoints: Int = 0
    protected var abilityRemainingPoints: Int = 0
    protected var talentsRemainingPoints: Int = 0

    //FRAGMENTS + GESTIONE VIEWPAGER
    protected lateinit var tabsTitles: MutableList<String>
    protected lateinit var fragmentList: MutableList<Fragment>
    protected var actualPosition: Int = 0

    //FLAGS PER SAPERE SE AGGIORNARE O MENO LA VIEW
    protected var flagModifyBase: Boolean = true
    protected var flagModifyRace: Boolean = true
    protected var flagModifyClass: Boolean = true
    protected var flagModifyStats: Boolean = true
    protected var flagModifyAbility: Boolean = true
    protected var flagModifyTalents: Boolean = true

    //VARIABILI X I PUNTEGGI DI LEVEL-UP
    //TODO! IN CASO DI MODIFICHE DEI PUNTI STATISTICA E TALENTI MODIFICRE QUA
    protected val newStatsPoints: Int = 1
    protected val newTalentsPoints: Int = 1

    //per gestire i talenti
    protected var availableTalents: Int = 0

    fun getPosition(): Int {
        return actualPosition
    }

    fun getFragments(): MutableList<Fragment> {
        return fragmentList
    }

    fun getCount(): Int {
        return fragmentList.count()
    }

    fun getTabTitle(position: Int): String {
        return tabsTitles[position]
    }

    protected abstract fun savePlayerSheet(): Int
    abstract fun updateView(newPosition: Int): Boolean
}
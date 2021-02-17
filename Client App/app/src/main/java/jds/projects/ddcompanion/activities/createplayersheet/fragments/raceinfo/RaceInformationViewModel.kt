package jds.projects.ddcompanion.activities.createplayersheet.fragments.raceinfo

import android.content.Context
import jds.projects.ddcompanion.my_classes.adapters.ExpandableCardAdapter
import jds.projects.ddcompanion.my_classes.adapters.RaceAdapter
import jds.projects.ddcompanion.my_classes.dd_classes.DDRace
import jds.projects.ddcompanion.my_classes.utils.JDPreferences
import jds.projects.ddcompanion.my_classes.utils.JDViewModel

class RaceInformationViewModel(context: Context) : JDViewModel(context) {
    private lateinit var _races: MutableList<DDRace>

    val races: MutableList<DDRace>
        get() = _races

    fun needToUpdate(manualID: Int): Boolean {
        return lastManualID != manualID
    }

    fun createAdapter(): ExpandableCardAdapter {
        _races = mutableListOf()
        arrayAdapter = RaceAdapter(context, _races, JDPreferences.getInstance(context).language.id)
        return arrayAdapter
    }

    fun updateAdapter(races: MutableList<DDRace>, manualID: Int) {
        lastManualID = manualID
        _races.clear()
        _races.addAll(races)
        arrayAdapter.notifyDataSetChanged()
    }
}
package jds.projects.ddcompanion.activities.createplayersheet.fragments.talentsinfo

import android.content.Context
import jds.projects.ddcompanion.my_classes.adapters.ExpandableCardAdapter
import jds.projects.ddcompanion.my_classes.adapters.TalentsAdapter
import jds.projects.ddcompanion.my_classes.dd_classes.DDTalent
import jds.projects.ddcompanion.my_classes.utils.JDPreferences
import jds.projects.ddcompanion.my_classes.utils.JDViewModel

class TalentsInformationViewModel(context: Context) : JDViewModel(context) {
    private lateinit var _talents: MutableList<DDTalent>
    private var _useablePoints: Int = 0

    val useablePoints: Int
        get() = _useablePoints

    fun needToUpdate(manualID: Int): Boolean {
        return lastManualID != manualID
    }

    fun createAdapter(): ExpandableCardAdapter {
        _talents = mutableListOf()
        arrayAdapter =
            TalentsAdapter(context, _talents, JDPreferences.getInstance(context).language.id)
        return arrayAdapter
    }

    fun updateAdapter(talents: MutableList<DDTalent>, manualID: Int, useablePoints: Int) {
        lastManualID = manualID
        this._useablePoints = useablePoints
        _talents.clear()
        _talents.addAll(talents)
        arrayAdapter.notifyDataSetChanged()
    }

    fun selectedTalent(position: Int): DDTalent {
        _useablePoints -= 1
        return _talents[position]
    }

    fun deselectTalent(position: Int): DDTalent {
        _useablePoints += 1
        return _talents[position]
    }

    fun resetSelection() {
        arrayAdapter.resetSelection()
    }
}
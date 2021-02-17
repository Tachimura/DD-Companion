package jds.projects.ddcompanion.activities.createplayersheet.fragments.classinfo

import android.content.Context
import jds.projects.ddcompanion.my_classes.adapters.ClassesAdapter
import jds.projects.ddcompanion.my_classes.adapters.ExpandableCardAdapter
import jds.projects.ddcompanion.my_classes.dd_classes.DDClass
import jds.projects.ddcompanion.my_classes.utils.JDPreferences
import jds.projects.ddcompanion.my_classes.utils.JDViewModel

class ClassInformationViewModel(context: Context) : JDViewModel(context) {
    private lateinit var _classes: MutableList<DDClass>
    private var _lastAlignment = -1

    val classes: List<DDClass>
        get() = _classes

    fun needToUpdate(manualID: Int, raceID: Int): Boolean {
        return lastManualID != manualID || _lastAlignment != raceID
    }

    fun updateAdapter(classes: Array<DDClass>, manual: Int, alignment: Int) {
        lastManualID = manual
        _lastAlignment = alignment
        //Mi prendo tutte le classi del manuale ma tengo solo quelle che hanno l'allineamento richiesto ad 1 e che dunque ne permette l'uso
        _classes.clear()
        _classes.addAll(classes.filter { el -> el.allowedAlignments.alignments[alignment] == 1 })
        arrayAdapter.notifyDataSetChanged()
    }

    fun createAdapter(): ExpandableCardAdapter {
        _classes = mutableListOf()
        arrayAdapter =
            ClassesAdapter(context, _classes, JDPreferences.getInstance(context).language.id)
        return arrayAdapter
    }

    fun resetSelection() {
        arrayAdapter.resetSelection()
    }
}
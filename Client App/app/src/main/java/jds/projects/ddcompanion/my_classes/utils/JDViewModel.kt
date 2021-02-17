package jds.projects.ddcompanion.my_classes.utils

import android.content.Context
import jds.projects.ddcompanion.my_classes.adapters.ExpandableCardAdapter

open class JDViewModel(protected var context: Context) {
    protected var lastManualID = -1
    protected lateinit var arrayAdapter: ExpandableCardAdapter

    val manualID: Int
        get() = lastManualID
}
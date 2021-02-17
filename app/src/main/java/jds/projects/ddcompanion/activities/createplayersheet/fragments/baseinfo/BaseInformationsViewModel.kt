package jds.projects.ddcompanion.activities.createplayersheet.fragments.baseinfo

import android.content.Context
import android.widget.ArrayAdapter
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.adapters.AlignmentsAdapter
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GameManual

class BaseInformationsViewModel(private var context: Context) {

    private var _gameManuals = arrayOf<GameManual>()
    private val _alignmentsAdapter =
        AlignmentsAdapter(context, context.resources.getStringArray(R.array.alignments))

    val gameManualsAdapter: ArrayAdapter<String>
        get() = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            _gameManuals.map { v -> context.resources.getString(R.string.game_manual_title) + " " + v.manualVersion.toString() })

    val gameManuals: Array<GameManual>
        get() = _gameManuals

    val alignmentsAdapter: AlignmentsAdapter
        get() = _alignmentsAdapter

    fun loadGameManuals() {
        val deltaGameManuals = mutableListOf<GameManual>()
        for (manual in DBInterface.getInstance(context).getGameManualsBaseInfo()) {
            deltaGameManuals.add(
                DBInterface.getInstance(context).getGameManualFullInfo(manual.manualID)
            )
        }
        _gameManuals = deltaGameManuals.toTypedArray()
    }
}
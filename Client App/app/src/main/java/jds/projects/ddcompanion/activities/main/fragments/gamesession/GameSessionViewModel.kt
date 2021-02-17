package jds.projects.ddcompanion.activities.main.fragments.gamesession

import android.content.Context
import android.widget.ArrayAdapter
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GameManual

class GameSessionViewModel(private var context: Context) {
    private var _selectedGameManualID: Int = 1
    val selectedGameManualID: Int
        get() = _selectedGameManualID

    private var _gameManuals = mutableListOf<GameManual>()
    private var _gameManualsAdapter: ArrayAdapter<String> =
        ArrayAdapter(context, android.R.layout.simple_list_item_1)

    val gameManualsAdapter: ArrayAdapter<String>
        get() = _gameManualsAdapter

    init {
        loadNewGameManuals()
    }

    fun loadNewGameManuals() {
        synchronized(_gameManuals) {
            _gameManuals = DBInterface.getInstance(context).getGameManualsBaseInfo().toMutableList()
            _gameManualsAdapter.clear()
            _gameManualsAdapter.addAll(_gameManuals.map { v -> context.resources.getString(R.string.game_manual_title) + " " + v.manualVersion.toString() })
            _gameManualsAdapter.notifyDataSetChanged()
        }
    }

    fun setSelectedGameManual(position: Int) {
        synchronized(_gameManuals) {
            _selectedGameManualID = _gameManuals[position].manualID
        }
    }
}
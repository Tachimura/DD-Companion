package jds.projects.ddcompanion.activities.main.fragments.playersheets

import android.content.Context
import jds.projects.ddcompanion.my_classes.adapters.PlayerSheetsAdapter
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetExporter
import jds.projects.ddcompanion.my_classes.utils.JDPreferences

class PlayerSheetsViewModel(private var context: Context) {

    private var fabState = false
    private val playerSheets = mutableListOf<PlayerSheet>()
    private var adapterReady: Boolean = false
    private val dbHelper = DBInterface.getInstance(context)
    private lateinit var _adapter: PlayerSheetsAdapter

    private fun addPlayerSheet(sheet: PlayerSheet) {
        playerSheets.add(sheet)
        notifyAdapter()
    }

    private fun addPlayerSheets(sheets: Collection<PlayerSheet>) {
        playerSheets.addAll(sheets)
        notifyAdapter()
    }

    private fun notifyAdapter() {
        if (adapterReady)
            _adapter.notifyDataSetChanged()
    }

    fun getPlayerSheets(): MutableList<PlayerSheet> {
        return playerSheets
    }

    fun setFabState(state: Boolean) {
        fabState = state
    }

    fun isFabOpen(): Boolean {
        return fabState
    }

    fun createAdapter(): PlayerSheetsAdapter {
        _adapter = PlayerSheetsAdapter(
            playerSheets,
            JDPreferences.getInstance(context).language.id
        )
        adapterReady = true
        return _adapter
    }

    fun loadPlayerSheets() {
        addPlayerSheets(dbHelper.getPlayerSheets())
    }

    fun loadPlayerSheetWithID(psID: Int) {
        addPlayerSheet(dbHelper.getPlayerSheetByID(psID))
    }

    fun exportPlayerSheet(playerSheet: PlayerSheet): Boolean {
        return PlayerSheetExporter(context).exportPlayerSheet(playerSheet)
    }

    fun deletePlayerSheet(playerSheet: PlayerSheet): Boolean {
        if (adapterReady) {
            if (dbHelper.deletePlayerSheet(playerSheet.playerSheetID)) {
                playerSheets.remove(playerSheet)
                notifyAdapter()
                return true
            }
        }
        return false
    }

    fun updatePlayerSheetWithID(updatedPlayerSheetID: Int) {
        if (adapterReady) {
            playerSheets.find { ps -> ps.playerSheetID == updatedPlayerSheetID }!!
                .updatePlayerSheet(dbHelper.getPlayerSheetByID(updatedPlayerSheetID))
            notifyAdapter()
        }
    }

    fun languageChanged() {
        val newPlayerSheets = dbHelper.getPlayerSheets()
        playerSheets.clear()
        addPlayerSheets(newPlayerSheets)
        _adapter.updateLanguage(JDPreferences.getInstance(context).language.id)
        notifyAdapter()
    }
}
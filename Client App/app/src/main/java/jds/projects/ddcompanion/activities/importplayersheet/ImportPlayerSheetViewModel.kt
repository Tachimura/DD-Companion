package jds.projects.ddcompanion.activities.importplayersheet

import android.content.Context
import android.net.Uri
import jds.projects.ddcompanion.activities.importplayersheet.fragments.ImportPlayerSheetFragment
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetFileManager.CODE.SUCCESS
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetImporter

class ImportPlayerSheetViewModel(private var context: Context) {
    private var _importFragment = ImportPlayerSheetFragment()
    val importFragment: ImportPlayerSheetFragment
        get() = _importFragment

    private lateinit var _playerSheet: PlayerSheet
    val importedPlayerSheet: PlayerSheet
        get() = if (canImport) _playerSheet else PlayerSheet()

    private var canImport: Boolean = false

    private var _manualIDError = 0
    val manualIDNotFound: Int
        get() = _manualIDError

    fun loadPlayerSheetFromUri(fileName: Uri): Int {
        var result: Int
        //uso il mio importer x gestire l'import della player sheet json
        with(PlayerSheetImporter(context)) {
            result = readPlayerSheetFromJSONFile(fileName)
            if (result == SUCCESS)
                _playerSheet = importedPlayerSheet
            _manualIDError = manualIDError
        }
        canImport = result == SUCCESS
        return result
    }

    fun importPlayerSheetInDB(): Boolean {
        var result = false
        if (canImport) {
            _playerSheet.lateLoad(
                DBInterface.getInstance(context).insertNewPlayerSheet(importedPlayerSheet)
                    .toInt()
            )
            if (importedPlayerSheet.playerSheetID > 0)
                result = true
        }
        return result
    }

    fun loadPlayerSheet() {
        importFragment.loadPlayerSheet(importedPlayerSheet)
    }
}
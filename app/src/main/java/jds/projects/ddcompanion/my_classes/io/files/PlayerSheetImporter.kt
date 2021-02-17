package jds.projects.ddcompanion.my_classes.io.files

import android.content.Context
import android.net.Uri
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet
import org.json.JSONObject

class PlayerSheetImporter(context: Context) : PlayerSheetFileManager(context) {
    val importedPlayerSheet: PlayerSheet
        get() = ddJSONManager.playerSheetResult

    val manualIDError: Int
        get() = ddJSONManager.manualIDError

    fun readPlayerSheetFromJSONFile(fileSrc: Uri): Int {
        val result: Int
        result = try {
            val sheetText = context.contentResolver.openInputStream(fileSrc)!!.bufferedReader()
                .use { it.readText() }
            ddJSONManager.makePlayerSheet(JSONObject(sheetText))
        } catch (e: Exception) {
            CODE.ERROR_FILE_NOT_SUPPORTED
        }
        return result
    }
}
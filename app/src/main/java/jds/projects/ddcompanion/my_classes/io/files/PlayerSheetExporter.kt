package jds.projects.ddcompanion.my_classes.io.files

import android.content.Context
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet
import java.io.File
import java.io.FileOutputStream

class PlayerSheetExporter(context: Context) : PlayerSheetFileManager(context) {

    fun exportPlayerSheet(playerSheet: PlayerSheet): Boolean {
        var result = false
        if (ddJSONManager.makeJSON(playerSheet)) {
            //creo directory
            val appDirectory = context.getExternalFilesDir(null)
            appDirectory?.mkdirs()
            val file = File(appDirectory, "${playerSheet.playerName}.txt")
            FileOutputStream(file).use {
                it.write(
                    ddJSONManager.jsonResult.toString().toByteArray()
                )
            }
            result = true
        }
        return result
    }
}
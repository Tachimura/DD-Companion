package jds.projects.ddcompanion.my_classes.io.files

import android.content.Context

abstract class PlayerSheetFileManager(protected var context: Context) {
    object CODE {
        const val SUCCESS = 1
        const val ERROR_FILE_NOT_SUPPORTED = -1
        const val ERROR_MANUAL_NOT_PRESENT = -2
    }

    protected var ddJSONManager = PlayerSheetJSON(context)
}
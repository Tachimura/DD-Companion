package jds.projects.ddcompanion.my_classes.utils

abstract class JDUtils {
    object BUNDLES {
        const val PLAYER_SHEET_ID = "PlayerSheetID"
        const val GAME_MANUAL_ID = "GameManualID"
        const val LEVEL_UP = "LevelUp"
    }
    object REQUESTS {
        const val CREATE_PLAYER_SHEET = 1
        const val PICK_FILE_RESULT_CODE = 2
        const val UPDATE_PLAYER_SHEET = 3
        const val LEVEL_UP = 4
    }
    object WiFi {
        const val PORT = 6789
    }
}
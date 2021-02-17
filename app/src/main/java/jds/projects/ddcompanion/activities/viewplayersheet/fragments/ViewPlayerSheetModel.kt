package jds.projects.ddcompanion.activities.viewplayersheet.fragments

import android.content.Context
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet

class ViewPlayerSheetModel(context: Context, playerSheetID: Int) {

    private var _playerSheet: PlayerSheet =
        DBInterface.getInstance(context).getPlayerSheetByID(playerSheetID)

    val playerSheet: PlayerSheet
        get() = _playerSheet

}
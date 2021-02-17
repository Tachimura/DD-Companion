package jds.projects.ddcompanion.my_classes.dialogs

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet

class DialogPlayerSheetManager(context: Context, private val playerSheet: PlayerSheet) :
    AlertDialog(context) {
    private var listener: OnPlayerSheetListener? = null

    interface OnPlayerSheetListener {
        fun onPlayerSheetActionShow(playerSheet: PlayerSheet)
        fun onPlayerSheetActionLevelUp(playerSheet: PlayerSheet)
        fun onPlayerSheetActionExport(playerSheet: PlayerSheet)
        fun onPlayerSheetActionDelete(playerSheet: PlayerSheet)
    }

    fun setOnPlayerSheetActions(listener: OnPlayerSheetListener) {
        this.listener = listener
    }

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_player_sheet_manager)
        val txvName =
            findViewById<TextView>(R.id.txv_dialog_player_sheet_managerdialog_player_sheet_manager__sheetname)
        val btnShow =
            findViewById<Button>(R.id.btn_dialog_player_sheet_managerdialog_player_sheet_manager__showsheet)
        val btnLevelUp =
            findViewById<Button>(R.id.btn_dialog_player_sheet_managerdialog_player_sheet_manager__levelup)
        val btnExport =
            findViewById<Button>(R.id.btn_dialog_player_sheet_managerdialog_player_sheet_manager__exportsheet)
        val btnDelete =
            findViewById<Button>(R.id.btn_dialog_player_sheet_managerdialog_player_sheet_manager__deletesheet)
        //imposto il nome del player
        txvName.text = playerSheet.playerName
        //callback dell'action show
        btnShow.setOnClickListener {
            cancel()
            listener?.onPlayerSheetActionShow(playerSheet)
        }
        //se l'utente può salire di livello
        if (playerSheet.canLevelUp) {
            //abilito il pulsante e ci collego il listener
            btnLevelUp.isEnabled = true
            //callback dell'action levelup
            btnLevelUp.setOnClickListener {
                cancel()
                listener?.onPlayerSheetActionLevelUp(playerSheet)
            }
        }
        //callback dell'action export
        btnExport.setOnClickListener {
            cancel()
            listener?.onPlayerSheetActionExport(playerSheet)
        }
        //callback dell'action delete
        btnDelete.setOnClickListener {
            cancel()
            listener?.onPlayerSheetActionDelete(playerSheet)
        }
    }
}
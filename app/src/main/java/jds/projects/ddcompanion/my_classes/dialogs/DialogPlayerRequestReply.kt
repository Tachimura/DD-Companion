package jds.projects.ddcompanion.my_classes.dialogs

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessage
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessageManager
import jds.projects.ddcompanion.my_classes.utils.DayNightActivity
import kotlin.random.Random

class DialogPlayerRequestReply(
    context: DayNightActivity,
    private var request: JDMessage
) : AlertDialog.Builder(context) {
    private val inflater = context.layoutInflater
    private val view = inflater.inflate(R.layout.dialog_player_request_reply, null)
    private var listener: RequestListener? = null

    interface RequestListener {
        fun onResultAvailable(dice: Int, result: Int, punteggioBase: Int, punteggioBonus: Int)
    }

    fun setOnRequestListener(listener: RequestListener) {
        this.listener = listener
    }

    init {
        setView(view)
    }

    override fun create(): AlertDialog {
        val dialog = super.create()
        dialog.setContentView(view)
        dialog.setCancelable(false)
        val txvTitle = dialog.findViewById<TextView>(R.id.txv_dialog_player_request_reply__title)!!
        val txvUser = dialog.findViewById<TextView>(R.id.txv_dialog_player_request_reply__user)!!
        val txvClasseDifficolta =
            dialog.findViewById<TextView>(R.id.txv_dialog_player_request_reply__difficulty)!!
        val txvPunteggioBase =
            dialog.findViewById<TextView>(R.id.txv_dialog_player_request_reply__base)!!
        val edtPunteggioBonus =
            dialog.findViewById<EditText>(R.id.edt_dialog_player_request_reply__bonus)!!
        val btnSend = dialog.findViewById<Button>(R.id.btn_dialog_player_request_reply__send)!!
        when (request.requestID) {
            JDMessageManager.TYPE.REQUEST_THROW_TC -> {
                txvTitle.text = context.getString(R.string.requested_throw_tc)
            }
            JDMessageManager.TYPE.REQUEST_THROW_ABILITY -> {
                txvTitle.text = context.getString(R.string.requested_throw_ability, request.message)
            }
            JDMessageManager.TYPE.REQUEST_THROW_CA -> {
                txvTitle.text = context.getString(R.string.requested_throw_ca)
            }
            JDMessageManager.TYPE.REQUEST_THROW_SAVING -> {
                when (request.savingType) {
                    JDMessageManager.SAVES.FORTITUDE -> {
                        txvTitle.text = context.getString(R.string.requested_throw_fortitude)
                    }
                    JDMessageManager.SAVES.REFLEXES -> {
                        txvTitle.text = context.getString(R.string.requested_throw_reflexes)
                    }
                    else -> {
                        txvTitle.text = context.getString(R.string.requested_throw_will)
                    }
                }
            }
        }
        txvUser.text = context.getString(R.string.text_requested_by_master)
        txvClasseDifficolta.text = request.difficulty.toString()
        txvPunteggioBase.text = request.punteggioBase.toString()

        edtPunteggioBonus.requestFocus()
        btnSend.setOnClickListener {
            if (edtPunteggioBonus.text.trim().isNotEmpty()) {
                val dice = Random.nextInt(20) + 1
                val bonus = edtPunteggioBonus.text.trim().toString().toInt()
                val punteggio = request.punteggioBase + bonus
                val result = (punteggio + dice) - request.difficulty
                dialog.dismiss()
                listener?.onResultAvailable(dice, result, request.punteggioBase, bonus)
            }
        }
        return dialog
    }
}
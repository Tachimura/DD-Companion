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

class DialogMasterRequestReply(
    context: DayNightActivity,
    private var playerName: String,
    private var request: JDMessage
) : AlertDialog.Builder(context) {
    private val inflater = context.layoutInflater
    private val view = inflater.inflate(R.layout.dialog_master_request_reply, null)
    private var listener: RequestListener? = null

    interface RequestListener {
        fun onResultAvailable(dice: Int, result: Int, difficolta: Int)
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
        val txvTitle = dialog.findViewById<TextView>(R.id.txv_dialog_master_request_reply__title)!!
        val txvUser = dialog.findViewById<TextView>(R.id.txv_dialog_master_request_reply__user)!!
        val txvPunteggioBase =
            dialog.findViewById<TextView>(R.id.txv_dialog_master_request_reply__base)!!
        val txvPunteggioBonus =
            dialog.findViewById<TextView>(R.id.txv_dialog_master_request_reply__bonus)!!
        val edtClasseDifficolta =
            dialog.findViewById<EditText>(R.id.edt_dialog_master_request_reply__difficolta)!!
        val btnSend = dialog.findViewById<Button>(R.id.btn_dialog_master_request_reply__send)!!
        when (request.requestID) {
            JDMessageManager.TYPE.REQUEST_THROW_TC -> {
                txvTitle.text = context.getString(R.string.requested_throw_tc)
            }
            JDMessageManager.TYPE.REQUEST_THROW_ABILITY -> {
                txvTitle.text = context.getString(R.string.requested_throw_ability, request.message)
            }
        }
        txvUser.text = context.getString(R.string.text_requested_by, playerName)
        txvPunteggioBase.text = request.punteggioBase.toString()
        txvPunteggioBonus.text = request.punteggioBonus.toString()
        edtClasseDifficolta.setText(request.difficulty.toString())

        edtClasseDifficolta.requestFocus()
        btnSend.setOnClickListener {
            if (edtClasseDifficolta.text.trim().isNotEmpty()) {
                val dice = Random.nextInt(20) + 1
                val classeDifficolta = edtClasseDifficolta.text.trim().toString().toInt()
                val result =
                    (request.punteggioBase + request.punteggioBonus + dice) - classeDifficolta
                dialog.dismiss()
                listener?.onResultAvailable(dice, result, classeDifficolta)
            }
        }
        return dialog
    }
}
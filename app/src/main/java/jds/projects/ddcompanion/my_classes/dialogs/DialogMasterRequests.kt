package jds.projects.ddcompanion.my_classes.dialogs

import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.cards.ExpandableCardView
import jds.projects.ddcompanion.my_classes.dd_classes.DDAbility
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessage
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessageManager

class DialogMasterRequests(fragment: Fragment, private var abilities: MutableList<DDAbility>) :
    AlertDialog.Builder(fragment.requireContext()) {
    private val inflater = fragment.layoutInflater
    private val view = inflater.inflate(R.layout.dialog_master_requests, null)
    private var listener: RequestListener? = null
    private lateinit var address: String
    private lateinit var dialog: AlertDialog

    private lateinit var txvPlayerName: TextView

    interface RequestListener {
        fun onRequestSelected(request: JDMessage, address: String)
    }

    fun setOnRequestListener(listener: RequestListener) {
        this.listener = listener
    }

    init {
        setView(view)
    }

    override fun create(): AlertDialog {
        dialog = super.create()
        dialog.setContentView(view)
        txvPlayerName = dialog.findViewById(R.id.txv_dialog_master_requests__player)!!
        setupCardTC(dialog)
        setupCardCA(dialog)
        setupCardSaving(dialog)
        setupCardAbility(dialog)
        setupCardlevelUp(dialog)
        return dialog
    }

    private fun setupCardAbility(dialog: AlertDialog) {
        val card = dialog.findViewById<CardView>(R.id.dialog_master_requests__ability)!!
        val eCard = ExpandableCardView(card, context)
        eCard.canExpand = true
        val innerLayout = View.inflate(context, R.layout.view_inner_card_master_ability, null)
        val spnAbility =
            innerLayout.findViewById<Spinner>(R.id.spn_view_inner_card_master_ability__abilities)
        spnAbility.adapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            abilities.map { ability -> ability.name })
        val edtDifficulty =
            innerLayout.findViewById<EditText>(R.id.edt_inner_card_master__difficulty)
        val btnSend =
            innerLayout.findViewById<Button>(R.id.btn_view_inner_card_master_general__request)
        btnSend.setOnClickListener {
            if (spnAbility.selectedItemPosition != -1) {
                val selectedAbilityID = abilities[spnAbility.selectedItemPosition].id
                val difficulty = edtDifficulty.text.toString().trim()
                if (difficulty.isNotEmpty()) {
                    val message = JDMessageManager.requestToPlayerThrowAbility(
                        selectedAbilityID,
                        difficulty.toInt()
                    )
                    dialog.dismiss()
                    listener?.onRequestSelected(message, address)
                }
            }
        }
        eCard.updateCard(context.getString(R.string.card_title_ability), innerLayout)
    }

    private fun setupCardSaving(dialog: AlertDialog) {
        val card = dialog.findViewById<CardView>(R.id.dialog_master_requests__saving)!!
        val eCard = ExpandableCardView(card, context)
        eCard.canExpand = true
        val innerLayout = View.inflate(context, R.layout.view_inner_card_master_saving, null)
        val radioGroup =
            innerLayout.findViewById<RadioGroup>(R.id.rdg_inner_card_master_saving__type)
        val edtDifficulty =
            innerLayout.findViewById<EditText>(R.id.edt_inner_card_master__difficulty)
        val btnSend =
            innerLayout.findViewById<Button>(R.id.btn_view_inner_card_master_general__request)
        btnSend.setOnClickListener {
            val difficulty = edtDifficulty.text.toString().trim()
            if (difficulty.isNotEmpty()) {
                val checkedRadio = radioGroup.checkedRadioButtonId
                if (checkedRadio != -1) {
                    var savingType = JDMessageManager.SAVES.FORTITUDE
                    when (checkedRadio) {
                        R.id.rdb_inner_card_master_saving__tempra -> {
                            savingType = JDMessageManager.SAVES.FORTITUDE
                        }
                        R.id.rdb_inner_card_master_saving__riflessi -> {
                            savingType = JDMessageManager.SAVES.REFLEXES
                        }
                        R.id.rdb_inner_card_master_saving__volonta -> {
                            savingType = JDMessageManager.SAVES.WILL
                        }
                    }
                    val message =
                        JDMessageManager.requestToPlayerThrowSaving(savingType, difficulty.toInt())
                    dialog.dismiss()
                    listener?.onRequestSelected(message, address)
                }
            }
        }
        eCard.updateCard(context.getString(R.string.card_title_savings), innerLayout)
    }

    private fun setupCardCA(dialog: AlertDialog) {
        val card = dialog.findViewById<CardView>(R.id.dialog_master_requests__ca)!!
        val eCard = ExpandableCardView(card, context)
        eCard.canExpand = true
        val innerLayout = View.inflate(context, R.layout.view_inner_card_master_general, null)
        val edtDifficulty =
            innerLayout.findViewById<EditText>(R.id.edt_inner_card_master__difficulty)
        val btnSend =
            innerLayout.findViewById<Button>(R.id.btn_view_inner_card_master_general__request)
        btnSend.setOnClickListener {
            val difficulty = edtDifficulty.text.toString().trim()
            if (difficulty.isNotEmpty()) {
                val message = JDMessageManager.requestToPlayerThrowCA(difficulty.toInt())
                dialog.dismiss()
                listener?.onRequestSelected(message, address)
            }
        }
        eCard.updateCard(context.getString(R.string.card_title_ca), innerLayout)
    }

    private fun setupCardTC(dialog: AlertDialog) {
        val card = dialog.findViewById<CardView>(R.id.dialog_master_requests__tc)!!
        val eCard = ExpandableCardView(card, context)
        eCard.canExpand = true
        val innerLayout = View.inflate(context, R.layout.view_inner_card_master_general, null)
        val edtDifficulty =
            innerLayout.findViewById<EditText>(R.id.edt_inner_card_master__difficulty)
        val btnSend =
            innerLayout.findViewById<Button>(R.id.btn_view_inner_card_master_general__request)
        btnSend.setOnClickListener {
            val difficulty = edtDifficulty.text.toString().trim()
            if (difficulty.isNotEmpty()) {
                val message = JDMessageManager.requestToPlayerThrowTC(difficulty.toInt())
                dialog.dismiss()
                listener?.onRequestSelected(message, address)
            }
        }
        eCard.updateCard(context.getString(R.string.card_title_tc), innerLayout)
    }

    private fun setupCardlevelUp(dialog: AlertDialog) {
        val card = dialog.findViewById<CardView>(R.id.dialog_master_requests__levelup)!!
        val eCard = ExpandableCardView(card, context)
        eCard.canExpand = true
        val innerLayout = View.inflate(context, R.layout.view_inner_card_master_levelup, null)
        val btnSend =
            innerLayout.findViewById<Button>(R.id.btn_view_inner_card_master_general__request)
        btnSend.setOnClickListener {
            val message = JDMessageManager.requestToPlayerLevelUp()
            dialog.dismiss()
            listener?.onRequestSelected(message, address)
        }
        eCard.updateCard(context.getString(R.string.card_title_levelup), innerLayout)
    }

    fun updateDialog(address: String, playerName: String) {
        this.address = address
        txvPlayerName.text = playerName
    }

    fun getDialog(): AlertDialog {
        return dialog
    }
}
package jds.projects.ddcompanion.activities.ddsession.playerfragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.cards.ExpandableCardView
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet
import jds.projects.ddcompanion.my_classes.dialogs.DialogThrowResult
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessage
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessageManager

class PlayerSessionRequestsFragment(context: Context, playerSheet: PlayerSheet) : Fragment() {
    private val viewModel = PlayerSessionRequestsViewModel(context, playerSheet)
    private var listener: PlayerSessionRequestsListener? = null

    interface PlayerSessionRequestsListener {
        fun onRequestTC(value: Int, bonus: Int): Boolean
        fun onRequestAbility(id: Int, value: Int, bonus: Int): Boolean
    }

    fun setOnPlayerSessionRequestsListener(listener: PlayerSessionRequestsListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_player_session_requests, container, false)
        initRequestsLayout(root)
        return root
    }

    private fun initRequestsLayout(root: View) {
        setUpTCCard(root)
        setUpAbilityCard(root)
    }

    private fun setUpTCCard(root: View) {
        val expandableCardTC = ExpandableCardView(
            root.findViewById(R.id.card_fragment_player_session_requests__tc),
            requireContext()
        )
        //SETUP CARD TIRO X COLPIRE
        val tcInnerView = View.inflate(requireContext(), R.layout.view_inner_card_player_tc, null)
        val txvBaseValue = tcInnerView.findViewById<TextView>(R.id.txv_inner_card_player_tc__base)
        val edtBonusValue = tcInnerView.findViewById<EditText>(R.id.edt_inner_card_player_tc__bonus)
        val btnSend = tcInnerView.findViewById<Button>(R.id.btn_view_inner_card_player_tc__request)
        txvBaseValue.text = viewModel.throwTC.toString()
        expandableCardTC.canExpand = false
        btnSend.setOnClickListener {
            val bonus = edtBonusValue.text.toString().trim()
            val result = if (bonus.isEmpty())
                listener?.onRequestTC(txvBaseValue.text.toString().toInt(), 0) ?: false
            else
                listener?.onRequestTC(txvBaseValue.text.toString().toInt(), bonus.toInt()) ?: false
            if (!result)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.text_not_connected),
                    Toast.LENGTH_SHORT
                ).show()

        }
        expandableCardTC.updateCard(getString(R.string.request_title_hit_throw), tcInnerView)
    }

    private fun setUpAbilityCard(root: View) {
        val expandableCardAbility = ExpandableCardView(
            root.findViewById(R.id.card_fragment_player_session_requests__ability),
            requireContext()
        )
        //SETUP CARD TIRO ABILITA
        val abilityInnerView =
            View.inflate(requireContext(), R.layout.view_inner_card_player_ability, null)
        val txvBaseValue =
            abilityInnerView.findViewById<TextView>(R.id.txv_inner_card_player_ability__base)
        val edtBonusValue =
            abilityInnerView.findViewById<EditText>(R.id.edt_inner_card_player_ability__bonus)
        val spnAbility =
            abilityInnerView.findViewById<Spinner>(R.id.spn_view_inner_card_player_ability__abilities)
        val btnSend =
            abilityInnerView.findViewById<Button>(R.id.btn_view_inner_card_player_ability__request)

        //txvBaseValue.text = viewModel.abilities[0].level.toString()
        spnAbility.adapter = viewModel.spnAdapter
        spnAbility.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                viewModel.selectedAbility = position
                txvBaseValue.text = viewModel.abilities[position].level.toString()
            }
        }

        btnSend.setOnClickListener {
            val bonus = edtBonusValue.text.toString().trim()
            if (bonus.isEmpty())
                listener?.onRequestAbility(
                    viewModel.selectedAbility,
                    txvBaseValue.text.toString().toInt(),
                    0
                )
            else
                listener?.onRequestAbility(
                    viewModel.selectedAbility,
                    txvBaseValue.text.toString().toInt(),
                    bonus.toInt()
                )
        }
        expandableCardAbility.canExpand = false
        expandableCardAbility.updateCard(
            getString(R.string.request_title_ability_throw),
            abilityInnerView
        )
    }

    fun resultRequest(request: JDMessage, answer: JDMessage) {
        activity?.runOnUiThread {
            if (request.requestID == answer.requestID) {
                when (answer.requestID) {
                    JDMessageManager.TYPE.REQUEST_THROW_TC -> {
                        DialogThrowResult.createHitThrowDialog(requireContext(), null, answer)
                            .show()
                    }
                    JDMessageManager.TYPE.REQUEST_THROW_ABILITY -> {
                        DialogThrowResult.createAbilityThrowDialog(
                            requireContext(),
                            null,
                            answer,
                            viewModel.abilities[answer.ability]
                        ).show()
                    }
                    JDMessageManager.TYPE.REQUEST_THROW_CA -> {
                        DialogThrowResult.createCAThrowDialog(requireContext(), null, answer).show()
                    }
                    JDMessageManager.TYPE.REQUEST_THROW_SAVING -> {
                        DialogThrowResult.createSavingThrowDialog(requireContext(), null, answer)
                            .show()
                    }
                }
            }
        }
    }

    fun errorConnection() {
        activity?.runOnUiThread {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.error_lost_connection),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private class PlayerSessionRequestsViewModel(context: Context, playerSheet: PlayerSheet) {
        val throwTC =
            (playerSheet.playerTalents.map { talent -> talent.modTC }).reduce { acc, i -> acc + i }
        val abilities = playerSheet.playerAbilities
        val spnAdapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_list_item_1,
            playerSheet.playerAbilities.map { dev -> dev.name })
        var selectedAbility: Int = 0
    }
}
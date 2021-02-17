package jds.projects.ddcompanion.activities.main.fragments.gamesession

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.activities.ddsession.MasterSessionActivity
import jds.projects.ddcompanion.activities.ddsession.PlayerSessionActivity
import jds.projects.ddcompanion.my_classes.cards.ExpandableCardView
import jds.projects.ddcompanion.my_classes.utils.JDUtils

class GameSessionFragment : Fragment() {
    private lateinit var viewModel: GameSessionViewModel

    private var listener: GameSessionListener? = null

    interface GameSessionListener {
        fun onPlayerSessionFinished(flagLevelUp: Int, playerSheetID: Int)
    }

    fun setOnGameSessionListener(listener: GameSessionListener) {
        this.listener = listener
    }

    fun newGameManualReceived() {
        viewModel.loadNewGameManuals()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_game_session, container, false)
        viewModel = GameSessionViewModel(requireContext())
        initMasterCard(root.findViewById(R.id.ecard_fragment_game_session__master))
        initPlayerCard(root.findViewById(R.id.ecard_fragment_game_session__player))
        return root
    }

    private fun initMasterCard(root: CardView) {
        //creo la view del master
        val innerView = View.inflate(requireContext(), R.layout.view_game_session_mastercard, null)
        val txvInnerTitle =
            innerView.findViewById<TextView>(R.id.txv_view_game_session_mastercard_innertitle)
        txvInnerTitle.text =
            requireContext().resources.getString(R.string.game_session_fragment_master_innertitle)
        val spnVersion =
            innerView.findViewById<Spinner>(R.id.spn_view_game_session_mastercard__manualversion)
        val manualsAdapter = viewModel.gameManualsAdapter
        spnVersion.adapter = manualsAdapter
        spnVersion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                viewModel.setSelectedGameManual(position)
            }
        }
        //imposto la master card
        val masterCard = ExpandableCardView(root, requireContext())
        masterCard.updateCard(
            requireContext().resources.getString(R.string.game_session_fragment_master),
            innerView
        )
        masterCard.canExpand = false
        masterCard.setOnCardClickListener(object : ExpandableCardView.OnCardClickListener {
            override fun onCardClick() {
                val intent = Intent(requireContext(), MasterSessionActivity::class.java)
                intent.putExtra(JDUtils.BUNDLES.GAME_MANUAL_ID, viewModel.selectedGameManualID)
                startActivity(intent)
            }
        })
    }

    private fun initPlayerCard(root: CardView) {
        //creo la view del player
        val innerView = View.inflate(requireContext(), R.layout.view_game_session_playercard, null)
        val txvInnerTitle =
            innerView.findViewById<TextView>(R.id.txv_view_game_session_playercard__innertitle)
        txvInnerTitle.text =
            requireContext().resources.getString(R.string.game_session_fragment_player_innertitle)
        //imposto la playercard
        val playerCard = ExpandableCardView(root, requireContext())
        playerCard.updateCard(
            requireContext().resources.getString(R.string.game_session_fragment_player),
            innerView
        )
        playerCard.canExpand = false
        playerCard.setOnCardClickListener(object : ExpandableCardView.OnCardClickListener {
            override fun onCardClick() {
                val intent = Intent(requireContext(), PlayerSessionActivity::class.java)
                startActivityForResult(intent, JDUtils.REQUESTS.LEVEL_UP)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == JDUtils.REQUESTS.LEVEL_UP) {
            if (data != null) {
                listener?.onPlayerSessionFinished(
                    data.getIntExtra(JDUtils.BUNDLES.LEVEL_UP, 0),
                    data.getIntExtra(JDUtils.BUNDLES.PLAYER_SHEET_ID, -1)
                )
            }
        }
    }
}
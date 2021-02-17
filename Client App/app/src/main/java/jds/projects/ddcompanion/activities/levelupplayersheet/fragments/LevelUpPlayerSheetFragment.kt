package jds.projects.ddcompanion.activities.levelupplayersheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet
import jds.projects.ddcompanion.my_classes.utils.JDPreferences

class LevelUpPlayerSheetFragment : Fragment() {

    private lateinit var txtName: TextView
    private lateinit var txtManual: TextView
    private lateinit var txtRace: TextView
    private lateinit var txtLevel: TextView
    private lateinit var txtHP: TextView

    private var listener: OnLevelUpFragmentListener? = null

    interface OnLevelUpFragmentListener {
        fun onFragmentReady()
    }

    fun setOnLevelUpFragmentListener(listener: OnLevelUpFragmentListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_level_up_player_sheet, container, false)
        txtName = root.findViewById(R.id.txv_fragment_level_up_player_sheet__playername)
        txtManual = root.findViewById(R.id.txv_fragment_level_up_player_sheet__playermanual)
        txtRace = root.findViewById(R.id.txv_fragment_level_up_player_sheet__playerrace)
        txtLevel = root.findViewById(R.id.txv_fragment_level_up_player_sheet__playerlevel)
        txtHP = root.findViewById(R.id.txv_fragment_level_up_player_sheet__playerhp)
        listener?.onFragmentReady()
        return root
    }

    fun loadPlayerInfo(playerSheet: PlayerSheet) {
        txtName.text = playerSheet.playerName
        txtManual.text =
            ("${requireContext().resources.getString(R.string.game_manual_title)}${playerSheet.manualVersion}")
        txtRace.text = playerSheet.playerRace.names.getTextWithLanguageID(
            JDPreferences.getInstance(requireContext()).language.id
        )
        txtLevel.text = playerSheet.playerLevel.toString()
        txtHP.text = playerSheet.playerHP.toString()
    }

}
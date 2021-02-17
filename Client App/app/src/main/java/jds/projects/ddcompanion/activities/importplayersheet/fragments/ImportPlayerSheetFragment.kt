package jds.projects.ddcompanion.activities.importplayersheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet
import jds.projects.ddcompanion.my_classes.utils.JDPreferences
import jds.projects.ddcompanion.my_classes.viewer.PlayerSheetViewer

class ImportPlayerSheetFragment : Fragment() {
    private lateinit var playerSheetViewer: PlayerSheetViewer
    private lateinit var viewPlayerSheetInfos: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_import_player_sheet, container, false)
        viewPlayerSheetInfos =
            root.findViewById(R.id.view_fragment_import_player_sheet__importedplayersheet)
        playerSheetViewer =
            PlayerSheetViewer(
                viewPlayerSheetInfos,
                requireContext(),
                JDPreferences.getInstance(requireContext()).language.id
            )
        return root
    }

    fun loadPlayerSheet(importedPlayerSheet: PlayerSheet) {
        //se non è visibile la rendo visibile
        if (viewPlayerSheetInfos.visibility != View.VISIBLE)
            viewPlayerSheetInfos.visibility = View.VISIBLE
        playerSheetViewer.loadPlayerSheet(importedPlayerSheet)
    }
}
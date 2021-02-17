package jds.projects.ddcompanion.activities.viewplayersheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.utils.JDPreferences
import jds.projects.ddcompanion.my_classes.viewer.PlayerSheetViewer

class ViewPlayerSheetFragment(private var sheetID: Int) : Fragment() {

    private lateinit var viewer: PlayerSheetViewer
    private lateinit var model: ViewPlayerSheetModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_view_player_sheet, container, false)
        model =
            ViewPlayerSheetModel(
                requireContext(),
                sheetID
            )
        viewer = PlayerSheetViewer(
            root.findViewById(R.id.view_activity_view_player_sheet__playerviewer),
            requireContext(),
            JDPreferences.getInstance(requireContext()).language.id
        )
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewer.loadPlayerSheet(model.playerSheet)
    }
}
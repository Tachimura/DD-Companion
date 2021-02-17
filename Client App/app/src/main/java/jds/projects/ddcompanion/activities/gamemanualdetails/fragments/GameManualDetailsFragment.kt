package jds.projects.ddcompanion.activities.gamemanualdetails.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.utils.JDPreferences
import jds.projects.ddcompanion.my_classes.viewer.GameManualViewer

class GameManualDetailsFragment(private var manualID: Int) : Fragment() {
    private lateinit var viewer: GameManualViewer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_game_manual_details, container, false)

        viewer = GameManualViewer(
            requireContext(),
            root.findViewById<View>(R.id.view_activity_game_manual_details_patchnotes)
        )
        viewer.loadGameManual(manualID, JDPreferences.getInstance(requireContext()).language.id)
        return root
    }
}
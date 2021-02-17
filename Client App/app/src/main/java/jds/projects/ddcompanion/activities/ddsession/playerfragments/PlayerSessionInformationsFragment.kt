package jds.projects.ddcompanion.activities.ddsession.playerfragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet
import jds.projects.ddcompanion.my_classes.utils.JDPreferences
import jds.projects.ddcompanion.my_classes.viewer.PlayerSheetViewer

class PlayerSessionInformationsFragment(
    context: Context,
    address: String,
    manualText: String,
    playerSheet: PlayerSheet
) : Fragment() {
    private var viewModel = ViewModel(context, address, manualText, playerSheet)
    private lateinit var viewer: PlayerSheetViewer
    private lateinit var txvWiFiStatus: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_player_session_informations, container, false)

        val txvAddress =
            root.findViewById<TextView>(R.id.txv_fragment_player_session_informations__ip)
        val txvIP =
            root.findViewById<TextView>(R.id.txv_fragment_player_session_informations__manual)
        txvWiFiStatus = root.findViewById(R.id.txv_fragment_player_session_informations__wifistatus)
        viewer = PlayerSheetViewer(
            root.findViewById(R.id.view_fragment_player_session_informations__playerviewer),
            requireContext(),
            JDPreferences.getInstance(requireContext()).language.id
        )
        viewer.loadPlayerSheet(viewModel.playerSheet)

        txvAddress.text = viewModel.address
        txvIP.text = viewModel.manualText
        changeWiFiStatusText(true)
        return root
    }

    fun changeWiFiStatusText(status: Boolean) {
        //se sono online
        if (status) {
            txvWiFiStatus.text = viewModel.statusOnline
            txvWiFiStatus.setTextColor(viewModel.colorGreen)
            //se sono offline
        } else {
            txvWiFiStatus.text = viewModel.statusOffline
            txvWiFiStatus.setTextColor(viewModel.colorRed)
        }
    }

    private class ViewModel(
        context: Context,
        val address: String,
        val manualText: String,
        val playerSheet: PlayerSheet
    ) {

        val colorRed: Int =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                context.resources.getColor(android.R.color.holo_red_light, context.theme)
            else
                context.resources.getColor(android.R.color.holo_red_light)
        val colorGreen: Int =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                context.resources.getColor(android.R.color.holo_green_light, context.theme)
            else
                context.resources.getColor(android.R.color.holo_green_light)

        val statusOnline: String = context.resources.getString(R.string.status_online)
        val statusOffline: String = context.resources.getString(R.string.status_offline)
    }
}
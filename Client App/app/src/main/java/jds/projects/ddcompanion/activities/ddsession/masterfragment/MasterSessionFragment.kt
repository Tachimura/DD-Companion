package jds.projects.ddcompanion.activities.ddsession.masterfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.activities.ddsession.MasterSessionActivity
import jds.projects.ddcompanion.my_classes.dd_classes.DDAbility
import jds.projects.ddcompanion.my_classes.dialogs.DialogMasterRequests
import jds.projects.ddcompanion.my_classes.dialogs.DialogThrowResult
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessage
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessageManager

class MasterSessionFragment(private var address: String, private var manualText: String) :
    Fragment() {

    private lateinit var viewModel: MasterSessionViewModel
    private lateinit var lsvPlayers: ListView
    private lateinit var txvWiFiStatus: TextView
    private lateinit var txvAddress: TextView

    private var listener: MasterRequestListener? = null

    interface MasterRequestListener {
        fun onRequest(request: JDMessage, address: String)
    }

    fun setOnMasterRequestListener(listener: MasterRequestListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_master_main, container, false)
        viewModel =
            MasterSessionViewModel(requireContext())
        lsvPlayers = root.findViewById(R.id.lsv_fragment_master_main__devices)
        txvAddress = root.findViewById(R.id.txv_fragment_master_main__ip)
        val txvManualID = root.findViewById<TextView>(R.id.txv_fragment_master_main__manual)
        txvWiFiStatus = root.findViewById(R.id.txv_fragment_master_main__wifistatus)
        synchronized(viewModel.canRequest) {
            viewModel.myAddress = address
        }
        txvAddress.text = address
        txvManualID.text = manualText

        lsvPlayers.adapter = viewModel.adapter
        lsvPlayers.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                var canRequest: Boolean
                synchronized(viewModel.status) {
                    canRequest = viewModel.canRequest
                }
                if (canRequest)
                    showDialogRequests(
                        viewModel.linkedDevices[position].first,
                        viewModel.linkedDevices[position].second
                    )
                else
                    Toast.makeText(context, getString(R.string.status_offline), Toast.LENGTH_SHORT)
                        .show()
            }
        changeWiFiStatusText(true)
        viewModel.createDialog(
            DialogMasterRequests(
                this,
                (activity as MasterSessionActivity).useableViewModel.abilities
            ).apply {
                setOnRequestListener(object : DialogMasterRequests.RequestListener {
                    override fun onRequestSelected(request: JDMessage, address: String) {
                        var canRequest: Boolean
                        synchronized(viewModel.canRequest) {
                            canRequest = viewModel.canRequest
                        }
                        if (canRequest)
                            listener?.onRequest(request, address)
                        else
                            activity?.runOnUiThread {
                                Toast.makeText(
                                    context,
                                    context.resources.getString(R.string.status_offline),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                })
            }
        )
        return root
    }

    private fun showDialogRequests(address: String, playerName: String) {
        val dialog = viewModel.updateDialog(address, playerName)
        dialog.show()
    }

    fun changeWiFiStatusText(status: Boolean) {
        synchronized(viewModel.canRequest) {
            viewModel.status = status
        }
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

    fun addConnection(address: String, name: String) {
        viewModel.addConnection(address, name)
    }

    fun removeConnection(address: String) {
        viewModel.removeConnection(address)
    }

    fun getPlayerByAddress(address: String): String {
        return viewModel.getPlayerByAddress(address)
    }

    fun receiveAnswer(address: String, answer: JDMessage, ability: DDAbility?) {
        when (answer.requestID) {
            JDMessageManager.TYPE.REQUEST_THROW_TC -> {
                DialogThrowResult.createHitThrowDialog(
                    requireContext(),
                    getPlayerByAddress(address),
                    answer
                ).show()
            }
            JDMessageManager.TYPE.REQUEST_THROW_CA -> {
                DialogThrowResult.createCAThrowDialog(
                    requireContext(),
                    getPlayerByAddress(address),
                    answer
                ).show()
            }
            JDMessageManager.TYPE.REQUEST_THROW_ABILITY -> {
                DialogThrowResult.createAbilityThrowDialog(
                    requireContext(),
                    getPlayerByAddress(address),
                    answer,
                    ability!!
                ).show()
            }
            JDMessageManager.TYPE.REQUEST_THROW_SAVING -> {
                DialogThrowResult.createSavingThrowDialog(
                    requireContext(),
                    getPlayerByAddress(address),
                    answer
                ).show()
            }
            JDMessageManager.TYPE.REQUEST_LEVEL_UP -> {
                Toast.makeText(
                    requireContext(),
                    getPlayerByAddress(address) + " " + resources.getString(R.string.toast_level_up_text),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun updateMasterAddress(newIpAddress: String) {
        synchronized(viewModel.canRequest) {
            viewModel.myAddress = newIpAddress
        }
        txvAddress.text = newIpAddress
    }

    fun getConnectedPlayers(): Array<String> {
        return viewModel.linkedDevices.map { devices -> devices.first }.toTypedArray()
    }
}
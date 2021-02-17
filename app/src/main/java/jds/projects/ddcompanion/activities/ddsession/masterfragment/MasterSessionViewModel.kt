package jds.projects.ddcompanion.activities.ddsession.masterfragment

import android.content.Context
import android.os.Build
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import jds.projects.ddcompanion.my_classes.dialogs.DialogMasterRequests

class MasterSessionViewModel(context: Context) {

    val canRequest: Boolean
        get() = status && myAddress != "0.0.0.0"

    var myAddress = "0.0.0.0"
    var status = false

    val colorRed: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        context.resources.getColor(android.R.color.holo_red_light, context.theme)
    else
        context.resources.getColor(android.R.color.holo_red_light)
    val colorGreen: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        context.resources.getColor(android.R.color.holo_green_light, context.theme)
    else
        context.resources.getColor(android.R.color.holo_green_light)

    val statusOnline: String =
        context.resources.getString(jds.projects.ddcompanion.R.string.status_online)
    val statusOffline: String =
        context.resources.getString(jds.projects.ddcompanion.R.string.status_offline)

    //address + name
    private var mLinkedDevices = mutableListOf<Pair<String, String>>()
    val linkedDevices: MutableList<Pair<String, String>>
        get() = mLinkedDevices
    private var arrayAdapter: ArrayAdapter<String> =
        ArrayAdapter(context, android.R.layout.simple_list_item_1)
    val adapter: ArrayAdapter<String>
        get() = arrayAdapter

    private lateinit var dialog: DialogMasterRequests

    fun addConnection(address: String, name: String) {
        val linkedDevice = mLinkedDevices.find { dev -> dev.first == address }
        if (linkedDevice == null) {
            val newConnection = Pair(address, name)
            mLinkedDevices.add(newConnection)
            arrayAdapter.add(connectionToString(newConnection))
            arrayAdapter.notifyDataSetChanged()
        } else {
            //se ne è presente già uno con un nome diverso aggiorno il nome
            if (linkedDevice.second != name) {
                val index = mLinkedDevices.indexOf(linkedDevice)
                mLinkedDevices.removeAt(index)
                mLinkedDevices.add(index, Pair(address, name))
                updateArrayAdapter()
            }
        }
    }

    fun removeConnection(address: String) {
        for (linkedDevice in mLinkedDevices) {
            if (linkedDevice.first == address) {
                mLinkedDevices.remove(linkedDevice)
                arrayAdapter.remove(connectionToString(linkedDevice))
                arrayAdapter.notifyDataSetChanged()
                break
            }
        }
    }

    private fun connectionToString(connection: Pair<String, String>): String {
        //SECOND = NOME PLAYER, FIRST = ADDRESS
        return connection.second + "\n" + connection.first
    }

    fun getPlayerByAddress(address: String): String {
        return mLinkedDevices.find { device -> device.first == address }!!.second
    }

    private fun updateArrayAdapter() {
        arrayAdapter.clear()
        arrayAdapter.addAll(mLinkedDevices.map { device -> connectionToString(device) })
        arrayAdapter.notifyDataSetChanged()
    }

    fun updateDialog(address: String, playerName: String): AlertDialog {
        dialog.updateDialog(address, playerName)
        return dialog.getDialog()
    }

    fun createDialog(dialog: DialogMasterRequests) {
        this.dialog = dialog
        this.dialog.create()
    }
}
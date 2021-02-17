package jds.projects.ddcompanion.my_classes.utils

import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDConnection
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessage
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDWiFiBroadcastReceiver
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.WiFiServer

abstract class WiFiActivity : DayNightActivity() {
    protected lateinit var viewModel: WiFiViewModel
    private var receiver: JDWiFiBroadcastReceiver? = null
    private val intentFilter = IntentFilter().apply {
        addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        /*
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        */
    }
    protected open fun onActivityReady() {}
    //richiamata x far partire la sessione di gioco
    protected open fun onGameSessionReady() {}
    protected abstract fun manageRequest(connection: JDConnection, request: JDMessage): JDMessage
    protected abstract fun manageAnswer(connection: JDConnection, request: JDMessage, answer: JDMessage)

    protected open fun wifiStatusChanged(status: Boolean) {
        viewModel.changeWiFiStatusText(status)
    }

    protected open fun wifiDeviceStatusChanged() {
        viewModel.updateAddress()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiver = JDWiFiBroadcastReceiver()
        receiver?.setOnJDWiFiEventListener(object : JDWiFiBroadcastReceiver.JDWiFiEventListener {
            override fun onWiFiStatusChanged(online: Boolean) {
                viewModel.wifiStatus = online
                wifiStatusChanged(online)
            }

            override fun onWiFiDeviceStatusChanged() {
                wifiDeviceStatusChanged()
            }
        })
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        //inizializzo il server e lo faccio partire in attesa di ricevere messaggi
        viewModel.server = WiFiServer().apply {
            setOnWiFiServerListener(object : WiFiServer.WiFiServerListener {
                override fun onRequestReceived(
                    connection: JDConnection,
                    request: JDMessage
                ): JDMessage {
                    return manageRequest(connection, request)
                }
            })
        }
        viewModel.server?.run {
            start()
        }
        onActivityReady()
    }

    override fun onBackPressed() {
        shutDownActivityTasks()
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        shutDownActivityTasks()
    }

    private fun shutDownActivityTasks() {
        viewModel.server?.stop()
    }

    /* register the broadcast receiver with the intent values to be matched */
    override fun onResume() {
        super.onResume()
        receiver?.also { receiver ->
            registerReceiver(receiver, intentFilter)
        }
    }

    /* unregister the broadcast receiver */
    override fun onPause() {
        super.onPause()
        receiver?.also { receiver ->
            unregisterReceiver(receiver)
        }
    }
}
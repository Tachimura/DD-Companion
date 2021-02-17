package jds.projects.ddcompanion.my_classes.io.network.gamesession_networking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager

class JDWiFiBroadcastReceiver : BroadcastReceiver() {
    private var listener: JDWiFiEventListener? = null

    interface JDWiFiEventListener {
        fun onWiFiStatusChanged(online: Boolean)
        fun onWiFiDeviceStatusChanged()
    }

    fun setOnJDWiFiEventListener(listener: JDWiFiEventListener) {
        this.listener = listener
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                when (intent.getIntExtra(
                    WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_DISABLED
                )) {
                    WifiManager.WIFI_STATE_ENABLED -> {
                        listener?.onWiFiStatusChanged(true)
                    }
                    WifiManager.WIFI_STATE_DISABLED -> {
                        listener?.onWiFiStatusChanged(false)
                    }
                }
            }

            WifiManager.NETWORK_STATE_CHANGED_ACTION -> {
                listener?.onWiFiDeviceStatusChanged()
            }

            /*
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // Check to see if Wi-Fi is enabled and notify appropriate activity
                when (intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)) {
                    WifiP2pManager.WIFI_P2P_STATE_ENABLED -> {
                        // Wifi P2P is enabled
                        listener?.onWiFiStatusChanged(true)
                    }
                    else -> {
                        // Wi-Fi P2P is not enabled
                        listener?.onWiFiStatusChanged(false)
                    }
                }
            }
             */
            /*
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                listener?.onWiFiDeviceStatusChanged()
            }
             */
        }
    }
}
package jds.projects.ddcompanion.my_classes.utils

import android.content.Context
import android.net.wifi.WifiManager
import jds.projects.ddcompanion.my_classes.dd_classes.DDAbilityManager
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.WiFiServer
import java.net.InetAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder

abstract class WiFiViewModel(protected var activity: WiFiActivity) {
    var server: WiFiServer? = null
    val abilities = DDAbilityManager.getInstance(activity).getAbilities()
    protected lateinit var ipAddress: String
    val myAddress: String
        get() = ipAddress

    abstract fun changeWiFiStatusText(status: Boolean)
    abstract fun changedAddress()

    init {
        updateAddressValue()
    }

    protected var mManualID: Int = 1
    val manualID: Int
        get() = mManualID

    private var _wifiStatus: Boolean = false
    var wifiStatus: Boolean
        get() = synchronized(_wifiStatus) { _wifiStatus }
        set(value) = synchronized(_wifiStatus) { _wifiStatus = value }


    fun updateAddress() {
        val oldAddress = ipAddress
        updateAddressValue()
        if (oldAddress != ipAddress)
            changedAddress()
    }

    private fun updateAddressValue() {
        //prendo l'indirizzo ip
        val ipAddressToFormat =
            (activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).connectionInfo.ipAddress
        ipAddress = InetAddress.getByAddress(
            ByteBuffer
                .allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(ipAddressToFormat)
                .array()
        ).hostAddress
    }
}
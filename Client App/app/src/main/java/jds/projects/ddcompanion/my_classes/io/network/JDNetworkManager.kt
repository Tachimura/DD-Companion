package jds.projects.ddcompanion.my_classes.io.network

import android.content.Context
import android.net.ConnectivityManager


object JDNetworkManager {

    /**
     * @param context: Context
     * @return networkConnected: Boolean
     * Returns the availability to use network features
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }
}
package jds.projects.ddcompanion.my_classes.io.network

import android.util.Log
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

class NetworkRequester : Thread() {
    private lateinit var requestedURL: URL
    private var listener: NetworkRequesterListener? = null
    private var postParams: HashMap<String, String> = hashMapOf()

    object REQUEST {
        const val CHECK_LATEST_GAME_MANUAL =
            "https://jdj0k3r.altervista.org/DDCompanion/latest_game_manual.php"
        const val DOWNLOAD_NEW_GAME_MANUALS =
            "https://jdj0k3r.altervista.org/DDCompanion/download_new_game_manuals.php"
    }

    interface NetworkRequesterListener {
        fun onRequestStart(postParams: HashMap<String, String>)
        fun onRequestEnd(result: JSONObject)
    }

    fun prepareRequest(requestedURL: String) {
        this.requestedURL = URL(requestedURL)
        start()
    }

    fun setOnNetworkRequesterListener(listener: NetworkRequesterListener) {
        this.listener = listener
    }

    private fun getPostDataString(params: HashMap<String, String>): String? {
        val result = java.lang.StringBuilder()
        var first = true
        for ((key, value) in params.entries) {
            if (first) first = false else result.append("&")
            result.append(URLEncoder.encode(key, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode(value, "UTF-8"))
        }
        return result.toString()
    }

    override fun run() {
        onPreExecute()
        var response = JSONObject()
        try {
            val urlConnection = requestedURL.openConnection() as HttpURLConnection
            urlConnection.readTimeout = 15000
            urlConnection.connectTimeout = 15000
            urlConnection.requestMethod = "POST"
            urlConnection.doInput = true
            urlConnection.doOutput = true
            //inserisco i dati post se sono presenti
            val os: OutputStream = urlConnection.outputStream
            val writer = BufferedWriter(
                OutputStreamWriter(os, "UTF-8")
            )
            writer.write(getPostDataString(postParams))
            writer.flush()
            writer.close()
            os.close()
            val responseCode = urlConnection.responseCode
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                val inputStream = urlConnection.inputStream
                val content = StringBuilder()
                // get text from stream, convert to string and send to main thread.
                val allText = inputStream.bufferedReader().use { it.readText() }
                content.append(allText)
                response = JSONObject(content.toString())
                inputStream.close()
            } else {
                response.put("success", -2)
                response.put("error_code", "Error, wrong response code")
            }
            urlConnection.disconnect()
        } catch (e: Exception) {
            Log.println(Log.ERROR, "NEWTORKREQUESTER_ERROR", "e -> " + e.message)
            response.put("success", -2)
            response.put("error_code", "Error, cannot create connection to the server")
        }
        onPostExecute(response)
    }

    private fun onPreExecute() {
        listener?.onRequestStart(postParams)
    }

    private fun onPostExecute(result: JSONObject) {
        listener?.onRequestEnd(result)
    }
}
package jds.projects.ddcompanion.my_classes.io.network.gamesession_networking

import jds.projects.ddcompanion.my_classes.utils.JDUtils
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class WiFiCommunicator(private val address: String) {
    private var listener: WiFiCommunicatorListener? = null

    interface WiFiCommunicatorListener {
        fun onAnswerReceived(connection: JDConnection, request: JDMessage, answer: JDMessage)
        fun onConnectionCancelled()
        fun onConnectionClosed()
    }

    fun setOnWiFiCommunicatorListener(listener: WiFiCommunicatorListener) {
        this.listener = listener
    }

    fun sendRequest(request: JDMessage) {
        JDCommunication(address, request).apply {
            setOnJDCommunicationListener(object : JDCommunication.JDCommunicationListener {
                override fun onAnswerReceived(
                    connection: JDConnection,
                    request: JDMessage,
                    answer: JDMessage
                ) {
                    listener?.onAnswerReceived(connection, request, answer)
                }

                override fun onConnectionCancelled() {
                    listener?.onConnectionCancelled()
                }

                override fun onConnectionClosed() {
                    listener?.onConnectionClosed()
                }
            })
        }.start()
    }

    private class JDCommunication(private var address: String, private var request: JDMessage) :
        Thread() {
        private var listener: JDCommunicationListener? = null

        interface JDCommunicationListener {
            fun onAnswerReceived(connection: JDConnection, request: JDMessage, answer: JDMessage)
            fun onConnectionCancelled()
            fun onConnectionClosed()
        }

        fun setOnJDCommunicationListener(listener: JDCommunicationListener) {
            this.listener = listener
        }

        override fun run() {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect(InetSocketAddress(address, JDUtils.WiFi.PORT), 2000)
                val connection = JDConnection(socket).apply {
                    setOnJDActionListener(object : JDConnection.JDConnectionListener {
                        override fun onRequestReceived(request: JDMessage): JDMessage {
                            return JDMessage()
                        }

                        override fun onAnswerReceived(request: JDMessage, answer: JDMessage) {
                            listener?.onAnswerReceived(this@apply, request, answer)
                        }

                        override fun onConnectionCancelled() {
                            listener?.onConnectionCancelled()
                        }

                        override fun onConnectionClosed() {
                            listener?.onConnectionClosed()
                        }
                    })
                }
                connection.sendRequest(request)
            } catch (e: IOException) {
                listener?.onConnectionCancelled()
            }
        }
    }
}
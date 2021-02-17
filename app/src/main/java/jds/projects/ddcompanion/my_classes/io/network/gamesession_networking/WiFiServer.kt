package jds.projects.ddcompanion.my_classes.io.network.gamesession_networking

import jds.projects.ddcompanion.my_classes.utils.JDUtils
import java.io.IOException
import java.net.ServerSocket

class WiFiServer {
    private var serverTask: ServerThread? = null

    private lateinit var listener: WiFiServerListener

    interface WiFiServerListener {
        fun onRequestReceived(connection: JDConnection, request: JDMessage): JDMessage
    }

    fun setOnWiFiServerListener(listener: WiFiServerListener) {
        this.listener = listener
    }

    val isRunning: Boolean
        get() = serverTask?.serverStatus == ServerThread.RUNNING

    fun start() {
        //se c'è già un server in esecuzione lo fermo
        if (isRunning)
            stop()
        serverTask = ServerThread().apply {
            setOnServerSocketListener(object : ServerThread.ServerSocketListener {
                override fun onClientRequestReceived(
                    connection: JDConnection,
                    request: JDMessage
                ): JDMessage {
                    return listener.onRequestReceived(connection, request)
                }
            })
        }
        serverTask?.start()
    }

    fun stop() {
        serverTask?.run {
            if (serverStatus == ServerThread.RUNNING)
                shutdown()
        }
    }

    fun prepareToClose() {
        serverTask?.prepareToClose()
    }

    private class ServerThread : Thread() {
        companion object {
            const val NOT_RUNNING = 0
            const val RUNNING = 1
        }

        private val activeConnections = mutableListOf<JDConnection>()
        private var _serverStatus = NOT_RUNNING
        private var acceptNewRequests = true
        val serverStatus: Int
            get() = _serverStatus

        private lateinit var listener: ServerSocketListener

        interface ServerSocketListener {
            fun onClientRequestReceived(connection: JDConnection, request: JDMessage): JDMessage
        }

        fun setOnServerSocketListener(listener: ServerSocketListener) {
            this.listener = listener
        }

        private var serverSocket: ServerSocket? = null

        override fun run() {
            _serverStatus = RUNNING
            try {
                serverSocket = ServerSocket(JDUtils.WiFi.PORT)
                while (synchronized(_serverStatus) { serverStatus } == RUNNING && synchronized(
                        acceptNewRequests
                    ) { acceptNewRequests }) {
                    val client = serverSocket?.accept()
                    client?.run {
                        val clientConnection = JDConnection(this).apply {
                            setOnJDActionListener(object : JDConnection.JDConnectionListener {
                                override fun onRequestReceived(request: JDMessage): JDMessage {
                                    return listener.onClientRequestReceived(this@apply, request)
                                }

                                override fun onAnswerReceived(
                                    request: JDMessage,
                                    answer: JDMessage
                                ) {
                                }

                                override fun onConnectionCancelled() {
                                    activeConnections.remove(this@apply)
                                }

                                override fun onConnectionClosed() {
                                    activeConnections.remove(this@apply)
                                }
                            })
                        }
                        activeConnections.add(clientConnection)
                        //mi metto in attesa di una richiesta
                        clientConnection.awaitRequest()
                    }
                }
            } catch (e: IOException) {
                shutdown()
            }
        }

        @Synchronized
        fun shutdown() {
            prepareToClose()
            synchronized(_serverStatus) {
                if (_serverStatus == RUNNING) {
                    _serverStatus = NOT_RUNNING
                    serverSocket?.close()
                }
            }
        }

        @Synchronized
        fun prepareToClose() {
            synchronized(acceptNewRequests) {
                acceptNewRequests = false
            }
        }
    }
}
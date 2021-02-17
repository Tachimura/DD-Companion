package jds.projects.ddcompanion.my_classes.io.network.gamesession_networking

import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

class JDConnection(private var _socket: Socket) {
    private object TYPE {
        const val SEND_REQUEST = 0
        const val AWAIT_REQUEST = 1
    }

    val socket: Socket
        get() = _socket

    private var listener: JDConnectionListener? = null

    interface JDConnectionListener {
        fun onRequestReceived(request: JDMessage): JDMessage?
        fun onAnswerReceived(request: JDMessage, answer: JDMessage)
        fun onConnectionCancelled()
        fun onConnectionClosed()
    }

    fun setOnJDActionListener(listener: JDConnectionListener) {
        this.listener = listener
    }

    fun sendRequest(request: JDMessage) {
        val connector = JDConnector(this, TYPE.SEND_REQUEST).apply {
            setMessage(request)
            setOnJDConnectorListener(object : JDConnector.JDConnectorListener {
                override fun onRequestReceived(
                    request: JDMessage
                ): JDMessage? {
                    //UNSUPPORTED OPERATION EXCEPTION, NON SI DOVREBBE MAI FINIRE QUA
                    throw UnsupportedOperationException("WTF?!? GET OUT OF HERE REQUEST_RECEIVED")
                }

                override fun onAnswerReceived(
                    request: JDMessage,
                    answer: JDMessage
                ) {
                    listener?.onAnswerReceived(request, answer)
                }

                override fun onRequestCancelled() {
                    listener?.onConnectionCancelled()
                }

                override fun onConnectionClosed() {
                    listener?.onConnectionClosed()
                }
            })
        }
        connector.start()
    }

    fun awaitRequest() {
        val connector = JDConnector(this, TYPE.AWAIT_REQUEST).apply {
            setOnJDConnectorListener(object : JDConnector.JDConnectorListener {
                override fun onRequestReceived(
                    request: JDMessage
                ): JDMessage? {
                    return listener?.onRequestReceived(request) ?: JDMessage()
                }

                override fun onAnswerReceived(
                    request: JDMessage,
                    answer: JDMessage
                ) {
                    //UNSUPPORTED OPERATION EXCEPTION, NON SI DOVREBBE MAI FINIRE QUA
                    throw UnsupportedOperationException("WTF?!? GET OUT OF HERE - ANSWER_RECEIVED")
                }

                override fun onRequestCancelled() {
                    listener?.onConnectionCancelled()
                }

                override fun onConnectionClosed() {
                    listener?.onConnectionClosed()
                }
            })
        }
        connector.start()
    }

    fun close() {
        try {
            _socket.apply {
                if (!isClosed && isConnected) {
                    if (!isOutputShutdown)
                        shutdownOutput()
                    if (!isInputShutdown)
                        shutdownInput()
                    close()
                }
            }
        } catch (e: IOException) {
        }
    }

    private class JDConnector(private var connection: JDConnection, private var type: Int) :
        Thread() {
        private lateinit var message: JDMessage

        private lateinit var requestReceived: JDMessage
        private lateinit var answerReceived: JDMessage

        private var listener: JDConnectorListener? = null
        private var error = false

        interface JDConnectorListener {
            fun onRequestReceived(request: JDMessage): JDMessage?
            fun onAnswerReceived(request: JDMessage, answer: JDMessage)
            fun onRequestCancelled()
            fun onConnectionClosed()
        }

        fun setOnJDConnectorListener(listener: JDConnectorListener) {
            this.listener = listener
        }

        fun setMessage(message: JDMessage) {
            this.message = message
        }

        override fun run() {
            try {
                if (connection.socket.isConnected) {
                    val outputStream = ObjectOutputStream(connection.socket.outputStream)
                    val inputStream = ObjectInputStream(connection.socket.inputStream)
                    outputStream.flush()
                    if (type == TYPE.SEND_REQUEST) {
                        //invio la richiesta
                        outputStream.writeObject(message)
                        //ricevo la risposta
                        answerReceived = inputStream.readObject() as JDMessage
                        listener?.onAnswerReceived(message, answerReceived)
                    } else {
                        //attendo la richiesta
                        requestReceived = inputStream.readObject() as JDMessage
                        message = listener?.onRequestReceived(requestReceived) ?: JDMessage()
                        //invio la risposta
                        outputStream.writeObject(message)
                    }
                }
            } catch (e: IOException) {
                error = true
            } finally {
                //chiudo la comunicazione
                connection.close()
                if (error)
                    listener?.onRequestCancelled()
                else
                    listener?.onConnectionClosed()
            }
        }
    }
}
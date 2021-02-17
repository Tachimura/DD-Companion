package jds.projects.ddcompanion.activities.ddsession

import android.os.Bundle
import android.widget.Toast
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.dialogs.DialogMasterRequestReply
import jds.projects.ddcompanion.my_classes.dialogs.DialogThrowResult
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDConnection
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessage
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessageManager
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.WiFiCommunicator
import jds.projects.ddcompanion.my_classes.utils.JDUtils
import jds.projects.ddcompanion.my_classes.utils.WiFiActivity
import jds.projects.ddcompanion.my_classes.utils.WiFiViewModel
import java.util.concurrent.CountDownLatch

class MasterSessionActivity : WiFiActivity() {
    val useableViewModel: WiFiViewModel
        get() = viewModel

    override fun manageRequest(connection: JDConnection, request: JDMessage): JDMessage {
        val address = request.address
        return when (request.requestID) {
            JDMessageManager.TYPE.REQUEST_GAME_MANUAL -> {
                JDMessageManager.replyToPlayerSessionManual(viewModel.manualID)
            }
            JDMessageManager.TYPE.REQUEST_CONNECTION -> {
                val accept = viewModel.server?.isRunning ?: false
                if (accept) {
                    //salvo i valori x evitare crash dato che il socket può essere
                    //cancellato dal garbage collector prima che runOnUiThread parta
                    val playerName = request.message
                    runOnUiThread {
                        (viewModel as MasterSessionActivityViewModel)
                            .addConnection(address, playerName)
                    }
                }
                JDMessageManager.replyToPlayerConnection(accept)
            }
            JDMessageManager.TYPE.REQUEST_DISCONNECTION -> {
                runOnUiThread {
                    Toast.makeText(
                        this,
                        getString(
                            R.string.toast_player_disconnected,
                            (viewModel as MasterSessionActivityViewModel)
                                .getPlayerByAddress(request.address)
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                    (viewModel as MasterSessionActivityViewModel).removeConnection(address)
                }
                JDMessageManager.replyDisconnection()
            }
            JDMessageManager.TYPE.REQUEST_THROW_TC -> {
                with(viewModel as MasterSessionActivityViewModel) {
                    return createDialogThrowTC(
                        getPlayerByAddress(address),
                        request
                    )
                }
            }
            JDMessageManager.TYPE.REQUEST_THROW_ABILITY -> {
                with(viewModel as MasterSessionActivityViewModel) {
                    return createDialogThrowAbility(
                        getPlayerByAddress(address),
                        request.apply { message = abilities[request.ability].name })
                }
            }
            //messaggio nullo, rispondo con messaggio nullo
            else -> {
                JDMessage()
            }
        }
    }

    @Synchronized
    private fun createDialogThrowTC(playerName: String, request: JDMessage): JDMessage {
        var answer = JDMessage()
        val latch = CountDownLatch(1)
        runOnUiThread {
            val dialog = DialogMasterRequestReply(this, playerName, request).apply {
                setOnRequestListener(object : DialogMasterRequestReply.RequestListener {
                    override fun onResultAvailable(dice: Int, result: Int, difficolta: Int) {
                        answer = JDMessageManager.replyToPlayerThrowTC(
                            dice,
                            result,
                            request.punteggioBase,
                            request.punteggioBonus,
                            difficolta
                        )
                        DialogThrowResult.createHitThrowDialog(
                            this@MasterSessionActivity,
                            playerName,
                            answer
                        )
                            .show()
                        latch.countDown()
                    }
                })
            }
            dialog.show()
        }
        latch.await()
        return answer
    }

    @Synchronized
    private fun createDialogThrowAbility(playerName: String, request: JDMessage): JDMessage {
        var answer = JDMessage()
        val latch = CountDownLatch(1)
        runOnUiThread {
            val dialog = DialogMasterRequestReply(this, playerName, request).apply {
                setOnRequestListener(object : DialogMasterRequestReply.RequestListener {
                    override fun onResultAvailable(dice: Int, result: Int, difficolta: Int) {
                        answer = JDMessageManager.replyToPlayerThrowAbility(
                            request.ability,
                            dice,
                            result,
                            request.punteggioBase,
                            request.punteggioBonus,
                            difficolta
                        )
                        DialogThrowResult.createAbilityThrowDialog(
                            this@MasterSessionActivity,
                            playerName,
                            answer,
                            viewModel.abilities[request.ability]
                        ).show()
                        latch.countDown()
                    }
                })
            }
            dialog.show()
        }
        latch.await()
        return answer
    }

    override fun manageAnswer(connection: JDConnection, request: JDMessage, answer: JDMessage) {
        runOnUiThread {
            (viewModel as MasterSessionActivityViewModel).reportAnswer(answer.address, answer)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master_session)
        //inizializzo l'activity
        viewModel = MasterSessionActivityViewModel(
            this,
            intent.extras!!.getInt(JDUtils.BUNDLES.GAME_MANUAL_ID)
        )
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    (viewModel as MasterSessionActivityViewModel).fragment
                )
                .commitNow()
        }
    }

    fun sendRequest(request: JDMessage, address: String) {
        val requester = WiFiCommunicator(address).apply {
            setOnWiFiCommunicatorListener(object : WiFiCommunicator.WiFiCommunicatorListener {
                override fun onAnswerReceived(
                    connection: JDConnection, request: JDMessage, answer: JDMessage) {
                    manageAnswer(connection, request, answer)
                }
                override fun onConnectionCancelled() {
                    runOnUiThread {
                        Toast.makeText(
                            baseContext,
                            getString(R.string.error_lost_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                override fun onConnectionClosed() {}
            })
        }
        requester.sendRequest(request)
    }


    private fun sendDisconnectionToPlayers() {
        with(viewModel as MasterSessionActivityViewModel) {
            server?.prepareToClose()
            //mando un messaggio ad ogni player(in questo caso address)
            for (player in connectedPlayers) {
                val request = JDMessageManager.requestDisconnection(viewModel.myAddress)
                val communicator = WiFiCommunicator(player).apply {
                    setOnWiFiCommunicatorListener(object :
                        WiFiCommunicator.WiFiCommunicatorListener {
                        override fun onAnswerReceived(
                            connection: JDConnection,
                            request: JDMessage,
                            answer: JDMessage
                        ) {
                        }

                        override fun onConnectionCancelled() {
                            runOnUiThread {
                                Toast.makeText(
                                    this@MasterSessionActivity,
                                    getString(R.string.error_lost_connection),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onConnectionClosed() {}
                    })
                }
                communicator.sendRequest(request)
            }
        }
    }

    override fun onBackPressed() {
        sendDisconnectionToPlayers()
        super.onBackPressed()
    }

    override fun onDestroy() {
        sendDisconnectionToPlayers()
        super.onDestroy()
    }
}
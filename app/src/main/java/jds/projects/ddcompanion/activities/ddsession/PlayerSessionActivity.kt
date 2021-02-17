package jds.projects.ddcompanion.activities.ddsession

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.adapters.FixedSectionsPagerAdapter
import jds.projects.ddcompanion.my_classes.adapters.PlayerSheetsAdapter
import jds.projects.ddcompanion.my_classes.dialogs.DialogPlayerRequestReply
import jds.projects.ddcompanion.my_classes.dialogs.DialogThrowResult
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDConnection
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessage
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessageManager
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.WiFiCommunicator
import jds.projects.ddcompanion.my_classes.utils.JDUtils
import jds.projects.ddcompanion.my_classes.utils.WiFiActivity
import java.util.concurrent.CountDownLatch

class PlayerSessionActivity : WiFiActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var lltDevices: LinearLayout

    override fun manageRequest(connection: JDConnection, request: JDMessage): JDMessage {
        return when (request.requestID) {
            JDMessageManager.TYPE.REQUEST_GAME_MANUAL -> {
                JDMessageManager.replyToPlayerSessionManual(0)
            }
            JDMessageManager.TYPE.REQUEST_CONNECTION -> {
                JDMessageManager.replyToPlayerConnection(false)
            }
            JDMessageManager.TYPE.REQUEST_THROW_TC -> {
                return createDialogThrowTC(request)
            }

            JDMessageManager.TYPE.REQUEST_THROW_ABILITY -> {
                with(viewModel as PlayerSessionActivityViewModel) {
                    return createDialogThrowAbility(
                        request.apply { message = abilities[request.ability].name })
                }
            }

            JDMessageManager.TYPE.REQUEST_THROW_SAVING -> {
                return createDialogThrowSaving(request)
            }
            JDMessageManager.TYPE.REQUEST_THROW_CA -> {
                return createDialogThrowCA(request)
            }
            JDMessageManager.TYPE.REQUEST_LEVEL_UP -> {
                return createDialogLevelUp()
            }
            JDMessageManager.TYPE.REQUEST_DISCONNECTION -> {
                return createDialogDisconnection()
            }
            //messaggio nullo, rispondo con messaggio nullo
            else -> {
                JDMessage()
            }
        }
    }

    private fun createDialogDisconnection(): JDMessage {
        var answer = JDMessage()
        val latch = CountDownLatch(1)
        runOnUiThread {
            answer = JDMessageManager.replyDisconnection()
            (viewModel as PlayerSessionActivityViewModel).disconnectedFromMaster()
            Toast.makeText(this, getString(R.string.master_disconnected), Toast.LENGTH_SHORT).show()
            latch.countDown()
        }
        latch.await()
        return answer
    }

    private fun createDialogLevelUp(): JDMessage {
        var answer = JDMessage()
        val latch = CountDownLatch(1)
        runOnUiThread {
            answer = JDMessageManager.replyToMasterLevelUp(viewModel.myAddress)
            DialogThrowResult.createLevelUpDialog(
                this,
                (viewModel as PlayerSessionActivityViewModel).playerSheet!!
            ).show()
            (viewModel as PlayerSessionActivityViewModel).levelUp()
            latch.countDown()
        }
        latch.await()
        return answer
    }

    @Synchronized
    private fun createDialogThrowTC(request: JDMessage): JDMessage {
        var answer = JDMessage()
        val latch = CountDownLatch(1)
        runOnUiThread {
            //imposto il mio tc nella request
            request.punteggioBase =
                (viewModel as PlayerSessionActivityViewModel).playerSheet!!.bonusTC
            val dialog = DialogPlayerRequestReply(this, request).apply {
                setOnRequestListener(object : DialogPlayerRequestReply.RequestListener {
                    override fun onResultAvailable(
                        dice: Int, result: Int,
                        punteggioBase: Int, punteggioBonus: Int
                    ) {
                        answer = JDMessageManager.replyToMasterThrowTC(
                            viewModel.myAddress,
                            dice,
                            result,
                            punteggioBase,
                            punteggioBonus,
                            request.difficulty
                        )
                        DialogThrowResult.createHitThrowDialog(
                            this@PlayerSessionActivity,
                            null,
                            answer
                        )
                            .show()
                        latch.countDown()
                    }
                })
            }.create()
            dialog.show()
        }
        latch.await()
        return answer
    }

    @Synchronized
    private fun createDialogThrowCA(request: JDMessage): JDMessage {
        var answer = JDMessage()
        val latch = CountDownLatch(1)
        runOnUiThread {
            //imposto la mia CA nella request
            request.punteggioBase =
                (viewModel as PlayerSessionActivityViewModel).playerSheet!!.bonusCA
            val dialog = DialogPlayerRequestReply(this, request).apply {
                setOnRequestListener(object : DialogPlayerRequestReply.RequestListener {
                    override fun onResultAvailable(
                        dice: Int, result: Int,
                        punteggioBase: Int, punteggioBonus: Int
                    ) {
                        answer = JDMessageManager.replyToMasterThrowCA(
                            viewModel.myAddress,
                            dice,
                            result,
                            punteggioBase,
                            punteggioBonus,
                            request.difficulty
                        )
                        DialogThrowResult.createCAThrowDialog(
                            this@PlayerSessionActivity,
                            null,
                            answer
                        )
                            .show()
                        latch.countDown()
                    }
                })
            }.create()
            dialog.show()
        }
        latch.await()
        return answer
    }

    @Synchronized
    private fun createDialogThrowAbility(request: JDMessage): JDMessage {
        var answer = JDMessage()
        val latch = CountDownLatch(1)
        runOnUiThread {
            val dialog = DialogPlayerRequestReply(this, request).apply {
                setOnRequestListener(object : DialogPlayerRequestReply.RequestListener {
                    override fun onResultAvailable(
                        dice: Int, result: Int,
                        punteggioBase: Int, punteggioBonus: Int
                    ) {
                        answer = JDMessageManager.replyToMasterThrowAbility(
                            viewModel.myAddress,
                            request.ability,
                            dice,
                            result,
                            punteggioBase,
                            punteggioBonus,
                            request.difficulty
                        )
                        DialogThrowResult.createAbilityThrowDialog(
                            this@PlayerSessionActivity,
                            null,
                            answer,
                            viewModel.abilities[answer.ability]
                        ).show()
                        latch.countDown()
                    }
                })
            }.create()
            dialog.show()
        }
        latch.await()
        return answer
    }

    @Synchronized
    private fun createDialogThrowSaving(request: JDMessage): JDMessage {
        var answer = JDMessage()
        val latch = CountDownLatch(1)
        runOnUiThread {
            //imposto il mio tiro salvezza nella request
            when (request.savingType) {
                JDMessageManager.SAVES.FORTITUDE -> {
                    request.punteggioBase =
                        (viewModel as PlayerSessionActivityViewModel).playerSheet!!.salvezzaTempra
                }
                JDMessageManager.SAVES.REFLEXES -> {
                    request.punteggioBase =
                        (viewModel as PlayerSessionActivityViewModel).playerSheet!!.salvezzaRiflessi
                }
                JDMessageManager.SAVES.WILL -> {
                    request.punteggioBase =
                        (viewModel as PlayerSessionActivityViewModel).playerSheet!!.salvezzaVolonta
                }
            }
            val dialog = DialogPlayerRequestReply(this, request).apply {
                setOnRequestListener(object : DialogPlayerRequestReply.RequestListener {
                    override fun onResultAvailable(
                        dice: Int, result: Int,
                        punteggioBase: Int, punteggioBonus: Int
                    ) {
                        answer = JDMessageManager.replyToMasterThrowSaving(
                            viewModel.myAddress,
                            request.savingType,
                            dice,
                            result,
                            punteggioBase,
                            punteggioBonus,
                            request.difficulty
                        )
                        DialogThrowResult.createSavingThrowDialog(
                            this@PlayerSessionActivity,
                            null,
                            answer
                        ).show()
                        latch.countDown()
                    }
                })
            }.create()
            dialog.show()
        }
        latch.await()
        return answer
    }

    override fun manageAnswer(connection: JDConnection, request: JDMessage, answer: JDMessage) {
        when (answer.requestID) {
            JDMessageManager.TYPE.REQUEST_GAME_MANUAL -> {
                val manualID = answer.message.toInt()
                if (manualID > 0)
                    selectPlayerSheet(answer.message.toInt())
                else
                    runOnUiThread {
                        Toast.makeText(
                            baseContext,
                            getString(R.string.text_connection_refused),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            JDMessageManager.TYPE.REQUEST_CONNECTION -> {
                if (answer.message == JDMessageManager.VALUES.SUCCESS)
                    onGameSessionReady()
                else
                    runOnUiThread {
                        Toast.makeText(
                            baseContext,
                            getString(R.string.text_connection_refused),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    override fun onActivityReady() {
        //creo lista dei dispositivi visibili, e decido a chi collegarmi
        lltDevices = findViewById(R.id.llt_activity_player_session__devices)
        val edtIP = findViewById<EditText>(R.id.edt_activity_player_session__ip)
        val btnConnect = findViewById<Button>(R.id.btn_activity_player_session__connect)
        btnConnect.setOnClickListener {
            val address = edtIP.text.toString().trim()
            //se l'indirizzo è un ip valido
            if (isAValidIpAddress(address))
                requestGameManual(address)
            else
                Toast.makeText(
                    baseContext,
                    getString(R.string.toast_invalid_ip_address),
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    override fun onGameSessionReady() {
        (viewModel as PlayerSessionActivityViewModel).connectedToMaster()
        runOnUiThread {
            //PULISCO LE VECCHIE VIEW E MOSTRO LE VIEW DELLA SESSIONE DI GIOCO
            //pulisco il layout e lo rendo GONE cosi da ridurre il peso dell'activity
            lltDevices.removeAllViews()
            lltDevices.visibility = View.GONE
            //rendo visibile invece il layout della sessione di gioco

            viewPager.adapter =
                FixedSectionsPagerAdapter(
                    this,
                    (viewModel as PlayerSessionActivityViewModel).getFragments()
                )

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = (viewModel as PlayerSessionActivityViewModel).getTabTitle(position)
            }.attach()
            viewPager.visibility = View.VISIBLE
            tabLayout.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_session)
        //inizializzo l'activity
        viewModel = PlayerSessionActivityViewModel(this)

        //IMPOSTO IL VIEWPAGER
        viewPager = findViewById(R.id.activity_player_session_pager)
        //SETTO IL NUMERO DI PAGINE FUORI DALLA VISIONE DELL'UTENTE
        //IN QUESTO MODO GIà INIZIA A CREARLE E LE TIENE SALVATE
        viewPager.offscreenPageLimit = PlayerSessionActivityViewModel.NUM_FRAGMENTS

        //IMPOSTO IL TAB LAYOUT E LO COLLEGO AL VIEWPAGER
        tabLayout = findViewById(R.id.activity_player_session_tabs)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                (viewModel as PlayerSessionActivityViewModel).updateActualPosition(position)
            }
        })
        viewPager.visibility = View.GONE
    }

    private fun isAValidIpAddress(address: String): Boolean {
        return Patterns.IP_ADDRESS.matcher(address).matches()
    }

    //COLLEGAMENTO METODI ESEGUITI IN ORDINE
    //MI COLLEGO E RICHIEDO MANUALE DI GIOCO
    private fun requestGameManual(address: String) {
        //imposto il device del master di sessione
        (viewModel as PlayerSessionActivityViewModel).setSessionMasterDeviceIP(address)
        (viewModel as PlayerSessionActivityViewModel).masterDeviceIP?.let {
            //creo messaggio
            val progressBar =
                lltDevices.findViewById<ProgressBar>(R.id.pbar_activity_player_session__connection)
            val btnConnect = findViewById<Button>(R.id.btn_activity_player_session__connect)
            btnConnect.isClickable = false
            btnConnect.isFocusable = false
            btnConnect.isEnabled = false
            progressBar.visibility = View.VISIBLE
            val requestMessage = JDMessageManager.requestToMasterSessionManual(viewModel.myAddress)
            val communicator = WiFiCommunicator(it).apply {
                setOnWiFiCommunicatorListener(object : WiFiCommunicator.WiFiCommunicatorListener {
                    override fun onAnswerReceived(
                        connection: JDConnection,
                        request: JDMessage,
                        answer: JDMessage
                    ) {
                        if (request.requestID == answer.requestID)
                            manageAnswer(connection, request, answer)
                    }

                    override fun onConnectionCancelled() {
                        runOnUiThread {
                            Toast.makeText(
                                baseContext,
                                getString(R.string.error_lost_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                            progressBar.visibility = View.GONE
                            btnConnect.isClickable = true
                            btnConnect.isFocusable = true
                            btnConnect.isEnabled = true
                        }
                    }

                    override fun onConnectionClosed() {
                        runOnUiThread {
                            progressBar.visibility = View.GONE
                            btnConnect.isClickable = true
                            btnConnect.isFocusable = true
                            btnConnect.isEnabled = true
                        }
                    }
                })
            }
            communicator.sendRequest(requestMessage)
        }
    }

    //RICEVUTO MANUALE DI GIOCO SCELGO IL PLAYER SHEET E MI COLLEGO ALLA SESSIONE
    private fun selectPlayerSheet(manualID: Int) {
        runOnUiThread {
            with(viewModel as PlayerSessionActivityViewModel) {
                setManualID(manualID)
                //DISATTIVO VIEW DI COLLEGAMENTO AL MASTER
                findViewById<LinearLayout>(R.id.llt_activity_player_session_devices__connection).visibility =
                    View.GONE
                //ATTIVO VIEW DI SCELTA PLAYER SHEETS
                lltDevices.findViewById<LinearLayout>(R.id.llt_activity_player_session_devices__playersheets).visibility =
                    View.VISIBLE
                //INIT DEL RECYCLER VIEW
                val recyclerView =
                    lltDevices.findViewById<RecyclerView>(R.id.recyclerview_activity_player_session__playersheets)
                recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                //ottimizzazioni per la performance del recycler view
                recyclerView.setRecycledViewPool(RecyclerView.RecycledViewPool())
                recyclerView.setItemViewCacheSize(20)
                recyclerView.setHasFixedSize(true)
                //fine ottimizzazioni
                val adapter = createAdapter(manualID)
                adapter.setOnCardClickListener(object : PlayerSheetsAdapter.OnCardClickListener {
                    override fun onCardClicked(position: Int) {
                        setPlayerSheet(position)
                        val request = JDMessageManager.requestToMasterConnection(
                            viewModel.myAddress,
                            playerSheet!!.playerName
                        )
                        val communicator = WiFiCommunicator(masterDeviceIP!!).apply {
                            setOnWiFiCommunicatorListener(object :
                                WiFiCommunicator.WiFiCommunicatorListener {
                                override fun onAnswerReceived(
                                    connection: JDConnection,
                                    request: JDMessage,
                                    answer: JDMessage
                                ) {
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
                        communicator.sendRequest(request)
                    }

                    override fun onCardLongClicked(position: Int): Boolean {
                        return true
                    }
                })
                recyclerView.adapter = adapter
            }
        }
    }

    private fun sendDisconnectionToMaster() {
        with(viewModel as PlayerSessionActivityViewModel) {
            if (connected) {
                disconnectedFromMaster()
                val request = JDMessageManager.requestDisconnection(myAddress)
                val communicator = WiFiCommunicator(masterDeviceIP!!).apply {
                    setOnWiFiCommunicatorListener(object :
                        WiFiCommunicator.WiFiCommunicatorListener {
                        override fun onAnswerReceived(
                            connection: JDConnection,
                            request: JDMessage, answer: JDMessage
                        ) {
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
                communicator.sendRequest(request)
            }
        }
    }

    private fun setActivityResult(flagLevelUp: Int) {
        val intent = Intent()
        intent.putExtra(JDUtils.BUNDLES.LEVEL_UP, flagLevelUp)
        val playerSheet = (viewModel as PlayerSessionActivityViewModel).playerSheet
        if (playerSheet != null)
            intent.putExtra(JDUtils.BUNDLES.PLAYER_SHEET_ID, playerSheet.playerSheetID)
        setResult(RESULT_OK, intent)
    }

    override fun onBackPressed() {
        setActivityResult((viewModel as PlayerSessionActivityViewModel).flagLevelUp)
        sendDisconnectionToMaster()
        super.onBackPressed()
    }

    override fun onDestroy() {
        sendDisconnectionToMaster()
        setActivityResult((viewModel as PlayerSessionActivityViewModel).flagLevelUp)
        super.onDestroy()
    }
}
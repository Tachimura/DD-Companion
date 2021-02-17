package jds.projects.ddcompanion.activities.ddsession

import androidx.fragment.app.Fragment
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.activities.ddsession.playerfragments.PlayerSessionInformationsFragment
import jds.projects.ddcompanion.activities.ddsession.playerfragments.PlayerSessionRequestsFragment
import jds.projects.ddcompanion.my_classes.adapters.PlayerSheetsAdapter
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDConnection
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessage
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessageManager
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.WiFiCommunicator
import jds.projects.ddcompanion.my_classes.utils.JDPreferences
import jds.projects.ddcompanion.my_classes.utils.WiFiViewModel

class PlayerSessionActivityViewModel(activity: PlayerSessionActivity) : WiFiViewModel(activity) {
    companion object {
        const val POSITION_FRAGMENT_INFORMATIONS: Int = 0
        const val POSITION_FRAGMENT_REQUESTS: Int = 1
        const val NUM_FRAGMENTS: Int = 2
    }

    private var actualPosition: Int
    private var fragments: Array<Fragment>? = null
    private var tabsTitles: Array<String>
    private var dbHelper: DBInterface

    private var _flagLevelUp = 0
    val flagLevelUp: Int
        get() = _flagLevelUp

    private var _masterDeviceIP: String? = null
    val masterDeviceIP: String?
        get() = _masterDeviceIP

    private var mPlayerSheet: PlayerSheet? = null
    val playerSheet: PlayerSheet?
        get() = mPlayerSheet

    private lateinit var playerSheets: MutableList<PlayerSheet>

    private var mConnected = false
    val connected: Boolean
        get() {
            synchronized(mConnected) {
                return mConnected
            }
        }

    init {
        //inizializzo i 3 fragments da mostrare a schermo
        actualPosition = 0
        tabsTitles = arrayOf(
            activity.resources.getString(R.string.title_fragment_player_session_informations),
            activity.resources.getString(R.string.title_fragment_player_session_requests)
        )
        dbHelper = DBInterface.getInstance(activity)
    }

    fun getFragments(): Array<Fragment> {
        if (fragments == null) {
            val requestsFragment = PlayerSessionRequestsFragment(activity, playerSheet!!)
            requestsFragment.setOnPlayerSessionRequestsListener(object :
                PlayerSessionRequestsFragment.PlayerSessionRequestsListener {
                override fun onRequestTC(value: Int, bonus: Int): Boolean {
                    if (!connected)
                        return false
                    val requestMessage =
                        JDMessageManager.requestToMasterThrowTC(myAddress, value, bonus)
                    val communicator = WiFiCommunicator(masterDeviceIP!!).apply {
                        setOnWiFiCommunicatorListener(object :
                            WiFiCommunicator.WiFiCommunicatorListener {
                            override fun onAnswerReceived(
                                connection: JDConnection,
                                request: JDMessage,
                                answer: JDMessage
                            ) {
                                if (request.requestID == answer.requestID)
                                    (fragments!![POSITION_FRAGMENT_REQUESTS] as PlayerSessionRequestsFragment).resultRequest(
                                        request, answer
                                    )
                            }

                            override fun onConnectionCancelled() {
                                (fragments!![POSITION_FRAGMENT_REQUESTS] as PlayerSessionRequestsFragment).errorConnection()
                            }

                            override fun onConnectionClosed() {}
                        })
                    }
                    communicator.sendRequest(requestMessage)
                    return true
                }

                override fun onRequestAbility(id: Int, value: Int, bonus: Int): Boolean {
                    if (!connected)
                        return false
                    val requestMessage =
                        JDMessageManager.requestToMasterThrowAbility(myAddress, id, value, bonus)
                    val communicator = WiFiCommunicator(masterDeviceIP!!).apply {
                        setOnWiFiCommunicatorListener(object :
                            WiFiCommunicator.WiFiCommunicatorListener {
                            override fun onAnswerReceived(
                                connection: JDConnection,
                                request: JDMessage,
                                answer: JDMessage
                            ) {
                                if (request.requestID == answer.requestID)
                                    (fragments!![POSITION_FRAGMENT_REQUESTS] as PlayerSessionRequestsFragment).resultRequest(
                                        request,
                                        answer
                                    )
                            }

                            override fun onConnectionCancelled() {
                                (fragments!![POSITION_FRAGMENT_REQUESTS] as PlayerSessionRequestsFragment).errorConnection()
                            }

                            override fun onConnectionClosed() {}
                        })
                    }
                    communicator.sendRequest(requestMessage)
                    return true
                }
            })

            fragments = arrayOf(
                PlayerSessionInformationsFragment(
                    activity,
                    masterDeviceIP!!,
                    activity.resources.getString(R.string.game_manual_title) + " " + dbHelper.getGameManualBaseInfo(
                        manualID
                    ).manualVersion,
                    playerSheet!!
                ),
                requestsFragment
            )
        }
        return fragments!!
    }

    fun getTabTitle(position: Int): String {
        return tabsTitles[position]
    }

    fun updateActualPosition(position: Int) {
        if (position in 0 until NUM_FRAGMENTS)
            actualPosition = position
    }

    fun setSessionMasterDeviceIP(deviceIP: String) {
        this._masterDeviceIP = deviceIP
    }

    fun createAdapter(manualID: Int): PlayerSheetsAdapter {
        playerSheets = dbHelper.getPlayerSheetsByGameManualID(manualID).toMutableList()
        return PlayerSheetsAdapter(
            playerSheets,
            JDPreferences.getInstance(activity).language.id
        )
    }

    fun setPlayerSheet(position: Int) {
        mPlayerSheet = playerSheets[position]
    }

    fun setManualID(manualID: Int) {
        mManualID = manualID
    }

    override fun changeWiFiStatusText(status: Boolean) {
        fragments?.run {
            (get(POSITION_FRAGMENT_INFORMATIONS) as PlayerSessionInformationsFragment)
                .changeWiFiStatusText(status)
        }
    }

    override fun changedAddress() {}

    fun levelUp() {
        _flagLevelUp = 1
        dbHelper.playerCanLevelUp(playerSheet!!)
    }

    fun connectedToMaster() {
        synchronized(mConnected) {
            mConnected = true
        }
    }

    fun disconnectedFromMaster() {
        synchronized(mConnected) {
            mConnected = false
        }
    }
}
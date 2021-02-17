package jds.projects.ddcompanion.activities.ddsession

import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.activities.ddsession.masterfragment.MasterSessionFragment
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GameManual
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessage
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessageManager
import jds.projects.ddcompanion.my_classes.utils.WiFiViewModel

class MasterSessionActivityViewModel(activity: MasterSessionActivity, manualID: Int) :
    WiFiViewModel(activity) {
    val connectedPlayers: Array<String>
        get() = mFragment.getConnectedPlayers()
    private var _gameManual: GameManual
    val manual: GameManual
        get() = _gameManual

    private var mFragment: MasterSessionFragment
    val fragment: MasterSessionFragment
        get() = mFragment

    init {
        mManualID = manualID
        _gameManual = DBInterface.getInstance(activity).getGameManualBaseInfo(mManualID)
        //inizializzo il fragment da mostrare a schermo
        mFragment = MasterSessionFragment(
            ipAddress,
            "${activity.resources.getString(R.string.game_manual_title)} ${_gameManual.manualVersion}"
        )
        mFragment.setOnMasterRequestListener(object : MasterSessionFragment.MasterRequestListener {
            override fun onRequest(request: JDMessage, address: String) {
                activity.sendRequest(request, address)
            }
        })
    }

    fun addConnection(address: String, name: String) {
        mFragment.addConnection(address, name)
    }

    fun removeConnection(address: String) {
        mFragment.removeConnection(address)
    }

    fun getPlayerByAddress(address: String): String {
        return fragment.getPlayerByAddress(address)
    }

    override fun changeWiFiStatusText(status: Boolean) {
        fragment.changeWiFiStatusText(status)
    }

    override fun changedAddress() {
        fragment.updateMasterAddress(ipAddress)
    }

    fun reportAnswer(address: String, answer: JDMessage) {
        if (answer.requestID == JDMessageManager.TYPE.REQUEST_THROW_ABILITY)
            mFragment.receiveAnswer(address, answer, abilities[answer.ability])
        else
            mFragment.receiveAnswer(address, answer, null)
    }
}
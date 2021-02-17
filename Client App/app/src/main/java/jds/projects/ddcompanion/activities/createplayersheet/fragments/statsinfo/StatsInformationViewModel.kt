package jds.projects.ddcompanion.activities.createplayersheet.fragments.statsinfo

import android.content.Context
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.CARISMA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.COSTITUZIONE
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.DESTREZZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.FORZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.INTELLIGENZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.SAGGEZZA
import jds.projects.ddcompanion.my_classes.utils.JDViewModel

class StatsInformationViewModel(context: Context) : JDViewModel(context) {
    private var selectedManualID: Int = -1
    private lateinit var _stats: PlayerStats
    val stats: PlayerStats
        get() = _stats
    private lateinit var minStats: PlayerStats

    private var _useablePoints: Int = 0
    val useablePoints: Int
        get() = _useablePoints

    fun setUp(minStats: PlayerStats, manualID: Int, useablePoints: Int) {
        selectedManualID = manualID
        _useablePoints = useablePoints
        this.minStats = minStats
        val str = minStats.stats.getValue(FORZA)
        val dex = minStats.stats.getValue(DESTREZZA)
        val cos = minStats.stats.getValue(COSTITUZIONE)
        val int = minStats.stats.getValue(INTELLIGENZA)
        val sag = minStats.stats.getValue(SAGGEZZA)
        val car = minStats.stats.getValue(CARISMA)
        _stats = PlayerStats(str, dex, cos, int, sag, car)
    }

    fun reduce(stat: Int): Boolean {
        if (canReduce(stat)) {
            _stats.updateStat(stat, _stats.stats.getValue(stat) - 1)
            _useablePoints += 1
            return true
        }
        return false
    }

    fun canReduce(stat: Int): Boolean {
        return _stats.stats.getValue(stat) > minStats.stats.getValue(stat)
    }

    fun increase(stat: Int): Boolean {
        if (useablePoints > 0) {
            _useablePoints -= 1
            _stats.updateStat(stat, _stats.stats.getValue(stat) + 1)
            return true
        }
        return false
    }
}
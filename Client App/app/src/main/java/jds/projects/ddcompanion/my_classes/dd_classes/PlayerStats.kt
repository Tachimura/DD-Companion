package jds.projects.ddcompanion.my_classes.dd_classes

import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.CARISMA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.COSTITUZIONE
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.DESTREZZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.FORZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.INTELLIGENZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.SAGGEZZA
import kotlin.math.floor

class PlayerStats() {

    object PlayerStat {
        const val FORZA = 0
        const val DESTREZZA = 1
        const val COSTITUZIONE = 2
        const val INTELLIGENZA = 3
        const val SAGGEZZA = 4
        const val CARISMA = 5
    }

    private var _stats = mutableMapOf(
        Pair(FORZA, 0),
        Pair(DESTREZZA, 0),
        Pair(COSTITUZIONE, 0),
        Pair(INTELLIGENZA, 0),
        Pair(SAGGEZZA, 0),
        Pair(CARISMA, 0)
    )

    val stats: Map<Int, Int>
        get() = _stats

    constructor(str: Int, dex: Int, cos: Int, int: Int, sag: Int, car: Int) : this() {
        _stats[FORZA] = str
        _stats[DESTREZZA] = dex
        _stats[COSTITUZIONE] = cos
        _stats[INTELLIGENZA] = int
        _stats[SAGGEZZA] = sag
        _stats[CARISMA] = car
    }

    fun updateStat(stat: Int, newValue: Int) {
        _stats[stat] = newValue
    }

    fun statModifier(stat: Int): Int {
        return floor((_stats[stat]!!.toFloat() - 10) / 2).toInt()
    }
}
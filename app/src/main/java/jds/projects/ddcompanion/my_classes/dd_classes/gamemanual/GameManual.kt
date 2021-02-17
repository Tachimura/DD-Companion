package jds.projects.ddcompanion.my_classes.dd_classes.gamemanual

import jds.projects.ddcompanion.my_classes.dd_classes.DDClass
import jds.projects.ddcompanion.my_classes.dd_classes.DDRace
import jds.projects.ddcompanion.my_classes.dd_classes.DDTalent
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats
import jds.projects.ddcompanion.my_classes.utils.MLanguageText

class GameManual(
    private var _manualID: Int,
    private var _manualVersion: Float,
    private var _manualDate: String
) {
    private var _manualUsablePoints: Int = 0
    private lateinit var _manualBaseStats: PlayerStats
    private lateinit var _manualNotes: MLanguageText

    private var _manualRaces: Array<DDRace>
    private var _manualClasses: Array<DDClass>
    private var _manualTalents: Array<DDTalent>
    private var _canLoad = true

    //id manuale
    val manualID: Int
        get() = _manualID

    //versione manuale
    val manualVersion: Float
        get() = _manualVersion

    //data versione del manuale
    val manualDate: String
        get() = _manualDate

    val manualUsablePoints: Int
        get() = _manualUsablePoints

    val manualBaseStats: PlayerStats
        get() = _manualBaseStats

    val manualNotes: MLanguageText
        get() = _manualNotes

    val availableRaces: Array<DDRace>
        get() = _manualRaces

    val availableClasses: Array<DDClass>
        get() = _manualClasses

    val availableTalents: Array<DDTalent>
        get() = _manualTalents

    val canLoad: Boolean
        get() = _canLoad

    init {
        _manualClasses = arrayOf()
        _manualRaces = arrayOf()
        _manualTalents = arrayOf()
    }

    constructor(
        manualID: Int,
        manualVersion: Float,
        manualDate: String,
        manualUsablePoints: Int,
        manualBaseStats: PlayerStats,
        manualNotes: MLanguageText
    ) : this(manualID, manualVersion, manualDate) {
        _manualNotes = manualNotes
        _manualUsablePoints = manualUsablePoints
        _manualBaseStats = manualBaseStats
    }

    constructor(
        manualID: Int,
        manualVersion: Float,
        manualDate: String,
        manualUsablePoints: Int,
        manualBaseStats: PlayerStats,
        manualNotes: MLanguageText,
        races: Array<DDRace>,
        classes: Array<DDClass>,
        talents: Array<DDTalent>
    ) :
            this(
                manualID,
                manualVersion,
                manualDate,
                manualUsablePoints,
                manualBaseStats,
                manualNotes
            ) {
        lateLoad(races, classes, talents)
    }

    fun lateLoad(
        manualRaces: Array<DDRace>,
        manualClasses: Array<DDClass>,
        manualTalents: Array<DDTalent>
    ) {
        if (canLoad) {
            _manualRaces = manualRaces
            _manualClasses = manualClasses
            _manualTalents = manualTalents
            _canLoad = false
        }
    }
}
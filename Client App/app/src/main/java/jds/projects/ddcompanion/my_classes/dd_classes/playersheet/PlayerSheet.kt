package jds.projects.ddcompanion.my_classes.dd_classes.playersheet

import jds.projects.ddcompanion.my_classes.dd_classes.*
import kotlin.math.floor

class PlayerSheet() {
    val bonusTC: Int
        get() {
            var tc = 0
            for (talent in _talents) {
                tc += talent.modTC
            }
            return tc
        }

    val bonusCA: Int
        get() {
            var ca = 0
            for (talent in _talents) {
                ca += talent.modCA
            }
            return ca
        }

    val salvezzaTempra: Int
        get() {
            var tempra = 0
            for (talent in _talents) {
                tempra += talent.salvTempra
            }
            return tempra
        }

    val salvezzaRiflessi: Int
        get() {
            var riflessi = 0
            for (talent in _talents) {
                riflessi += talent.salvRiflessi
            }
            return riflessi
        }

    val salvezzaVolonta: Int
        get() {
            var volonta = 0
            for (talent in _talents) {
                volonta += talent.salvVolonta
            }
            return volonta
        }

    private var _playerSheetID: Int = 0
    val playerSheetID: Int
        get() = _playerSheetID

    private var _manualID: Int = 0
    val manualID: Int
        get() = _manualID

    private var _manualVersion: Float = 0.0F
    val manualVersion: Float
        get() = _manualVersion

    private var _name: String
    val playerName: String
        get() = _name

    private var _ddRace: DDRace
    val playerRace: DDRace
        get() = _ddRace

    private var _playerClasses: PlayerDDClass
    val playerClasses: PlayerDDClass
        get() = _playerClasses

    private var _playerMainClass: DDClass
    val playerMainClass: DDClass
        get() = _playerMainClass

    private var _alignmentID: Int = 0
    val playerAlignmentID: Int
        get() = _alignmentID

    private var _level: Int = 0
    val playerLevel: Int
        get() = _level

    private var _playerStats: PlayerStats
    val playerStats: PlayerStats
        get() = _playerStats

    private var _abilities: MutableList<DDAbility>
    val playerAbilities: MutableList<DDAbility>
        get() = _abilities

    private var _talents: MutableList<DDTalent>
    val playerTalents: MutableList<DDTalent>
        get() = _talents

    private var _levelUpFlag: Int = 0
    val levelUpFlag: Int
        get() = _levelUpFlag
    val canLevelUp: Boolean
        get() = _levelUpFlag == 1


    private var _hp: Int = 0
    val playerHP: Int
        get() = _hp

    //funzioni statiche di classe
    companion object {
        fun statModifier(value: Int): String {
            return " (${statToString(
                floor((value.toFloat() - 10) / 2).toInt()
            )})"
        }

        fun statToString(value: Int): String {
            return if (value > 0)
                "+$value"
            else
                value.toString()
        }
    }

    init {
        _name = ""
        _ddRace =
            DDRace(-1, false)
        _playerMainClass =
            DDClass(-1, false)
        _playerClasses =
            PlayerDDClass()
        _playerStats =
            PlayerStats()
        _abilities = mutableListOf()
        _talents = mutableListOf()
    }

    constructor(
        manualID: Int,
        manualVersion: Float,
        name: String,
        alignment: Int,
        level: Int,
        hp: Int,
        playerBaseStats: PlayerStats,
        ddRace: DDRace,
        ddClasses: PlayerDDClass,
        mainClass: DDClass,
        abilities: MutableList<DDAbility>,
        talents: MutableList<DDTalent>,
        levelUpFlag: Int
    ) : this() {
        this._manualID = manualID
        this._manualVersion = manualVersion
        this._name = name
        this._alignmentID = alignment
        this._level = level
        this._hp = hp
        this._playerStats = playerBaseStats
        this._ddRace = ddRace
        this._playerClasses = ddClasses
        this._playerMainClass = mainClass
        this._abilities = abilities
        this._talents = talents
        this._levelUpFlag = levelUpFlag
    }

    constructor(
        playerSheetID: Int,
        manualID: Int,
        manualVersion: Float,
        name: String,
        alignment: Int,
        level: Int,
        hp: Int,
        playerBaseStats: PlayerStats,
        ddRace: DDRace,
        ddClasses: PlayerDDClass,
        mainClass: DDClass,
        abilities: MutableList<DDAbility>,
        talents: MutableList<DDTalent>,
        levelUpFlag: Int
    ) :
            this(
                manualID,
                manualVersion,
                name,
                alignment,
                level,
                hp,
                playerBaseStats,
                ddRace,
                ddClasses,
                mainClass,
                abilities,
                talents,
                levelUpFlag
            ) {
        lateLoad(playerSheetID)
    }

    fun lateLoad(playerSheetID: Int) {
        this._playerSheetID = playerSheetID
    }

    fun updatePlayerSheet(
        newHp: Int,
        newPlayerClass: DDClass,
        newPlayerStats: PlayerStats,
        newPlayerAbilities: MutableList<DDAbility>,
        newPlayerTalents: MutableList<DDTalent>
    ) {
        _levelUpFlag = 0
        _level += 1
        _hp = newHp
        if (!playerClasses.levelUp(newPlayerClass)) {
            playerClasses.load(newPlayerClass, 1)
        }
        _playerMainClass = newPlayerClass
        for (stat in newPlayerStats.stats)
            playerStats.updateStat(stat.key, stat.value)
        for (cont in 0 until newPlayerAbilities.size)
            playerAbilities[cont].updateLevel(newPlayerAbilities[cont].level)
        for (talent in newPlayerTalents)
            playerTalents.add(talent)
    }

    fun updatePlayerSheet(newSheet: PlayerSheet) {
        _level = newSheet.playerLevel
        _hp = newSheet.playerHP
        _playerClasses = newSheet.playerClasses
        _playerMainClass = newSheet.playerMainClass
        _playerStats = newSheet.playerStats
        _abilities = newSheet._abilities
        _talents = newSheet.playerTalents
        _levelUpFlag = newSheet.levelUpFlag
    }
}
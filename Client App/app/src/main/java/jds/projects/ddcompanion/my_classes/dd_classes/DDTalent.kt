package jds.projects.ddcompanion.my_classes.dd_classes

import jds.projects.ddcompanion.my_classes.utils.MLanguageText

class DDTalent(private var _id: Int, private var _is_new: Boolean) {
    private var _nameID: Int = 0
    private var _talentMinLvl: Int = 0
    private var _names: MLanguageText =
        MLanguageText()
    private var _descriptions: MLanguageText =
        MLanguageText()
    private var _stats: PlayerStats = PlayerStats()
    private var _talentSalTem: Int = 0
    private var _talentSalRif: Int = 0
    private var _talentSalVol: Int = 0
    private var _talentTC: Int = 0
    private var _talentCA: Int = 0

    val id: Int
        get() = _id

    val names: MLanguageText
        get() = _names

    val descriptions: MLanguageText
        get() = _descriptions

    val nameID: Int
        get() = _nameID

    val minLevel: Int
        get() = _talentMinLvl

    //modificatori di statistiche di razza
    val stats: PlayerStats
        get() = _stats

    val isNew: Boolean
        get() = _is_new

    val salvTempra: Int
        get() = _talentSalTem

    val salvRiflessi: Int
        get() = _talentSalRif

    val salvVolonta: Int
        get() = _talentSalVol

    val modTC: Int
        get() = _talentTC

    val modCA: Int
        get() = _talentCA

    constructor(
        _id: Int,
        _is_new: Boolean,
        _talentIDName: Int,
        _talentMinLvl: Int,
        _talentNames: MLanguageText,
        _talentDescriptions: MLanguageText,
        _playerStats: PlayerStats,
        _talentSalTem: Int,
        _talentSalRif: Int,
        _talentSalVol: Int,
        _talentTC: Int,
        _talentCA: Int
    ) : this(_id, _is_new) {
        this._nameID = _talentIDName
        this._talentMinLvl = _talentMinLvl
        this._names = _talentNames
        this._descriptions = _talentDescriptions
        this._stats = _playerStats
        this._talentSalTem = _talentSalTem
        this._talentSalRif = _talentSalRif
        this._talentSalVol = _talentSalVol
        this._talentTC = _talentTC
        this._talentCA = _talentCA
    }
}
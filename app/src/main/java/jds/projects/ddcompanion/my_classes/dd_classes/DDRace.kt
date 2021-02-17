package jds.projects.ddcompanion.my_classes.dd_classes

import jds.projects.ddcompanion.my_classes.utils.MLanguageText


class DDRace(private var _id: Int, private var _is_new: Boolean) {
    private var _names: MLanguageText =
        MLanguageText()
    private var _nameID: Int = 0
    private var _stats = PlayerStats()

    val id: Int
        get() = _id

    val names: MLanguageText
        get() = _names

    val nameID: Int
        get() = _nameID

    //modificatori di statistiche di razza
    val stats: PlayerStats
        get() = _stats

    val isNew: Boolean
        get() = _is_new

    constructor(
        _id: Int,
        _is_new: Boolean,
        _name_id: Int,
        _names: MLanguageText,
        _race_stats: PlayerStats
    ) : this(_id, _is_new) {
        this._nameID = _name_id
        this._stats = _race_stats
        this._names = _names
    }
}
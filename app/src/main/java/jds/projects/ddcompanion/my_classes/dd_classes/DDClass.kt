package jds.projects.ddcompanion.my_classes.dd_classes

import jds.projects.ddcompanion.my_classes.utils.MLanguageText

class DDClass(private var _id: Int, private var _is_new: Boolean) {

    private var _names: MLanguageText =
        MLanguageText()
    private var _nameID: Int = 0
    private var _allowedAlignments = DDAlignments(0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var _hpDice: Int = 0
    private var _hpDiceSucc: Int = 0
    private var _mainModifier: Int = 0
    private var _modSalv1: Int = 0
    private var _modSalv2: Int = 0
    private var _abilityPoints: Int = 0

    val id: Int
        get() = _id

    val nameID: Int
        get() = _nameID

    val names: MLanguageText
        get() = _names

    val hpDice: Int
        get() = _hpDice

    val hpDiceSucc: Int
        get() = _hpDiceSucc

    val mainModifier: Int
        get() = _mainModifier

    val modifierSalv1: Int
        get() = _modSalv1

    val modifierSalv2: Int
        get() = _modSalv2

    val allowedAlignments: DDAlignments
        get() = _allowedAlignments

    val isNew: Boolean
        get() = _is_new

    val abilityPoints: Int
        get() = _abilityPoints

    constructor(
        _id: Int,
        _is_new: Boolean,
        _name_id: Int,
        _names: MLanguageText,
        hpDice: Int,
        hpDiceSucc: Int,
        mainModifier: Int,
        modSalv1: Int,
        modSalv2: Int,
        abilityPoints: Int,
        _allowed_alignments: DDAlignments
    ) : this(_id, _is_new) {
        this._nameID = _name_id
        this._names = _names
        this._hpDice = hpDice
        this._hpDiceSucc = hpDiceSucc
        this._mainModifier = mainModifier
        this._modSalv1 = modSalv1
        this._modSalv2 = modSalv2
        this._abilityPoints = abilityPoints
        this._allowedAlignments = _allowed_alignments
    }
}
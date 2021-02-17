package jds.projects.ddcompanion.my_classes.cards

import android.widget.TextView
import androidx.cardview.widget.CardView
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.dd_classes.DDAbility


class AbilityCardView(root: CardView) {
    private var txvTitle: TextView = root.findViewById(R.id.txv_ability_adapter_cardview__title)
    private var btnMinus: TextView = root.findViewById(R.id.btn_ability_adapter_cardview__minus)
    private var btnPlus: TextView = root.findViewById(R.id.btn_ability_adapter_cardview__plus)
    private var _txvAbilityValue: TextView =
        root.findViewById(R.id.txv_ability_adapter_cardview__value)
    private var listener: OnAbilityCardViewListener? = null

    private lateinit var ddAbility: DDAbility

    private var _abilityMinLevel: Int = 0
    val abilityMinLevel: Int
        get() = _abilityMinLevel


    private var _diClasse: Boolean = false
    val diClasse: Boolean
        get() = _diClasse

    interface OnAbilityCardViewListener {
        fun onButtonPlusClick()
        fun onButtonMinusClick()
        fun onCardTitleClick(abilityClicked: DDAbility)
    }

    init {
        //imposto i listener
        btnPlus.setOnClickListener {
            listener?.onButtonPlusClick()
        }
        btnMinus.setOnClickListener {
            listener?.onButtonMinusClick()
        }
        txvTitle.setOnClickListener {
            listener?.onCardTitleClick(ddAbility)
        }
    }

    fun setOnCardClickListener(listener: OnAbilityCardViewListener) {
        this.listener = listener
    }

    fun setUpCardValues(abilityMinLevel: Int, diClasse: Boolean) {
        this._diClasse = diClasse
        this._abilityMinLevel = abilityMinLevel
        if (diClasse)
            txvTitle.text = ("${ddAbility.name} (C)")
        else
            txvTitle.text = ddAbility.name
        updateView()
    }

    fun setUpCardAbility(ddAbility: DDAbility) {
        this.ddAbility = ddAbility
        txvTitle.text = ddAbility.name
        updateView()
    }

    fun updateView() {
        _txvAbilityValue.text = ddAbility.level.toString()
    }
}
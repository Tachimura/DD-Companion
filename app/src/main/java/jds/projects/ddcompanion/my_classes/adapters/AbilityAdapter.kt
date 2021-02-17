package jds.projects.ddcompanion.my_classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.cards.AbilityCardView
import jds.projects.ddcompanion.my_classes.dd_classes.DDAbility

/**
 * Adapter x recycler view che mostra i manuali di gioco nella home fragment
 */
class AbilityAdapter(
    private var abilities: MutableList<DDAbility>,
    private var useablePoints: Int,
    private var abilitiesMinLevel: MutableList<Int>,
    private var playerMod1: Int,
    private var playerMod2: Int
) :
    RecyclerView.Adapter<AbilityAdapter.AbilityAdapterViewHolder>() {
    private var listener: OnAbilityCardListener? = null

    //mio listener x il click sul + o - delle abilità
    interface OnAbilityCardListener {
        fun onAbilityValueChanged(abilityChanged: DDAbility, useablePoints: Int)
        fun onAbilityCardTitleClick(abilityClicked: DDAbility)
    }

    fun setOnAbilityCardListener(listener: OnAbilityCardListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbilityAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_ability_adapter, parent, false)
        val abilityHolder = AbilityAdapterViewHolder(itemView)
        //imposto gli eventi ai 2 click dei buttons
        abilityHolder.abilityCard.setOnCardClickListener(object :
            AbilityCardView.OnAbilityCardViewListener {
            override fun onButtonPlusClick() {
                //se è di classe devo togliere solo 1 punto
                if (abilityHolder.abilityCard.diClasse)
                    increaseAbilityPoint(abilityHolder, 1)
                //se non è di classe devo togliere 2 punti
                else
                    increaseAbilityPoint(abilityHolder, 2)
            }

            override fun onButtonMinusClick() {
                //se posso scendere
                if (abilityHolder.abilityCard.diClasse)
                    decreaseAbilityPoint(abilityHolder, 1)
                else
                    decreaseAbilityPoint(abilityHolder, 2)
            }

            override fun onCardTitleClick(abilityClicked: DDAbility) {
                listener?.onAbilityCardTitleClick(abilityClicked)
            }
        })
        return abilityHolder
    }

    private fun increaseAbilityPoint(abilityHolder: AbilityAdapterViewHolder, increase: Int) {
        if (useablePoints >= increase) {
            useablePoints -= increase
            updateInternalViews(
                abilityHolder.adapterPosition,
                abilities[abilityHolder.adapterPosition].level + 1,
                abilityHolder
            )
        }
    }

    private fun decreaseAbilityPoint(abilityHolder: AbilityAdapterViewHolder, decrease: Int) {
        val position = abilityHolder.adapterPosition
        val abilityMinlevel = abilityHolder.abilityCard.abilityMinLevel
        val abilityCurrentLevel = abilities[position].level
        if (abilityCurrentLevel > abilityMinlevel) {
            useablePoints += decrease
            updateInternalViews(position, abilityCurrentLevel - 1, abilityHolder)
        }
    }

    private fun updateInternalViews(
        position: Int,
        newLevel: Int,
        abilityHolder: AbilityAdapterViewHolder
    ) {
        abilities[position].updateLevel(newLevel)
        abilityHolder.abilityCard.updateView()
        listener?.onAbilityValueChanged(
            abilities[position],
            useablePoints
        )
    }

    override fun getItemCount(): Int {
        return abilities.size
    }

    override fun onBindViewHolder(holder: AbilityAdapterViewHolder, position: Int) {
        val ability = abilities[position]
        holder.abilityCard.setUpCardAbility(ability)
        holder.abilityCard.setUpCardValues(
            abilitiesMinLevel[position],
            (ability.modifier == playerMod1 || ability.modifier == playerMod2)
        )
    }

    fun updatePlayerData(useablePoints: Int, playerModifier1: Int, playerModifier2: Int) {
        this.useablePoints = useablePoints
        this.playerMod1 = playerModifier1
        this.playerMod2 = playerModifier2
    }

    class AbilityAdapterViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private var _abilityCard =
            AbilityCardView(itemView as CardView)
        val abilityCard: AbilityCardView
            get() = _abilityCard
    }
}
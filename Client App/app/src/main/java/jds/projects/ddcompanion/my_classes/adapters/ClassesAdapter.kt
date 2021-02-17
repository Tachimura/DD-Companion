package jds.projects.ddcompanion.my_classes.adapters

import android.content.Context
import android.view.View
import android.widget.TextView
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.dd_classes.DDClass
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats

class ClassesAdapter(context: Context, classes: MutableList<DDClass>, languageID: Int) :
    ExpandableCardAdapter(context, classes, languageID) {

    override fun onBindViewHolder(holder: ExpandableViewHolder, position: Int) {
        val ddClass = arrayElements[position] as DDClass
        val innerView = View.inflate(context, R.layout.view_class_info, null)
        //genero il layout dei modificatori di classe

        val txvClassHitDice =
            innerView.findViewById<TextView>(R.id.txv_view_class_full_infos__hitdice)
        val txvClassHitDiceSucc =
            innerView.findViewById<TextView>(R.id.txv_view_class_full_infos__hitdicenext)
        val txvClassMainModifier =
            innerView.findViewById<TextView>(R.id.txv_view_class_full_infos__primarymodifier)
        val txvClassSavingModifiers =
            innerView.findViewById<TextView>(R.id.txv_view_class_full_infos__savingmodifiers)
        val txvClassAbilityPoints =
            innerView.findViewById<TextView>(R.id.txv_view_class_full_infos__abilitypoints)
        //hit dice
        txvClassHitDice.text = ("d${ddClass.hpDice}")
        txvClassHitDiceSucc.text = ("d${ddClass.hpDiceSucc}")
        //main modifier
        txvClassMainModifier.text = modifierIDIntoText(ddClass.mainModifier)
        //saving modifiers
        txvClassSavingModifiers.text =
            ("${modifierIDIntoText(ddClass.modifierSalv1)} & ${modifierIDIntoText(ddClass.modifierSalv2)}")
        //ability points
        txvClassAbilityPoints.text =
            ("${ddClass.abilityPoints} + ${context.resources.getString(R.string.stat_intelligenza)}")

        holder.expandableCard.updateCard(ddClass.names.getTextWithLanguageID(languageID), innerView)
    }

    private fun modifierIDIntoText(modID: Int): String {
        return when (modID) {
            PlayerStats.PlayerStat.FORZA -> context.getString(R.string.stat_forza_full)
            PlayerStats.PlayerStat.DESTREZZA -> context.getString(R.string.stat_destrezza_full)
            PlayerStats.PlayerStat.COSTITUZIONE -> context.getString(R.string.stat_costituzione_full)
            PlayerStats.PlayerStat.INTELLIGENZA -> context.getString(R.string.stat_intelligenza_full)
            PlayerStats.PlayerStat.SAGGEZZA -> context.getString(R.string.stat_saggezza_full)
            PlayerStats.PlayerStat.CARISMA -> context.getString(R.string.stat_carisma_full)
            else -> ""
        }
    }
}
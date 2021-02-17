package jds.projects.ddcompanion.my_classes.adapters

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.dd_classes.DDTalent

class TalentsAdapter(context: Context, talents: MutableList<DDTalent>, languageID: Int) :
    ExpandableCardAdapter(context, talents, languageID) {

    override fun onBindViewHolder(holder: ExpandableViewHolder, position: Int) {
        val ddTalent = arrayElements[position] as DDTalent
        val innerView = View.inflate(context, R.layout.view_talent_info, null)
        //genero il layout dei modificatori di classe

        val txvTalentDescription =
            innerView.findViewById<TextView>(R.id.txv_view_talent_full_infos__talentdescription)
        txvTalentDescription.text = ddTalent.descriptions.getTextWithLanguageID(languageID)
        val modifierLayout =
            innerView.findViewById<LinearLayout>(R.id.llt_view_talent_full_infos__talentmodifiers)
        createStatModifierLayout(context, ddTalent.stats, modifierLayout)
        //controllo se ci sono i modificatori speciali di talento da mettere o meno
        var otherModifiersText = ""
        if (ddTalent.salvTempra != 0)
            otherModifiersText += otherModifierTextFun(
                ddTalent.salvTempra,
                context.getString(R.string.tiri_salvezza_tempra)
            )
        if (ddTalent.salvRiflessi != 0)
            otherModifiersText += otherModifierTextFun(
                ddTalent.salvRiflessi,
                context.getString(R.string.tiri_salvezza_riflessi)
            )
        if (ddTalent.salvVolonta != 0)
            otherModifiersText += otherModifierTextFun(
                ddTalent.salvVolonta,
                context.getString(R.string.tiri_salvezza_volonta)
            )
        if (ddTalent.modTC != 0)
            otherModifiersText += modifierValueToString(ddTalent.modTC) + context.getString(R.string.bonus_tc_ai) + "\n"
        if (ddTalent.modCA != 0)
            otherModifiersText += modifierValueToString(ddTalent.modCA) + context.getString(R.string.bonus_ca_at) + "\n"

        if (otherModifiersText.isNotEmpty() && otherModifiersText.isNotBlank()) {
            val txvOtherModifiers =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    TextView(context, null, 0, R.style.TextAppearance_AppCompat_Body2)
                else
                    TextView(context, null, R.style.TextAppearance_AppCompat_Body2)
            txvOtherModifiers.text =
                otherModifiersText.substring(0, otherModifiersText.length - 1)
            modifierLayout.addView(txvOtherModifiers)
        }

        holder.expandableCard.updateCard(
            ddTalent.names.getTextWithLanguageID(languageID),
            innerView
        )
    }

    private fun otherModifierTextFun(value: Int, description: String): String {
        return modifierValueToString(value) + description + "\n"
    }
}
package jds.projects.ddcompanion.my_classes.adapters

import android.content.Context
import android.view.View
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.dd_classes.DDRace

class RaceAdapter(context: Context, races: MutableList<DDRace>, languageID: Int) :
    ExpandableCardAdapter(context, races, languageID) {

    override fun onBindViewHolder(holder: ExpandableViewHolder, position: Int) {
        val ddRace = arrayElements[position] as DDRace
        val innerView = View.inflate(context, R.layout.view_race_info, null)
        //genero il layout dei modificatori di classe
        createStatModifierLayout(
            context,
            ddRace.stats,
            innerView.findViewById(R.id.llt_view_race_full_infos__racemodifiers)
        )

        holder.expandableCard.updateCard(ddRace.names.getTextWithLanguageID(languageID), innerView)
    }
}
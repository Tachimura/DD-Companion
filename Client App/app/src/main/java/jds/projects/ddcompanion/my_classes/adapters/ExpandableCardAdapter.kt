package jds.projects.ddcompanion.my_classes.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.cards.ExpandableCardView
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats

abstract class ExpandableCardAdapter(
    protected val context: Context,
    protected val arrayElements: MutableList<*>,
    protected val languageID: Int
) :
    RecyclerView.Adapter<ExpandableCardAdapter.ExpandableViewHolder>() {

    private var listener: OnAdapterCardClickListener? = null
    private var startExpanded: Boolean = false

    private var lastCardSelected: ExpandableViewHolder? = null

    //mio listener x il click sul view holder
    interface OnAdapterCardClickListener {
        fun onCardClick(position: Int)
        fun onCardDeactivated(position: Int)
    }

    constructor(
        context: Context,
        arrayElements: MutableList<*>,
        languageID: Int,
        startExpanded: Boolean
    ) : this(context, arrayElements, languageID) {
        this.startExpanded = startExpanded
    }

    fun setOnCardClickListener(listener: OnAdapterCardClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandableViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.cardview_expandable, parent, false)
        val viewHolder = ExpandableViewHolder(itemView, context, startExpanded)
        viewHolder.expandableCard.setOnCardClickListener(object :
            ExpandableCardView.OnCardClickListener {
            override fun onCardClick() {
                //rendo trasparente la vecchia selezione
                lastCardSelected?.expandableCard?.setFocus(false)
                if (lastCardSelected != null)
                    listener?.onCardDeactivated(lastCardSelected!!.adapterPosition)
                //attivo la nuova selezione
                lastCardSelected = viewHolder
                lastCardSelected?.expandableCard?.setFocus(true)
                listener?.onCardClick(viewHolder.adapterPosition)
            }
        })
        return viewHolder
    }

    override fun getItemCount(): Int {
        return arrayElements.size
    }

    fun resetSelection() {
        lastCardSelected?.expandableCard?.setFocus(false)
        lastCardSelected = null
    }

    class ExpandableViewHolder(itemView: View, context: Context, startExpanded: Boolean) :
        RecyclerView.ViewHolder(itemView) {
        private var _expandableCard =
            ExpandableCardView(
                itemView as CardView,
                context
            )
        val expandableCard: ExpandableCardView
            get() = _expandableCard

        init {
            _expandableCard.canExpand = true
            if (startExpanded)
                _expandableCard.expand()
            else
                _expandableCard.collapse()
        }
    }

    protected fun modifierValueToString(value: Int): String {
        return if (value > 0) "+$value " else " $value "
    }

    protected fun createStatModifierLayout(
        context: Context,
        stats: PlayerStats,
        containerLayout: LinearLayout
    ) {
        val txvStat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            TextView(context, null, 0, R.style.TextAppearance_AppCompat_Body2)
        else
            TextView(context, null, R.style.TextAppearance_AppCompat_Body2)
        var statString = ""
        for (stat in stats.stats) {
            if (stat.value != 0) {
                statString = when (stat.key) {
                    PlayerStats.PlayerStat.FORZA -> statString + (modifierValueToString(stat.value) + context.getString(
                        R.string.stat_forza_full
                    )) + "\n"
                    PlayerStats.PlayerStat.DESTREZZA -> statString + (modifierValueToString(stat.value) + context.getString(
                        R.string.stat_destrezza_full
                    )) + "\n"
                    PlayerStats.PlayerStat.COSTITUZIONE -> statString + (modifierValueToString(stat.value) + context.getString(
                        R.string.stat_costituzione_full
                    )) + "\n"
                    PlayerStats.PlayerStat.INTELLIGENZA -> statString + (modifierValueToString(stat.value) + context.getString(
                        R.string.stat_intelligenza_full
                    )) + "\n"
                    PlayerStats.PlayerStat.SAGGEZZA -> statString + (modifierValueToString(stat.value) + context.getString(
                        R.string.stat_saggezza_full
                    )) + "\n"
                    PlayerStats.PlayerStat.CARISMA -> statString + (modifierValueToString(stat.value) + context.getString(
                        R.string.stat_carisma_full
                    )) + "\n"
                    else -> ""
                }
            }
        }
        if (statString.isNotEmpty() && statString.isNotBlank()) {
            txvStat.text = statString.substring(0, statString.length - 1)
            containerLayout.addView(txvStat)
        }
    }
}
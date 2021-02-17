package jds.projects.ddcompanion.my_classes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.cards.GameManualCardView
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GameManual

/**
 * Adapter x recycler view che mostra i manuali di gioco nella home fragment
 */
class GameManualsAdapter(private var manuals: MutableList<GameManual>) :
    RecyclerView.Adapter<GameManualsAdapter.GameManualViewHolder>() {

    private var listener: OnAdapterCardClickListener? = null
    private var startExpanded: Boolean = false

    //mio listener x il click sul view holder
    interface OnAdapterCardClickListener {
        fun onCardClick(position: Int)
    }

    constructor(manuals: MutableList<GameManual>, startExpanded: Boolean) : this(manuals) {
        this.startExpanded = startExpanded
    }

    fun setOnCardClickListener(listener: OnAdapterCardClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameManualViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_expandable_simple_1, parent, false)
        val manualHolder = GameManualViewHolder(itemView, itemView.context, startExpanded)
        manualHolder.gameCard.setOnCardClickListener(object :
            GameManualCardView.OnCardClickListener {
            override fun onCardClick() {
                listener?.onCardClick(manualHolder.adapterPosition)
            }
        })
        return manualHolder
    }

    override fun getItemCount(): Int {
        return manuals.size
    }

    override fun onBindViewHolder(holder: GameManualViewHolder, position: Int) {
        holder.gameCard.updateCard(manuals[position])
    }

    class GameManualViewHolder(itemView: View, context: Context, startExpanded: Boolean) :
        RecyclerView.ViewHolder(itemView) {
        private var _gameCard =
            GameManualCardView(
                itemView as CardView,
                context
            )
        val gameCard: GameManualCardView
            get() = _gameCard

        init {
            _gameCard.canExpand = true
            if (startExpanded)
                _gameCard.expand()
            else
                _gameCard.collapse()
        }
    }
}
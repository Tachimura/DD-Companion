package jds.projects.ddcompanion.my_classes.cards

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GameManual


class GameManualCardView(root: CardView, private var context: Context) {

    private var cardTitle: TextView = root.findViewById(R.id.txv_expanded_cardview__title)
    private var innerLayout: LinearLayout =
        root.findViewById(R.id.linearlayout_expanded_cardview__innerview)
    private var cardInnerTitle: TextView = root.findViewById(R.id.txv_expanded_cardview__innertitle)
    private var cardInnerData: TextView = root.findViewById(R.id.txv_expanded_cardview__innerdata)
    private var fabExpand: FloatingActionButton =
        root.findViewById(R.id.fab_expanded_cardview__expandbutton)
    private var listener: OnCardClickListener? = null

    interface OnCardClickListener {
        fun onCardClick()
    }

    fun setOnCardClickListener(listener: OnCardClickListener) {
        this.listener = listener
    }

    private var _expanded: Boolean = true
    val expanded: Boolean
        get() = _expanded

    private var _canExpand: Boolean = false
    var canExpand: Boolean
        get() = _canExpand
        set(value) {
            _canExpand = value
            modifyLayout()
        }

    init {
        fabExpand.setOnClickListener { if (expanded) collapse() else expand() }
        root.setOnClickListener { listener?.onCardClick() }
    }

    private fun modifyLayout() {
        if (canExpand) {
            fabExpand.visibility = View.VISIBLE
            fabExpand.isClickable = true
            fabExpand.isFocusable = true
            collapse()
        } else {
            fabExpand.visibility = View.GONE
            fabExpand.isClickable = false
            fabExpand.isFocusable = false
            expand()
        }
    }

    fun expand() {
        if (!expanded && canExpand) {
            innerLayout.visibility = View.VISIBLE
            _expanded = true
            fabExpand.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.baseline_arrow_circle_up_black_24dp,
                    context.theme
                )
            )
        }
    }

    fun collapse() {
        if (expanded && canExpand) {
            innerLayout.visibility = View.GONE
            _expanded = false
            fabExpand.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.baseline_arrow_circle_down_black_24dp,
                    context.theme
                )
            )
        }
    }

    fun updateCard(manual: GameManual) {
        cardTitle.text =
            ("${context.getString(R.string.game_manual_title)} ${manual.manualVersion}")
        cardInnerTitle.text =
            ("${context.resources.getString(R.string.game_viewer_manual_id)} ${manual.manualID}")
        cardInnerData.text =
            ("${context.resources.getString(R.string.game_viewer_released)} ${manual.manualDate}")
    }
}
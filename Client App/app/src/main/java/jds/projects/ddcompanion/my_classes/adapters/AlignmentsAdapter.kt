package jds.projects.ddcompanion.my_classes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import jds.projects.ddcompanion.R

class AlignmentsAdapter(private var context: Context, private var alignments: Array<String>) :
    BaseAdapter() {
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(
            clickedView: View,
            clickedViewPosition: Int,
            oldView: View?,
            oldViewPosition: Int
        )
    }

    private var oldViewPosition: Int = -1
    private var oldSelectedView: View? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val alignment = alignments[position]
        val returnView = if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            layoutInflater.inflate(R.layout.alignment_layout, null)
        } else
            convertView
        val txv = returnView.findViewById<TextView>(R.id.txv_alignment_layout__alignment)
        returnView.setOnClickListener {
            listener?.onItemClick(returnView, position, oldSelectedView, oldViewPosition)
            oldViewPosition = position
            oldSelectedView = returnView
        }
        txv.text = alignment
        return returnView
    }

    override fun getItem(position: Int): Any? {
        while (position in 0..alignments.size)
            return alignments[position]
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return alignments.size
    }
}
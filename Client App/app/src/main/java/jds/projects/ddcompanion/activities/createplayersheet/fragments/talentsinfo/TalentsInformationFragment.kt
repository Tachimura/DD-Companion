package jds.projects.ddcompanion.activities.createplayersheet.fragments.talentsinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.adapters.ExpandableCardAdapter
import jds.projects.ddcompanion.my_classes.dd_classes.DDTalent

class TalentsInformationFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: TalentsInformationViewModel
    private var listener: OnTalentInfoChangedListener? = null
    private lateinit var txvRemainingPoints: TextView

    interface OnTalentInfoChangedListener {
        fun onTalentSelected(talentSelected: DDTalent, remainingPoints: Int)
        fun onTalentDeselected(talentSelected: DDTalent, remainingPoints: Int)
    }

    fun setOnTalentInfoChangedListener(listener: OnTalentInfoChangedListener) {
        this.listener = listener
    }

    fun loadTalents(talents: MutableList<DDTalent>, manualID: Int, useablePoints: Int) {
        if (viewModel.needToUpdate(manualID)) {
            viewModel.updateAdapter(talents, manualID, useablePoints)
            updateRemainingPointsText()
        }
    }

    fun resetSelection() {
        viewModel.resetSelection()
    }

    private fun updateRemainingPointsText() {
        txvRemainingPoints.text = viewModel.useablePoints.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_talents_information, container, false)
        //init delle view
        viewModel = TalentsInformationViewModel(requireContext())
        txvRemainingPoints =
            root.findViewById(R.id.txv_fragment_talents_information__remainingpoints)
        //IMPOSTO IL RECYCLERVIEW
        recyclerView = root.findViewById(R.id.recyclerview_fragment_talents_information__talents)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //ottimizzazioni per la performance del recycler view
        recyclerView.setRecycledViewPool(RecyclerView.RecycledViewPool())
        recyclerView.setItemViewCacheSize(15)
        recyclerView.setHasFixedSize(true)
        //fine ottimizzazioni
        //imposto il adapter vuoto
        val adapter = viewModel.createAdapter()
        adapter.setOnCardClickListener(object :
            ExpandableCardAdapter.OnAdapterCardClickListener {
            override fun onCardClick(position: Int) {
                if (position != -1) {
                    if (viewModel.useablePoints > 0) {
                        val selectedTalent = viewModel.selectedTalent(position)
                        updateRemainingPointsText()
                        listener?.onTalentSelected(
                            selectedTalent,
                            viewModel.useablePoints
                        )
                    }
                }
            }

            override fun onCardDeactivated(position: Int) {
                if (position != -1) {
                    val deselectedTalent = viewModel.deselectTalent(position)
                    updateRemainingPointsText()
                    listener?.onTalentDeselected(
                        deselectedTalent,
                        viewModel.useablePoints
                    )
                }
            }
        })
        recyclerView.adapter = adapter
        return root
    }
}
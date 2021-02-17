package jds.projects.ddcompanion.activities.createplayersheet.fragments.raceinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.adapters.ExpandableCardAdapter
import jds.projects.ddcompanion.my_classes.dd_classes.DDRace


class RaceInformationFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: RaceInformationViewModel
    private var listener: OnRaceInfoChangedListener? = null

    interface OnRaceInfoChangedListener {
        fun onRaceSelected(raceSelected: DDRace)
    }

    fun setOnRaceInfoChangedListener(listener: OnRaceInfoChangedListener) {
        this.listener = listener
    }

    fun loadRaces(races: MutableList<DDRace>, manualID: Int) {
        if (viewModel.needToUpdate(manualID))
            viewModel.updateAdapter(races, manualID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_race_information, container, false)
        //init delle view
        viewModel = RaceInformationViewModel(requireContext())
        //INIT DEL RECYCLER-VIEW
        recyclerView = root.findViewById(R.id.recyclerview_fragment_race_information__races)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //ottimizzazioni per la performance del recycler view
        recyclerView.setRecycledViewPool(RecyclerView.RecycledViewPool())
        recyclerView.setItemViewCacheSize(10)
        recyclerView.setHasFixedSize(true)
        //fine ottimizzazioni
        val adapter = viewModel.createAdapter()
        adapter.setOnCardClickListener(object :
            ExpandableCardAdapter.OnAdapterCardClickListener {
            override fun onCardClick(position: Int) {
                if (position != -1)
                    listener?.onRaceSelected(viewModel.races[position])
            }

            override fun onCardDeactivated(position: Int) {}
        })
        recyclerView.adapter = adapter
        return root
    }
}
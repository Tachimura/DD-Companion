package jds.projects.ddcompanion.activities.createplayersheet.fragments.classinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.adapters.ExpandableCardAdapter
import jds.projects.ddcompanion.my_classes.dd_classes.DDClass


class ClassInformationFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ClassInformationViewModel
    private var listener: OnClassInfoChangedListener? = null

    interface OnClassInfoChangedListener {
        fun onClassSelected(classSelected: DDClass)
    }

    fun setOnClassInfoChangedListener(listener: OnClassInfoChangedListener) {
        this.listener = listener
    }

    fun loadClasses(classes: Array<DDClass>, manualID: Int, alignment: Int) {
        if (viewModel.needToUpdate(manualID, alignment))
            viewModel.updateAdapter(classes, manualID, alignment)
    }

    fun resetSelection() {
        viewModel.resetSelection()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_class_information, container, false)
        //init delle view
        viewModel = ClassInformationViewModel(requireContext())
        //INIT RECYCLER-VIEW
        recyclerView = root.findViewById(R.id.recyclerview_fragment_class_information__classes)
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
                listener?.onClassSelected(viewModel.classes[position])
            }

            override fun onCardDeactivated(position: Int) {}
        })
        recyclerView.adapter = adapter
        return root
    }
}
package jds.projects.ddcompanion.activities.createplayersheet.fragments.abilityinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.adapters.AbilityAdapter
import jds.projects.ddcompanion.my_classes.dd_classes.DDAbility

class AbilityInformationFragment : Fragment() {
    private lateinit var viewModel: AbilityInformationViewModel
    private var listener: OnAbilityLevelChangedListener? = null
    private lateinit var txvUseablePoints: TextView
    private lateinit var rcvAbilities: RecyclerView

    interface OnAbilityLevelChangedListener {
        fun onAbilityLevelChanged(abilityChanged: DDAbility, remainingPoints: Int)
    }

    fun setOnAbilityLevelChangedListener(listener: OnAbilityLevelChangedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_ability_information, container, false)
        viewModel = AbilityInformationViewModel(requireContext())
        //init delle view
        txvUseablePoints = root.findViewById(R.id.txv_fragment_ability_information__useablepoints)
        rcvAbilities = root.findViewById(R.id.recyclerview_fragment_ability_information__abilities)
        rcvAbilities.layoutManager = LinearLayoutManager(requireContext())
        //ottimizzazioni per la performance del recycler view
        rcvAbilities.setRecycledViewPool(RecyclerView.RecycledViewPool())
        rcvAbilities.setItemViewCacheSize(30)
        rcvAbilities.setHasFixedSize(true)
        //imposto listener al nuovo adapter
        val adapter = viewModel.createAdapter()
        adapter.setOnAbilityCardListener(object : AbilityAdapter.OnAbilityCardListener {
            override fun onAbilityValueChanged(abilityChanged: DDAbility, useablePoints: Int) {
                valueChanged(abilityChanged, useablePoints)
            }

            override fun onAbilityCardTitleClick(abilityClicked: DDAbility) {
                //TODO!: FINESTRA CHE MOSTRA INFORMAZIONI, ALLA FINE NON IMPLEMENTATA
                Toast.makeText(
                    requireContext(),
                    abilityClicked.name,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        rcvAbilities.adapter = viewModel.adapter
        //init textview useablePoints
        txvUseablePoints.text = "0"
        return root
    }

    fun loadAbilities(
        abilitiesMinLevel: MutableList<Int>,
        useablePoints: Int,
        playerModifier1: Int,
        playerModifier2: Int
    ) {
        viewModel.updateAdapter(abilitiesMinLevel, useablePoints, playerModifier1, playerModifier2)
        txvUseablePoints.text = useablePoints.toString()
    }

    private fun valueChanged(ability: DDAbility, useablePoints: Int) {
        txvUseablePoints.text = useablePoints.toString()
        listener?.onAbilityLevelChanged(ability, useablePoints)
    }
}
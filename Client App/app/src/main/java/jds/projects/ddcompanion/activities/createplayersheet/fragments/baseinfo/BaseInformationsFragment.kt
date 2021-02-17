package jds.projects.ddcompanion.activities.createplayersheet.fragments.baseinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridView
import android.widget.Spinner
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.adapters.AlignmentsAdapter
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GameManual


class BaseInformationsFragment : Fragment() {
    private lateinit var grvAlignments: GridView
    private lateinit var edtPlayerName: EditText
    private lateinit var spnVersion: Spinner
    private var listener: OnBaseInfoChangedListener? = null

    private lateinit var viewModel: BaseInformationsViewModel

    interface OnBaseInfoChangedListener {
        fun onPlayerNameChanged(playerName: String)
        fun onManualSelected(manual: GameManual)
        fun onAlignmentSelected(alignmentID: Int)
    }

    fun setOnBaseInfoChangedListener(listener: OnBaseInfoChangedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_base_information, container, false)
        //init delle view
        grvAlignments = root.findViewById(R.id.gridview_fragment_base_informations__alignments)
        edtPlayerName = root.findViewById(R.id.edt_fragment_base_informations__playername)
        spnVersion = root.findViewById(R.id.spinner_fragment_base_informations__sheetversion)
        //preparo viewmodel
        viewModel = BaseInformationsViewModel(requireContext())

        //imposto l'adapter della gridview con i vari allineamenti
        val alignmentsAdapter = viewModel.alignmentsAdapter
        alignmentsAdapter.setOnItemClickListener(object : AlignmentsAdapter.OnItemClickListener {
            override fun onItemClick(
                clickedView: View,
                clickedViewPosition: Int,
                oldView: View?,
                oldViewPosition: Int
            ) {
                //se era già stata selezionata una view prima la rimetto trasparente
                if (oldViewPosition != -1)
                    oldView!!.setBackgroundResource(android.R.color.transparent)
                //informo del click
                listener?.onAlignmentSelected(clickedViewPosition)
                //imposto la nuova view scura
                clickedView.setBackgroundResource(android.R.color.darker_gray)
            }
        })
        grvAlignments.adapter = alignmentsAdapter

        //imposto adapter dei manuali
        viewModel.loadGameManuals()
        val manualsAdapter = viewModel.gameManualsAdapter
        spnVersion.adapter = manualsAdapter
        spnVersion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                listener?.onManualSelected(viewModel.gameManuals[position])
            }
        }

        edtPlayerName.doAfterTextChanged { newPlayerName ->
            if (!newPlayerName.isNullOrEmpty() && !newPlayerName.isNullOrBlank())
                listener?.onPlayerNameChanged(newPlayerName.toString())
        }
        return root
    }
}
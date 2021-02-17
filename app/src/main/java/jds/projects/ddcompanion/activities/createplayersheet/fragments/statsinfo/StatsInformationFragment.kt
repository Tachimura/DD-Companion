package jds.projects.ddcompanion.activities.createplayersheet.fragments.statsinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.CARISMA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.COSTITUZIONE
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.DESTREZZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.FORZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.INTELLIGENZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.SAGGEZZA
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet


class StatsInformationFragment : Fragment() {
    private lateinit var viewModel: StatsInformationViewModel
    private var listener: OnStatsInfoChangedListener? = null
    private lateinit var txvUseablePoints: TextView
    private lateinit var txvPointsSTR: TextView
    private lateinit var txvPointsDEX: TextView
    private lateinit var txvPointsCOS: TextView
    private lateinit var txvPointsINT: TextView
    private lateinit var txvPointsSAG: TextView
    private lateinit var txvPointsCAR: TextView

    private lateinit var btnMinusSTR: Button
    private lateinit var btnMinusDEX: Button
    private lateinit var btnMinusCOS: Button
    private lateinit var btnMinusINT: Button
    private lateinit var btnMinusSAG: Button
    private lateinit var btnMinusCAR: Button

    private lateinit var btnPlusSTR: Button
    private lateinit var btnPlusDEX: Button
    private lateinit var btnPlusCOS: Button
    private lateinit var btnPlusINT: Button
    private lateinit var btnPlusSAG: Button
    private lateinit var btnPlusCAR: Button

    interface OnStatsInfoChangedListener {
        fun onStatChanged(statChanged: Int, newValue: Int, remainingPoints: Int)
        fun onStatInit(remainingPoints: Int)
    }

    fun setOnStatsInfoChangedListener(listener: OnStatsInfoChangedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_stats_information, container, false)
        //init delle view
        viewModel = StatsInformationViewModel(requireContext())

        txvUseablePoints = root.findViewById(R.id.txv_fragment_stats_information__useablepoints)
        //IMPOSTO I LISTENER DEI PULSANTI PREMUTI
        btnMinusSTR = root.findViewById(R.id.btn_fragment_stats_information__str_minus)
        btnMinusSTR.setOnClickListener { onMinusPressed(FORZA) }
        btnMinusDEX = root.findViewById(R.id.btn_fragment_stats_information__dex_minus)
        btnMinusDEX.setOnClickListener { onMinusPressed(DESTREZZA) }
        btnMinusCOS = root.findViewById(R.id.btn_fragment_stats_information__cos_minus)
        btnMinusCOS.setOnClickListener { onMinusPressed(COSTITUZIONE) }
        btnMinusINT = root.findViewById(R.id.btn_fragment_stats_information__int_minus)
        btnMinusINT.setOnClickListener { onMinusPressed(INTELLIGENZA) }
        btnMinusSAG = root.findViewById(R.id.btn_fragment_stats_information__sag_minus)
        btnMinusSAG.setOnClickListener { onMinusPressed(SAGGEZZA) }
        btnMinusCAR = root.findViewById(R.id.btn_fragment_stats_information__car_minus)
        btnMinusCAR.setOnClickListener { onMinusPressed(CARISMA) }

        btnPlusSTR = root.findViewById(R.id.btn_fragment_stats_information__str_plus)
        btnPlusSTR.setOnClickListener { onPlusPressed(FORZA) }
        btnPlusDEX = root.findViewById(R.id.btn_fragment_stats_information__dex_plus)
        btnPlusDEX.setOnClickListener { onPlusPressed(DESTREZZA) }
        btnPlusCOS = root.findViewById(R.id.btn_fragment_stats_information__cos_plus)
        btnPlusCOS.setOnClickListener { onPlusPressed(COSTITUZIONE) }
        btnPlusINT = root.findViewById(R.id.btn_fragment_stats_information__int_plus)
        btnPlusINT.setOnClickListener { onPlusPressed(INTELLIGENZA) }
        btnPlusSAG = root.findViewById(R.id.btn_fragment_stats_information__sag_plus)
        btnPlusSAG.setOnClickListener { onPlusPressed(SAGGEZZA) }
        btnPlusCAR = root.findViewById(R.id.btn_fragment_stats_information__car_plus)
        btnPlusCAR.setOnClickListener { onPlusPressed(CARISMA) }

        //imposto le textview che mostrano i punteggi
        txvPointsSTR = root.findViewById(R.id.txv_fragment_stats_information__str)
        txvPointsDEX = root.findViewById(R.id.txv_fragment_stats_information__dex)
        txvPointsCOS = root.findViewById(R.id.txv_fragment_stats_information__cos)
        txvPointsINT = root.findViewById(R.id.txv_fragment_stats_information__int)
        txvPointsSAG = root.findViewById(R.id.txv_fragment_stats_information__sag)
        txvPointsCAR = root.findViewById(R.id.txv_fragment_stats_information__car)

        listener?.onStatInit(viewModel.useablePoints)
        return root
    }

    private fun onMinusPressed(stat: Int) {
        val canChange = viewModel.reduce(stat)
        if (canChange) {
            valueChanged(stat)
            updateButtonUI(stat, 0, viewModel.canReduce(stat))
        }
    }

    private fun onPlusPressed(stat: Int) {
        val canChange = viewModel.increase(stat)
        if (canChange) {
            valueChanged(stat)
            updateButtonUI(stat, 1, true)
        }
    }

    private fun updateButtonUI(stat: Int, type: Int, canReduce: Boolean) {
        //se ho premuto un button back, controllo se posso nuovamente premerlo altrimenti li disattivo
        if (type == 0) {
            when (stat) {
                FORZA -> if (!canReduce) btnMinusSTR.isEnabled = false
                DESTREZZA -> if (!canReduce) btnMinusDEX.isEnabled = false
                COSTITUZIONE -> if (!canReduce) btnMinusCOS.isEnabled = false
                INTELLIGENZA -> if (!canReduce) btnMinusINT.isEnabled = false
                SAGGEZZA -> if (!canReduce) btnMinusSAG.isEnabled = false
                CARISMA -> if (!canReduce) btnMinusCAR.isEnabled = false
            }
            //se sono i button plus, riattivo i btnMinus
        } else {
            when (stat) {
                FORZA -> btnMinusSTR.isEnabled = true
                DESTREZZA -> btnMinusDEX.isEnabled = true
                COSTITUZIONE -> btnMinusCOS.isEnabled = true
                INTELLIGENZA -> btnMinusINT.isEnabled = true
                SAGGEZZA -> btnMinusSAG.isEnabled = true
                CARISMA -> btnMinusCAR.isEnabled = true
            }
        }
    }

    private fun valueChanged(stat: Int) {
        txvUseablePoints.text = viewModel.useablePoints.toString()
        updateStatTxvValue(stat, viewModel.stats.stats.getValue(stat))
    }

    private fun updateStatTxvValue(stat: Int, value: Int) {
        val textValue = "${PlayerSheet.statToString(value)}${PlayerSheet.statModifier(value)}"
        val txvToModify = when (stat) {
            FORZA -> txvPointsSTR
            DESTREZZA -> txvPointsDEX
            COSTITUZIONE -> txvPointsCOS
            INTELLIGENZA -> txvPointsINT
            SAGGEZZA -> txvPointsSAG
            CARISMA -> txvPointsCAR
            else -> null
        }
        txvToModify!!.text = textValue
        //avviso della modifica avvenuta
        listener?.onStatChanged(stat, value, viewModel.useablePoints)
    }

    fun loadStats(stats: PlayerStats, manualID: Int, useablePoints: Int) {
        viewModel.setUp(stats, manualID, useablePoints)
        txvUseablePoints.text = viewModel.useablePoints.toString()
        updateStatTxvValue(FORZA, viewModel.stats.stats.getValue(FORZA))
        updateStatTxvValue(DESTREZZA, viewModel.stats.stats.getValue(DESTREZZA))
        updateStatTxvValue(COSTITUZIONE, viewModel.stats.stats.getValue(COSTITUZIONE))
        updateStatTxvValue(INTELLIGENZA, viewModel.stats.stats.getValue(INTELLIGENZA))
        updateStatTxvValue(SAGGEZZA, viewModel.stats.stats.getValue(SAGGEZZA))
        updateStatTxvValue(CARISMA, viewModel.stats.stats.getValue(CARISMA))
        //imposto i minus a notenabled dato che le stats base sono il limite inferiore
        btnMinusSTR.isEnabled = false
        btnMinusDEX.isEnabled = false
        btnMinusCOS.isEnabled = false
        btnMinusINT.isEnabled = false
        btnMinusSAG.isEnabled = false
        btnMinusCAR.isEnabled = false
    }
}
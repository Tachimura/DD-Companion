package jds.projects.ddcompanion.my_classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet


class PlayerSheetsAdapter internal constructor(
    private var playerSheets: MutableList<PlayerSheet>,
    private var language: Int
) :
    RecyclerView.Adapter<PlayerSheetsAdapter.PlayerSheetViewHolder>() {
    private var listener: OnCardClickListener? = null

    interface OnCardClickListener {
        fun onCardClicked(position: Int)
        fun onCardLongClicked(position: Int): Boolean
    }

    fun setOnCardClickListener(onCardClickListener: OnCardClickListener) {
        this.listener = onCardClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerSheetViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_player_sheet, parent, false)
        val playerSheetCard = PlayerSheetViewHolder(itemView)
        itemView.setOnClickListener { listener?.onCardClicked(playerSheetCard.adapterPosition) }
        itemView.setOnLongClickListener {
            listener?.onCardLongClicked(playerSheetCard.adapterPosition) ?: false
        }
        return playerSheetCard
    }

    override fun getItemCount(): Int {
        return playerSheets.size
    }

    override fun onBindViewHolder(holder: PlayerSheetViewHolder, position: Int) {
        val playerSheet = playerSheets[position]
        holder.playerName.text = playerSheet.playerName
        holder.playerRace.text = playerSheet.playerRace.names.getTextWithLanguageID(language)
        holder.playerClass.text = playerSheet.playerMainClass.names.getTextWithLanguageID(language)
        holder.playerLevel.text = playerSheet.playerLevel.toString()
        holder.manualVersion.text = playerSheet.manualVersion.toString()
    }

    class PlayerSheetViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var playerName: TextView =
            itemView.findViewById(R.id.cardview_player_sheet_txv_player_name)
        var playerRace: TextView =
            itemView.findViewById(R.id.cardview_player_sheet_txv_player_race)
        var playerClass: TextView =
            itemView.findViewById(R.id.cardview_player_sheet_txv_player_class)
        var playerLevel: TextView =
            itemView.findViewById(R.id.cardview_player_sheet_txv_player_level)
        var manualVersion: TextView =
            itemView.findViewById(R.id.cardview_player_sheet_txv_manual_version)
    }

    fun updateLanguage(language: Int) {
        this.language = language
    }
}
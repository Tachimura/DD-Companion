package jds.projects.ddcompanion.my_classes.viewer

import android.content.Context
import android.view.View
import android.widget.*
import com.google.android.material.tabs.TabLayout
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.dd_classes.DDAbility
import jds.projects.ddcompanion.my_classes.dd_classes.DDTalent
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.CARISMA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.COSTITUZIONE
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.DESTREZZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.FORZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.INTELLIGENZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.SAGGEZZA
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerDDClass
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet


class PlayerSheetViewer(root: View, private var context: Context, private var language: Int) {

    //layouts
    private var lltBase = root.findViewById<ScrollView>(R.id.llt_view_playersheet__base)
    private var lltClasse = root.findViewById<LinearLayout>(R.id.llt_view_playersheet__classe)
    private var lltAbilita = root.findViewById<LinearLayout>(R.id.llt_view_playersheet__abilita)
    private var lltTalenti = root.findViewById<LinearLayout>(R.id.llt_view_playersheet__talenti)

    //VIEW DEL LAYOUT BASE
    private var txvNome = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__name)
    private var txvDDRazza = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__race)
    private var txvAllineamento =
        lltBase.findViewById<TextView>(R.id.txv_view_playersheet__alignment)
    private var txvLivello = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__level)
    private var txvHp = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__hp)

    private var txvStrTot = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__strtot)
    private var txvStrMod = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__strmod)
    private var txvStrBase = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__strbase)
    private var txvStrEtc = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__stretc)

    private var txvDexTot = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__dextot)
    private var txvDexMod = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__dexmod)
    private var txvDexBase = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__dexbase)
    private var txvDexEtc = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__dexetc)

    private var txvCosTot = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__costot)
    private var txvCosMod = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__cosmod)
    private var txvCosBase = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__cosbase)
    private var txvCosEtc = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__cosetc)

    private var txvIntTot = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__inttot)
    private var txvIntMod = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__intmod)
    private var txvIntBase = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__intbase)
    private var txvIntEtc = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__intetc)

    private var txvSagTot = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__sagtot)
    private var txvSagMod = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__sagmod)
    private var txvSagBase = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__sagbase)
    private var txvSagEtc = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__sagetc)

    private var txvCarTot = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__cartot)
    private var txvCarMod = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__carmod)
    private var txvCarBase = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__carbase)
    private var txvCarEtc = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__caretc)


    private var txvModHT = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__modtc)
    private var txvModCA = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__bonusca)
    private var txvModTempra = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__salvtem)
    private var txvModRiflessi = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__salvrif)
    private var txvModVolonta = lltBase.findViewById<TextView>(R.id.txv_view_playersheet__salvvol)

    //VIEW DEL LAYOUT CLASSI
    private var lsvClasses = lltClasse.findViewById<ListView>(R.id.lsv_view_playersheet__classi)

    //VIEW DEL LAYOUT ABILITIES
    private var lsvAbilities = lltAbilita.findViewById<ListView>(R.id.lsv_view_playersheet__abilita)

    //VIEW DEL LAYOUT TALENTS
    private var lsvTalents = lltTalenti.findViewById<ListView>(R.id.lsv_view_playersheet__talenti)

    //navigation
    private var bnvTabs = root.findViewById<TabLayout>(R.id.bnav_view_player_sheet)
    private var activePage = 0

    private object PAGES {
        const val BASE = 0
        const val CLASSE = 1
        const val ABILITA = 2
        const val TALENTI = 3
    }
    private fun showPage(pageToShow: Int) {
        if (pageToShow != activePage) {
            when (activePage) {
                PAGES.BASE -> lltBase.visibility = View.GONE
                PAGES.CLASSE -> lltClasse.visibility = View.GONE
                PAGES.ABILITA -> lltAbilita.visibility = View.GONE
                PAGES.TALENTI -> lltTalenti.visibility = View.GONE
            }
            activePage = pageToShow
            when (pageToShow) {
                PAGES.BASE -> lltBase.visibility = View.VISIBLE
                PAGES.CLASSE -> lltClasse.visibility = View.VISIBLE
                PAGES.ABILITA -> lltAbilita.visibility = View.VISIBLE
                PAGES.TALENTI -> lltTalenti.visibility = View.VISIBLE
            }
        }
    }
    fun loadPlayerSheet(playerSheet: PlayerSheet) {
        //imposto i vari load
        loadBasePage(playerSheet)
        //loadClassePage
        loadClassePage(playerSheet.playerClasses)
        //loadAbilitaPage
        loadAbilitiesPage(playerSheet.playerAbilities)
        //loadTalentiPage
        loadTalentsPage(playerSheet.playerTalents)
        bnvTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    showPage(tab.position)
                }
            }
        })
        //mostro la prima pagina
        showPage(PAGES.BASE)
    }
    private fun loadBasePage(playerSheet: PlayerSheet) {
        txvNome.text = playerSheet.playerName
        txvDDRazza.text = playerSheet.playerRace.names.getTextWithLanguageID(language)
        txvAllineamento.text =
            context.resources.getStringArray(R.array.alignments_inline)[playerSheet.playerAlignmentID]
        txvLivello.text = playerSheet.playerLevel.toString()
        txvHp.text = playerSheet.playerHP.toString()

        //modificatori di caratteristica
        var strTot = 0
        var dexTot = 0
        var cosTot = 0
        var intTot = 0
        var sagTot = 0
        var carTot = 0

        //modificatori dei talenti su tiri salvezza, tiro x colpire e classe armatura
        var ht = 0
        var ca = 0
        var tempra = 0
        var riflessi = 0
        var volonta = 0

        //modificatori base del personaggio
        val strBase = playerSheet.playerStats.stats.getValue(FORZA)
        val dexBase = playerSheet.playerStats.stats.getValue(DESTREZZA)
        val cosBase = playerSheet.playerStats.stats.getValue(COSTITUZIONE)
        val intBase = playerSheet.playerStats.stats.getValue(INTELLIGENZA)
        val sagBase = playerSheet.playerStats.stats.getValue(SAGGEZZA)
        val carBase = playerSheet.playerStats.stats.getValue(CARISMA)
        txvStrBase.text = strBase.toString()
        txvDexBase.text = dexBase.toString()
        txvCosBase.text = cosBase.toString()
        txvIntBase.text = intBase.toString()
        txvSagBase.text = sagBase.toString()
        txvCarBase.text = carBase.toString()
        strTot += strBase
        dexTot += dexBase
        cosTot += cosBase
        intTot += intBase
        sagTot += sagBase
        carTot += carBase

        //modificatori di razza + bonus ausiliari dei talenti
        var strEtc = playerSheet.playerRace.stats.stats.getValue(FORZA)
        var dexEtc = playerSheet.playerRace.stats.stats.getValue(DESTREZZA)
        var cosEtc = playerSheet.playerRace.stats.stats.getValue(COSTITUZIONE)
        var intEtc = playerSheet.playerRace.stats.stats.getValue(INTELLIGENZA)
        var sagEtc = playerSheet.playerRace.stats.stats.getValue(SAGGEZZA)
        var carEtc = playerSheet.playerRace.stats.stats.getValue(CARISMA)
        //BONUS AUSILIARI DEI TALENTI
        for (talent in playerSheet.playerTalents) {
            strEtc += talent.stats.stats.getValue(FORZA)
            dexEtc += talent.stats.stats.getValue(DESTREZZA)
            cosEtc += talent.stats.stats.getValue(COSTITUZIONE)
            intEtc += talent.stats.stats.getValue(INTELLIGENZA)
            sagEtc += talent.stats.stats.getValue(SAGGEZZA)
            carEtc += talent.stats.stats.getValue(CARISMA)
            ht += talent.modTC
            ca += talent.modCA
            tempra += talent.salvTempra
            riflessi += talent.salvRiflessi
            volonta += talent.salvVolonta
        }
        txvStrEtc.text =
            PlayerSheet.statToString(
                strEtc
            )
        txvDexEtc.text =
            PlayerSheet.statToString(
                dexEtc
            )
        txvCosEtc.text =
            PlayerSheet.statToString(
                cosEtc
            )
        txvIntEtc.text =
            PlayerSheet.statToString(
                intEtc
            )
        txvSagEtc.text =
            PlayerSheet.statToString(
                sagEtc
            )
        txvCarEtc.text =
            PlayerSheet.statToString(
                carEtc
            )
        strTot += strEtc
        dexTot += dexEtc
        cosTot += cosEtc
        intTot += intEtc
        sagTot += sagEtc
        carTot += carEtc

        //totale
        txvStrTot.text = strTot.toString()
        txvDexTot.text = dexTot.toString()
        txvCosTot.text = cosTot.toString()
        txvIntTot.text = intTot.toString()
        txvSagTot.text = sagTot.toString()
        txvCarTot.text = carTot.toString()

        //modificatori
        txvStrMod.text =
            PlayerSheet.statModifier(
                strTot
            )
        txvDexMod.text =
            PlayerSheet.statModifier(
                dexTot
            )
        txvCosMod.text =
            PlayerSheet.statModifier(
                cosTot
            )
        txvIntMod.text =
            PlayerSheet.statModifier(
                intTot
            )
        txvSagMod.text =
            PlayerSheet.statModifier(
                sagTot
            )
        txvCarMod.text =
            PlayerSheet.statModifier(
                carTot
            )

        txvModHT.text = modifierValueToString(ht)
        txvModCA.text = modifierValueToString(ca)
        txvModTempra.text = modifierValueToString(tempra)
        txvModRiflessi.text = modifierValueToString(riflessi)
        txvModVolonta.text = modifierValueToString(volonta)
    }
    private fun loadClassePage(playerDDClass: PlayerDDClass) {
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
        for ((ddClass, level) in playerDDClass.getAll())
            adapter.add(
                ddClass.names.getTextWithLanguageID(language) + ",  " + context.resources.getString(
                    R.string.player_sheet_level
                ) + " " + level.toString()
            )
        lsvClasses.adapter = adapter
    }
    private fun loadAbilitiesPage(abilities: List<DDAbility>) {
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
        for (ability in abilities)
            adapter.add(ability.name + ",  " + context.resources.getString(R.string.player_sheet_level) + " " + ability.level.toString())
        lsvAbilities.adapter = adapter
    }
    private fun loadTalentsPage(talents: List<DDTalent>) {
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
        for (talent in talents)
            adapter.add(talent.names.getTextWithLanguageID(language))
        lsvTalents.adapter = adapter
    }

    private fun modifierValueToString(value: Int): String {
        return if (value > 0) "+$value " else " $value "
    }
}
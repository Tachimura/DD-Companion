package jds.projects.ddcompanion.my_classes.viewer

import android.content.Context
import android.os.Build
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.dd_classes.DDAlignments
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.CARISMA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.COSTITUZIONE
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.DESTREZZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.FORZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.INTELLIGENZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.SAGGEZZA
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GameManual

class GameManualViewer(private var context: Context, root: View) {
    //info del manuale
    private lateinit var manual: GameManual

    //variabili grafiche
    private var txvManualTitle =
        root.findViewById<TextView>(R.id.txv_view_game_manual_full_infos_manualeversione)
    private var txvManualID =
        root.findViewById<TextView>(R.id.txv_view_game_manual_full_infos_manualeid)
    private var txvManualDate =
        root.findViewById<TextView>(R.id.txv_view_game_manual_full_infos_manualedata)
    private var txvManualUsablePoints =
        root.findViewById<TextView>(R.id.txv_view_game_manual_full_infos_manualepuntiutilizzabili)

    //statistiche base
    private var txvManualBaseStr =
        root.findViewById<TextView>(R.id.txv_view_game_manual_full_infos_manuale_basestr)
    private var txvManualBaseDex =
        root.findViewById<TextView>(R.id.txv_view_game_manual_full_infos_manuale_basedex)
    private var txvManualBaseCos =
        root.findViewById<TextView>(R.id.txv_view_game_manual_full_infos_manuale_basecos)
    private var txvManualBaseInt =
        root.findViewById<TextView>(R.id.txv_view_game_manual_full_infos_manuale_baseint)
    private var txvManualBaseSag =
        root.findViewById<TextView>(R.id.txv_view_game_manual_full_infos_manuale_basesag)
    private var txvManualBaseCar =
        root.findViewById<TextView>(R.id.txv_view_game_manual_full_infos_manuale_basecar)

    //note rilasciate
    private var txvManualNotes =
        root.findViewById<TextView>(R.id.txv_view_game_manual_full_infos_manuale_notes)

    //layout x inserirci razze e classi
    private var lltBase =
        root.findViewById<LinearLayout>(R.id.llt_view_game_manual_full_infos_manuale_base)
    private var lltRaces =
        root.findViewById<LinearLayout>(R.id.llt_view_game_manual_full_infos_manuale_races)
    private var lltClasses =
        root.findViewById<LinearLayout>(R.id.llt_view_game_manual_full_infos_manuale_classes)
    private var lltTalents =
        root.findViewById<LinearLayout>(R.id.llt_view_game_manual_full_infos_manuale_talents)

    private var bnvTabs = root.findViewById<TabLayout>(R.id.bnav_view_game_manual)

    private var activePage = 0

    private object PAGES {
        const val BASE = 0
        const val RAZZE = 1
        const val CLASSE = 2
        const val TALENTI = 3
    }

    private fun showPage(pageToShow: Int) {
        if (pageToShow != activePage) {
            when (activePage) {
                PAGES.BASE -> lltBase.visibility = View.GONE
                PAGES.RAZZE -> lltRaces.visibility = View.GONE
                PAGES.CLASSE -> lltClasses.visibility = View.GONE
                PAGES.TALENTI -> lltTalents.visibility = View.GONE
            }
            //tabsNavigator.activate(pageToShow)
            activePage = pageToShow
            when (pageToShow) {
                PAGES.BASE -> lltBase.visibility = View.VISIBLE
                PAGES.RAZZE -> lltRaces.visibility = View.VISIBLE
                PAGES.CLASSE -> lltClasses.visibility = View.VISIBLE
                PAGES.TALENTI -> lltTalents.visibility = View.VISIBLE
            }
        }
    }

    fun loadGameManual(manualID: Int, languageID: Int) {
        manual = DBInterface.getInstance(context).getGameManualFullInfo(manualID)
        loadBasePage(languageID)
        loadRacesPage(languageID)
        loadClassesPage(languageID)
        loadTalentsPage(languageID)
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
    private fun loadBasePage(languageID: Int) {
        txvManualTitle.text =
            ("${context.resources.getString(R.string.game_viewer_title)} ${manual.manualVersion}")
        txvManualID.text = manual.manualID.toString()
        txvManualDate.text = manual.manualDate
        txvManualUsablePoints.text = manual.manualUsablePoints.toString()
        txvManualNotes.text = manual.manualNotes.getTextWithLanguageID(languageID)

        //imposto i valori delle textview che indicano le stats di base
        txvManualBaseStr.text = manual.manualBaseStats.stats[FORZA].toString()
        txvManualBaseDex.text = manual.manualBaseStats.stats[DESTREZZA].toString()
        txvManualBaseCos.text = manual.manualBaseStats.stats[COSTITUZIONE].toString()
        txvManualBaseInt.text = manual.manualBaseStats.stats[INTELLIGENZA].toString()
        txvManualBaseSag.text = manual.manualBaseStats.stats[SAGGEZZA].toString()
        txvManualBaseCar.text = manual.manualBaseStats.stats[CARISMA].toString()
    }
    private fun loadRacesPage(languageID: Int) {
        //inserisco le RAZZE
        var cont = 0
        for (ddRace in manual.availableRaces) {
            //creo una view usando il mio layout view_race_full_infos e lo collego a lltRaces
            val raceView = View.inflate(context, R.layout.view_race_full_infos, null)
            val txvRaceName =
                raceView.findViewById<TextView>(R.id.txv_view_race_full_infos__racename)
            val txvIsRaceNew = raceView.findViewById<TextView>(R.id.txv_view_race_full_infos__new)
            txvRaceName.text = ddRace.names.getTextWithLanguageID(languageID)
            //txvRaceName.setTextColor(colorWhiteText)
            //imposto il new flag
            if (ddRace.isNew) {
                setUpIsNewTextView(txvIsRaceNew)
            } else
                txvIsRaceNew.visibility = View.GONE
            //genero il layout dei modificatori di classe
            createStatModifierLayout(
                context,
                ddRace.stats,
                raceView.findViewById(R.id.llt_view_race_full_infos__racemodifiers)
            )
            lltRaces.addView(raceView)
            //aggiungo separatore
            if (cont < manual.availableRaces.size) {
                val separator = View.inflate(context, R.layout.view_horizontal_divider, null)
                lltRaces.addView(separator)
            }
            cont += 1
        }
    }
    private fun loadClassesPage(languageID: Int) {
        //inserisco le CLASSI
        var cont = 0
        for (ddClass in manual.availableClasses) {
            //creo una view usando il mio layout view_race_full_infos e lo collego a lltRaces
            val classView = View.inflate(context, R.layout.view_class_full_infos, null)
            val txvClassName =
                classView.findViewById<TextView>(R.id.txv_view_class_full_infos__classname)
            val txvIsClassNew =
                classView.findViewById<TextView>(R.id.txv_view_class_full_infos__new)
            val txvClassHitDice =
                classView.findViewById<TextView>(R.id.txv_view_class_full_infos__hitdice)
            val txvClassHitDiceSucc =
                classView.findViewById<TextView>(R.id.txv_view_class_full_infos__hitdicenext)
            val txvClassMainModifier =
                classView.findViewById<TextView>(R.id.txv_view_class_full_infos__primarymodifier)
            val txvClassSavingModifiers =
                classView.findViewById<TextView>(R.id.txv_view_class_full_infos__savingmodifiers)
            val txvClassAbilityPoints =
                classView.findViewById<TextView>(R.id.txv_view_class_full_infos__abilitypoints)
            //nome
            txvClassName.text = ddClass.names.getTextWithLanguageID(languageID)
            //imposto il new flag
            if (ddClass.isNew)
                setUpIsNewTextView(txvIsClassNew)
            else
                txvIsClassNew.visibility = View.GONE
            //hit dice
            txvClassHitDice.text = ("d${ddClass.hpDice}")
            txvClassHitDiceSucc.text = ("d${ddClass.hpDiceSucc}")
            //main modifier
            txvClassMainModifier.text = modifierIDIntoText(ddClass.mainModifier)
            //saving modifiers
            txvClassSavingModifiers.text =
                ("${modifierIDIntoText(ddClass.modifierSalv1)} & ${modifierIDIntoText(ddClass.modifierSalv2)}")
            //ability points
            txvClassAbilityPoints.text =
                ("${ddClass.abilityPoints} + ${context.resources.getString(R.string.stat_intelligenza)}")
            //genero il layout degli allineamenti non permessi
            createAllowedAlignmentsLayout(
                context,
                ddClass.allowedAlignments,
                classView.findViewById(R.id.llt_view_class_full_infos__classallowedalignments)
            )
            lltClasses.addView(classView)
            //aggiungo separatore
            if (cont < manual.availableClasses.size) {
                val separator = View.inflate(context, R.layout.view_horizontal_divider, null)
                lltClasses.addView(separator)
            }
            cont += 1
        }
    }
    private fun loadTalentsPage(languageID: Int) {
        var cont = 0
        for (ddTalent in manual.availableTalents) {
            //creo una view usando il mio layout view_race_full_infos e lo collego a lltRaces
            val talentView = View.inflate(context, R.layout.view_talent_full_infos, null)
            val txvTalentName =
                talentView.findViewById<TextView>(R.id.txv_view_talent_full_infos__talentname)
            val txvIsTalentNew =
                talentView.findViewById<TextView>(R.id.txv_view_talent_full_infos__new)
            val txvTalentDescription =
                talentView.findViewById<TextView>(R.id.txv_view_talent_full_infos__talentdescription)
            txvTalentName.text = ddTalent.names.getTextWithLanguageID(languageID)
            txvTalentDescription.text = ddTalent.descriptions.getTextWithLanguageID(languageID)
            //imposto il new flag
            if (ddTalent.isNew) {
                setUpIsNewTextView(txvIsTalentNew)
            } else
                txvIsTalentNew.visibility = View.GONE
            //genero il layout dei modificatori di talento
            val modifierLayout =
                talentView.findViewById<LinearLayout>(R.id.llt_view_talent_full_infos__talentmodifiers)
            createStatModifierLayout(context, ddTalent.stats, modifierLayout)
            //controllo se ci sono i modificatori speciali di talento da mettere o meno
            var otherModifiersText = ""
            if (ddTalent.salvTempra != 0)
                otherModifiersText += otherModifierTextFun(
                    ddTalent.salvTempra,
                    context.getString(R.string.tiri_salvezza_tempra)
                )
            if (ddTalent.salvRiflessi != 0)
                otherModifiersText += otherModifierTextFun(
                    ddTalent.salvRiflessi,
                    context.getString(R.string.tiri_salvezza_riflessi)
                )
            if (ddTalent.salvVolonta != 0)
                otherModifiersText += otherModifierTextFun(
                    ddTalent.salvVolonta,
                    context.getString(R.string.tiri_salvezza_volonta)
                )
            if (ddTalent.modTC != 0)
                otherModifiersText += modifierValueToString(ddTalent.modTC) + context.getString(R.string.bonus_tc_ai) + "\n"
            if (ddTalent.modCA != 0)
                otherModifiersText += modifierValueToString(ddTalent.modCA) + context.getString(R.string.bonus_ca_at) + "\n"

            if (otherModifiersText.isNotEmpty() && otherModifiersText.isNotBlank()) {
                val txvOtherModifiers = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    TextView(context, null, 0, R.style.TextAppearance_AppCompat_Body2)
                else
                    TextView(context, null, R.style.TextAppearance_AppCompat_Body2)

                txvOtherModifiers.text =
                    otherModifiersText.substring(0, otherModifiersText.length - 1)
                modifierLayout.addView(txvOtherModifiers)
            }
            lltTalents.addView(talentView)
            //aggiungo separatore
            if (cont < manual.availableTalents.size) {
                val separator = View.inflate(context, R.layout.view_horizontal_divider, null)
                lltTalents.addView(separator)
            }
            cont += 1
        }
    }


    private fun modifierValueToString(value: Int): String {
        return if (value > 0) "+$value " else " $value "
    }

    private fun otherModifierTextFun(value: Int, description: String): String {
        return modifierValueToString(value) + description + "\n"
    }

    private fun modifierIDIntoText(modID: Int): String {
        return when (modID) {
            FORZA -> context.getString(R.string.stat_forza_full)
            DESTREZZA -> context.getString(R.string.stat_destrezza_full)
            COSTITUZIONE -> context.getString(R.string.stat_costituzione_full)
            INTELLIGENZA -> context.getString(R.string.stat_intelligenza_full)
            SAGGEZZA -> context.getString(R.string.stat_saggezza_full)
            CARISMA -> context.getString(R.string.stat_carisma_full)
            else -> ""
        }
    }

    private fun setUpIsNewTextView(txtNew: TextView) {
        txtNew.visibility = View.VISIBLE
        val anim = AlphaAnimation(0.0f, 1.0f)
        //animazione di blinking
        anim.duration = 200 //You can manage the blinking time with this parameter
        anim.startOffset = 50
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = Animation.INFINITE
        txtNew.animation = anim
    }

    private fun createStatModifierLayout(
        context: Context,
        stats: PlayerStats,
        containerLayout: LinearLayout
    ) {
        val txvStat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            TextView(context, null, 0, R.style.TextAppearance_AppCompat_Body2)
        else
            TextView(context, null, R.style.TextAppearance_AppCompat_Body2)
        var statString = ""
        for (stat in stats.stats) {
            if (stat.value != 0) {
                statString = when (stat.key) {
                    FORZA -> statString + (modifierValueToString(stat.value) + context.getString(R.string.stat_forza_full)) + "\n"
                    DESTREZZA -> statString + (modifierValueToString(stat.value) + context.getString(
                        R.string.stat_destrezza_full
                    )) + "\n"
                    COSTITUZIONE -> statString + (modifierValueToString(stat.value) + context.getString(
                        R.string.stat_costituzione_full
                    )) + "\n"
                    INTELLIGENZA -> statString + (modifierValueToString(stat.value) + context.getString(
                        R.string.stat_intelligenza_full
                    )) + "\n"
                    SAGGEZZA -> statString + (modifierValueToString(stat.value) + context.getString(
                        R.string.stat_saggezza_full
                    )) + "\n"
                    CARISMA -> statString + (modifierValueToString(stat.value) + context.getString(R.string.stat_carisma_full)) + "\n"
                    else -> ""
                }
            }
        }
        if (statString.isNotEmpty() && statString.isNotBlank()) {
            txvStat.text = statString.substring(0, statString.length - 1)
            containerLayout.addView(txvStat)
        }
    }

    private fun createAllowedAlignmentsLayout(
        context: Context,
        alignments: DDAlignments,
        containerLayout: LinearLayout
    ) {
        val txtAllowedAlignments = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            TextView(context, null, 0, R.style.TextAppearance_AppCompat_Body2)
        else
            TextView(context, null, R.style.TextAppearance_AppCompat_Body2)
        var alignmentsString = ""
        for (alignment in alignments.alignments) {
            //SE è = 1 VUOL DIRE CHE è PERMESSO E ALLORA LO MOSTRO
            if (alignment.value == 1) {
                alignmentsString =
                    alignmentsString + context.resources.getStringArray(R.array.alignments_inline)[alignment.key] + "\n"
            }
        }
        txtAllowedAlignments.text = alignmentsString.substring(0, alignmentsString.length - 1)
        containerLayout.addView(txtAllowedAlignments)
    }
}
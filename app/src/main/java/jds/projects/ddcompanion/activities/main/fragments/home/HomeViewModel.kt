package jds.projects.ddcompanion.activities.main.fragments.home

import android.content.Context
import jds.projects.ddcompanion.my_classes.adapters.GameManualsAdapter
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.dd_classes.*
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GMLanguage
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GameManual
import jds.projects.ddcompanion.my_classes.utils.MLanguageText
import org.json.JSONArray

class HomeViewModel(context: Context) {
    private val gameManuals = mutableListOf<GameManual>()
    private lateinit var latestManual: GameManual
    private lateinit var adapter: GameManualsAdapter
    private val dbHelper = DBInterface.getInstance(context)

    fun loadGameManualsBaseInfo() {
        for (manual in dbHelper.getGameManualsBaseInfo()) addGameManual(
            manual
        )
        updateLatestGameManual()
    }

    fun getGameManualsBaseInfo(): MutableList<GameManual> {
        return gameManuals
    }

    private fun insertNewGameManual(manual: GameManual) {
        addGameManual(manual)
        dbHelper.insertGameManual(manual)
    }

    private fun addGameManual(manual: GameManual) {
        gameManuals.add(manual)
    }

    fun setAdapter(adapter: GameManualsAdapter) {
        this.adapter = adapter
    }

    fun getAdapter(): GameManualsAdapter {
        return adapter
    }

    fun updateAdapter() {
        adapter.notifyDataSetChanged()
    }

    fun getLatestGameManual(): GameManual {
        return latestManual
    }

    fun updateLatestGameManual(): GameManual {
        latestManual = gameManuals.maxBy { it.manualID }!!
        return latestManual
    }

    fun isLatestGameManualOutdated(manual: GameManual): Boolean {
        return manual.manualID > latestManual.manualID
    }

    fun updateGameManualsDatabase(update: JSONArray, languages: JSONArray): Int {
        var newManuals = 0
        //gestione linguaggi
        for (cont in 0 until languages.length()) {
            val languageJSON = languages.getJSONObject(cont)
            val languageID = languageJSON.getInt("lan_id")
            val languageName = languageJSON.getString("lan_name")
            dbHelper.insertNewGMLanguage(
                GMLanguage(
                    languageID,
                    languageName
                )
            )
        }
        //gestione manuali di gioco
        for (cont in 0 until update.length()) {
            val manualJSON = update.getJSONObject(cont)
            val manualID = manualJSON.getInt("id")
            val manualVersion = manualJSON.getDouble("version")
            val manualReleaseData = manualJSON.getString("release_data")
            val manualUsablePoints = manualJSON.getInt("usable_points")
            val manualBaseStr = manualJSON.getInt("base_str")
            val manualBaseDex = manualJSON.getInt("base_dex")
            val manualBaseCos = manualJSON.getInt("base_cos")
            val manualBaseInt = manualJSON.getInt("base_int")
            val manualBaseSag = manualJSON.getInt("base_sag")
            val manualBaseCar = manualJSON.getInt("base_car")
            val manualNotesJSON = manualJSON.getJSONArray("notes")
            val manualRacesJSON = manualJSON.getJSONArray("races")
            val manualClassesJSON = manualJSON.getJSONArray("classes")
            val manualTalentsJSON = manualJSON.getJSONArray("talents")
            val manualNotes =
                MLanguageText()
            //caricamento delle note
            for (cont2 in 0 until manualNotesJSON.length()) {
                val noteJSON = manualNotesJSON.getJSONObject(cont2)
                val notesID = noteJSON.getInt("lan_id")
                val notesText = noteJSON.getString("note")
                manualNotes.addMLanguageText(notesID, notesText)
            }

            //caricamento delle razze
            val racesArray = mutableListOf<DDRace>()
            for (cont2 in 0 until manualRacesJSON.length()) {
                val raceJSON = manualRacesJSON.getJSONObject(cont2)
                val raceID = raceJSON.getInt("id")
                val raceNew = dbHelper.intToBool(raceJSON.getInt("new"))
                //se è nuovo allora ci sono anche i dati x memorizzarlo
                if (raceNew) {
                    val raceIDName = raceJSON.getInt("id_name")
                    val raceStr = raceJSON.getInt("mod_str")
                    val raceDex = raceJSON.getInt("mod_dex")
                    val raceCos = raceJSON.getInt("mod_cos")
                    val raceInt = raceJSON.getInt("mod_int")
                    val raceSag = raceJSON.getInt("mod_sag")
                    val raceCar = raceJSON.getInt("mod_car")
                    val raceNamesJSON = raceJSON.getJSONArray("names")
                    val raceNames =
                        MLanguageText()
                    //caricamento dei nomi della razza nei vari linguaggi
                    for (contRName in 0 until raceNamesJSON.length()) {
                        val rNameJSON = raceNamesJSON.getJSONObject(contRName)
                        val nameID = rNameJSON.getInt("race_lan_id")
                        val nameText = rNameJSON.getString("race_name")
                        raceNames.addMLanguageText(nameID, nameText)
                    }
                    racesArray.add(
                        DDRace(
                            raceID,
                            raceNew,
                            raceIDName,
                            raceNames,
                            PlayerStats(raceStr, raceDex, raceCos, raceInt, raceSag, raceCar)
                        )
                    )
                } else
                    racesArray.add(DDRace(raceID, raceNew))
            }//end caricamento razze

            //caricamento delle classi
            val classesArray = mutableListOf<DDClass>()
            for (cont2 in 0 until manualClassesJSON.length()) {
                val classJSON = manualClassesJSON.getJSONObject(cont2)
                val classID = classJSON.getInt("id")
                val classNew = dbHelper.intToBool(classJSON.getInt("new"))
                //se è nuovo allora ci sono anche i dati x memorizzarlo
                if (classNew) {
                    val classIDName = classJSON.getInt("id_name")
                    val hpDice = classJSON.getInt("hpdice")
                    val hpDiceSucc = classJSON.getInt("hpdice_next")
                    val mainModifier = classJSON.getInt("main_mod")
                    val modSalv1 = classJSON.getInt("salv_1")
                    val modSalv2 = classJSON.getInt("salv_2")
                    val abilityPoints = classJSON.getInt("ability_points")

                    val algLb = classJSON.getInt("alg_lb")
                    val algLn = classJSON.getInt("alg_ln")
                    val algLm = classJSON.getInt("alg_lm")
                    val algNb = classJSON.getInt("alg_nb")
                    val algNn = classJSON.getInt("alg_nn")
                    val algNm = classJSON.getInt("alg_nm")
                    val algCb = classJSON.getInt("alg_cb")
                    val algCn = classJSON.getInt("alg_cn")
                    val algCm = classJSON.getInt("alg_cm")
                    val classNamesJSON = classJSON.getJSONArray("names")
                    val classNames =
                        MLanguageText()
                    //caricamento dei nomi della razza nei vari linguaggi
                    for (contRName in 0 until classNamesJSON.length()) {
                        val cNameJSON = classNamesJSON.getJSONObject(contRName)
                        val nameID = cNameJSON.getInt("class_lan_id")
                        val nameText = cNameJSON.getString("class_name")
                        classNames.addMLanguageText(nameID, nameText)
                    }
                    classesArray.add(
                        DDClass(
                            classID, classNew, classIDName, classNames,
                            hpDice, hpDiceSucc, mainModifier, modSalv1, modSalv2,
                            abilityPoints,
                            DDAlignments(
                                algLb,
                                algLn,
                                algLm,
                                algNb,
                                algNn,
                                algNm,
                                algCb,
                                algCn,
                                algCm
                            )
                        )
                    )
                } else
                    classesArray.add(DDClass(classID, classNew))
            }//end caricamento classi
            //caricamento dei talenti
            val talentsArray = mutableListOf<DDTalent>()
            for (cont2 in 0 until manualTalentsJSON.length()) {
                val talentJSON = manualTalentsJSON.getJSONObject(cont2)
                val talentID = talentJSON.getInt("id")
                val talentNew = dbHelper.intToBool(talentJSON.getInt("new"))
                //se è nuovo allora ci sono anche i dati x memorizzarlo
                if (talentNew) {
                    val talentIDName = talentJSON.getInt("id_name")
                    val talentMinLvl = talentJSON.getInt("min_lvl")
                    val talentStr = talentJSON.getInt("mod_str")
                    val talentDex = talentJSON.getInt("mod_dex")
                    val talentCos = talentJSON.getInt("mod_cos")
                    val talentInt = talentJSON.getInt("mod_int")
                    val talentSag = talentJSON.getInt("mod_sag")
                    val talentCar = talentJSON.getInt("mod_car")
                    val talentSalTem = talentJSON.getInt("sal_tem")
                    val talentSalRif = talentJSON.getInt("sal_rif")
                    val talentSalVol = talentJSON.getInt("sal_vol")
                    val talentTC = talentJSON.getInt("mod_tc")
                    val talentCA = talentJSON.getInt("mod_ca")
                    val talentNamesJSON = talentJSON.getJSONArray("description")
                    val talentNames =
                        MLanguageText()
                    val talentDescriptions =
                        MLanguageText()
                    //caricamento dei nomi della razza nei vari linguaggi
                    for (contTName in 0 until talentNamesJSON.length()) {
                        val tNameJSON = talentNamesJSON.getJSONObject(contTName)
                        val languageID = tNameJSON.getInt("talent_lan_id")
                        val nameText = tNameJSON.getString("talent_name")
                        val descriptionText = tNameJSON.getString("talent_description")
                        talentNames.addMLanguageText(languageID, nameText)
                        talentDescriptions.addMLanguageText(languageID, descriptionText)
                    }
                    talentsArray.add(
                        DDTalent(
                            talentID,
                            talentNew,
                            talentIDName,
                            talentMinLvl,
                            talentNames,
                            talentDescriptions,
                            PlayerStats(
                                talentStr,
                                talentDex,
                                talentCos,
                                talentInt,
                                talentSag,
                                talentCar
                            ),
                            talentSalTem,
                            talentSalRif,
                            talentSalVol,
                            talentTC,
                            talentCA
                        )
                    )
                } else
                    talentsArray.add(DDTalent(talentID, talentNew))
            }
            //inserimento in db
            val manualReceived =
                GameManual(
                    manualID, manualVersion.toFloat(), manualReleaseData,
                    manualUsablePoints,
                    PlayerStats(
                        manualBaseStr, manualBaseDex, manualBaseCos,
                        manualBaseInt, manualBaseSag, manualBaseCar
                    ),
                    manualNotes,
                    racesArray.toTypedArray(),
                    classesArray.toTypedArray(),
                    talentsArray.toTypedArray()
                )
            insertNewGameManual(manualReceived)
            newManuals += 1
        }
        return newManuals
    }
}
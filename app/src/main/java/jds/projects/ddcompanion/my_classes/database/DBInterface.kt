package jds.projects.ddcompanion.my_classes.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import jds.projects.ddcompanion.my_classes.dd_classes.*
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.CARISMA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.COSTITUZIONE
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.DESTREZZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.FORZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.INTELLIGENZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.SAGGEZZA
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GMLanguage
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GameManual
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerDDClass
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet
import jds.projects.ddcompanion.my_classes.utils.JDPreferences
import jds.projects.ddcompanion.my_classes.utils.MLanguageText

class DBInterface private constructor(private var context: Context) {
    private var db: DBManager = DBManager(context)
    private var _usedLanguage: Int = JDPreferences.getInstance(context).language.id

    companion object {
        @Volatile
        private var INSTANCE: DBInterface? = null

        @Synchronized
        fun getInstance(context: Context): DBInterface =
            INSTANCE ?: DBInterface(context).also { INSTANCE = it }
    }

    private fun readFromDB(sqlCommand: String): Cursor {
        return db.read(sqlCommand)
    }
    private fun writeIntoDB(table_name: String, records: ContentValues): Long {
        return db.write(table_name, records)
    }
    private fun updateIntoDB(
        tableName: String, value: ContentValues,
        whereClause: String, whereArgs: Array<String>
    ): Int {
        return db.update(tableName, value, whereClause, whereArgs)
    }


    //funzione per impostare il linguaggio in uso dall'app
    fun setUsedLanguage(usedLanguage: Int) {
        this._usedLanguage = usedLanguage
    }

    //funzioni usate sotto
    fun boolToInt(value: Boolean): Int {
        return if (value) 1 else 0
    }

    fun intToBool(value: Int): Boolean {
        return value > 0
    }

    fun closeDBConnection() {
        db.close()
    }

    private fun readGameManualBaseInfoFromCursor(cursor: Cursor): Collection<GameManual> {
        val items = mutableListOf<GameManual>()
        with(cursor) {
            while (moveToNext()) {
                val manualID = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableManuals.ID))
                val manualVersion =
                    getFloat(getColumnIndexOrThrow(DBManager.DBTables.TableManuals.VERSION))
                val manualDate =
                    getString(getColumnIndexOrThrow(DBManager.DBTables.TableManuals.DATE))
                items.add(
                    GameManual(
                        manualID,
                        manualVersion,
                        manualDate
                    )
                )
            }
        }
        return items
    }

    private fun readGameManualFullInfoFromCursor(cursor: Cursor): Collection<GameManual> {
        val items = mutableListOf<GameManual>()
        with(cursor) {
            while (moveToNext()) {
                val manualID = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableManuals.ID))
                val manualVersion =
                    getFloat(getColumnIndexOrThrow(DBManager.DBTables.TableManuals.VERSION))
                val manualDate =
                    getString(getColumnIndexOrThrow(DBManager.DBTables.TableManuals.DATE))
                val manualPoints =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableManuals.POINTS))
                val str = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableManuals.BASE_STR))
                val dex = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableManuals.BASE_DEX))
                val cos = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableManuals.BASE_COS))
                val int = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableManuals.BASE_INT))
                val sag = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableManuals.BASE_SAG))
                val car = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableManuals.BASE_CAR))
                val languageText =
                    MLanguageText()
                languageText.addMLanguageText(
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableManualsNotes.LANGUAGE)),
                    getString(getColumnIndexOrThrow(DBManager.DBTables.TableManualsNotes.NOTES))
                )
                items.add(
                    GameManual(
                        manualID,
                        manualVersion,
                        manualDate,
                        manualPoints,
                        PlayerStats(str, dex, cos, int, sag, car),
                        languageText
                    )
                )
            }
        }
        return items
    }

    private fun getPlayerSheetFromCursor(cursor: Cursor): Collection<PlayerSheet> {
        val playerSheets = mutableListOf<PlayerSheet>()
        val abilities = DDAbilityManager.getInstance(context).getAbilities()
        //carico la roba del player
        with(cursor) {
            while (moveToNext()) {
                val sheetID = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val manualID =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheets.ID_MANUAL))
                val manualVersion =
                    getFloat(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheets.VERSION_MANUAL))
                val playerHP =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheets.HP))
                val alignment =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheets.ID_ALIGNMENT))
                val raceID =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheets.ID_RACE))
                val mainClassID =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheets.ID_MAIN_CLASS))
                val levelUpFlag =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheets.CAN_LEVEL))
                val playerLevel =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheets.LEVEL))
                val playerName =
                    getString(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheets.PLAYER_NAME))
                val str =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheets.STAT_STR))
                val dex =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheets.STAT_DEX))
                val cos =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheets.STAT_COS))
                val int =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheets.STAT_INT))
                val sag =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheets.STAT_SAG))
                val car =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheets.STAT_CAR))
                val playerAbilities = abilities.toCollection(mutableListOf())
                val playerModifiedAbilities = getPlayerSheetAbilities(sheetID)
                //aggiorno i valori delle abilità che sono state modificate nel db
                for ((id, level) in playerModifiedAbilities)
                    playerAbilities[id].updateLevel(level)
                val playerSheetClasses = getPlayerSheetClasses(sheetID, manualID)
                playerSheets.add(
                    PlayerSheet(
                        sheetID, manualID, manualVersion, playerName,
                        alignment, playerLevel, playerHP,
                        PlayerStats(str, dex, cos, int, sag, car),
                        getDDRaceByID(raceID, manualID),
                        playerSheetClasses,
                        playerSheetClasses.getAll()
                            .find { pair -> pair.first.id == mainClassID }!!.first,
                        playerAbilities,
                        getPlayerSheetTalents(sheetID, manualID),
                        levelUpFlag
                    )
                )
            }
        }
        return playerSheets
    }

    fun getPlayerSheetClasses(sheetID: Int, manualID: Int): PlayerDDClass {
        val playerClasses =
            PlayerDDClass()
        val ddClassesID = mutableListOf<Int>()
        val levelClasses = mutableListOf<Int>()
        //mi trovo tutte le coppie classe/livello associate alla scheda sheetID
        var sqlCommand =
            "SELECT " + DBManager.DBTables.TablePlayerSheetsClasses.ID_CLASS + "," + DBManager.DBTables.TablePlayerSheetsClasses.LEVEL +
                    " FROM " + DBManager.DBTables.TablePlayerSheetsClasses.TABLE_NAME +
                    " WHERE " + DBManager.DBTables.TablePlayerSheetsClasses.ID_SHEET + " = " + sheetID
        var cursor = readFromDB(sqlCommand)
        with(cursor) {
            while (moveToNext()) {
                ddClassesID.add(getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheetsClasses.ID_CLASS)))
                levelClasses.add(getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheetsClasses.LEVEL)))
            }
            close()
        }
        //prendo tutte le informazioni delle classi trovate
        var inCommand = "("
        for (classID in ddClassesID)
            inCommand += "'$classID',"
        inCommand = inCommand.substring(0, inCommand.length - 1) + ")"
        sqlCommand = "SELECT *" +
                " FROM " + DBManager.DBTables.TableClasses.TABLE_NAME +
                " JOIN " + DBManager.DBTables.TableClassesDescription.TABLE_NAME + " ON " + DBManager.DBTables.TableClasses.ID_CLASS_NAME + " = " + DBManager.DBTables.TableClassesDescription.ID_CLASS +
                " JOIN " + DBManager.DBTables.TableManualClasses.TABLE_NAME + " ON " + DBManager.DBTables.TableClasses.ID + " = " + DBManager.DBTables.TableManualClasses.ID_CLASS +
                " WHERE " + DBManager.DBTables.TableManualClasses.ID_MANUAL + " = " + manualID + " AND " + DBManager.DBTables.TableClassesDescription.LANGUAGE + " = " + _usedLanguage + " AND " + DBManager.DBTables.TableClasses.ID + " IN " + inCommand
        cursor = readFromDB(sqlCommand)
        val ddClasses = getDDClassesFromCursor(cursor)
        cursor.close()
        playerClasses.loadAll(ddClasses.asList(), levelClasses)
        return playerClasses
    }


    fun getPlayerSheetAbilities(sheetID: Int): List<Pair<Int, Int>> {
        val playerAbilities = mutableListOf<Pair<Int, Int>>()
        //mi trovo tutte le coppie classe/livello associate alla scheda sheetID
        val sqlCommand =
            "SELECT " + DBManager.DBTables.TablePlayerSheetsAbilities.ID_ABILITY + "," + DBManager.DBTables.TablePlayerSheetsAbilities.LEVEL +
                    " FROM " + DBManager.DBTables.TablePlayerSheetsAbilities.TABLE_NAME +
                    " WHERE " + DBManager.DBTables.TablePlayerSheetsAbilities.ID_SHEET + " = " + sheetID
        val cursor = readFromDB(sqlCommand)
        with(cursor) {
            while (moveToNext()) {
                val abilityID =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheetsAbilities.ID_ABILITY))
                val abilityLevel =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheetsAbilities.LEVEL))
                playerAbilities.add(Pair(abilityID, abilityLevel))
            }
            close()
        }
        return playerAbilities
    }

    fun getPlayerSheetTalents(sheetID: Int, manualID: Int): MutableList<DDTalent> {
        val talentsID = mutableListOf<Int>()
        //mi trovo tutte le coppie classe/livello associate alla scheda sheetID
        var sqlCommand =
            "SELECT " + DBManager.DBTables.TablePlayerSheetsTalents.ID_TALENT +
                    " FROM " + DBManager.DBTables.TablePlayerSheetsTalents.TABLE_NAME +
                    " WHERE " + DBManager.DBTables.TablePlayerSheetsTalents.ID_SHEET + " = " + sheetID
        var cursor = readFromDB(sqlCommand)
        with(cursor) {
            while (moveToNext())
                talentsID.add(getInt(getColumnIndexOrThrow(DBManager.DBTables.TablePlayerSheetsTalents.ID_TALENT)))
            close()
        }
        //prendo tutte le informazioni delle classi trovate
        var inCommand = "( "
        for (talentID in talentsID)
            inCommand += "'$talentID',"
        inCommand = inCommand.substring(0, inCommand.length - 1) + ")"
        sqlCommand = "SELECT *" +
                " FROM " + DBManager.DBTables.TableTalents.TABLE_NAME +
                " JOIN " + DBManager.DBTables.TableTalentsDescription.TABLE_NAME + " ON " + DBManager.DBTables.TableTalents.ID_TALENT_NAME + " = " + DBManager.DBTables.TableTalentsDescription.ID_TALENT +
                " JOIN " + DBManager.DBTables.TableManualTalents.TABLE_NAME + " ON " + DBManager.DBTables.TableTalents.ID + " = " + DBManager.DBTables.TableManualTalents.ID_TALENT +
                " WHERE " + DBManager.DBTables.TableManualTalents.ID_MANUAL + " = " + manualID + " AND " + DBManager.DBTables.TableTalentsDescription.LANGUAGE + " = " + _usedLanguage + " AND " + DBManager.DBTables.TableTalents.ID + " IN " + inCommand
        cursor = readFromDB(sqlCommand)
        val ddClasses = getDDTalentsFromCursor(cursor)
        cursor.close()
        return ddClasses.toMutableList()
    }

    fun getGameManualsBaseInfo(): Collection<GameManual> {
        val sqlCommand =
            "SELECT " + DBManager.DBTables.TableManuals.ID + "," + DBManager.DBTables.TableManuals.VERSION + "," + DBManager.DBTables.TableManuals.DATE +
                    " FROM " + DBManager.DBTables.TableManuals.TABLE_NAME +
                    " ORDER BY " + DBManager.DBTables.TableManuals.ID +
                    " ASC"
        val cursor = readFromDB(sqlCommand)
        val items = readGameManualBaseInfoFromCursor(cursor)
        cursor.close()
        return items
    }

    fun getGameManualBaseInfo(manualID: Int): GameManual {
        val sqlCommand =
            "SELECT " + DBManager.DBTables.TableManuals.ID + "," + DBManager.DBTables.TableManuals.VERSION + "," + DBManager.DBTables.TableManuals.DATE +
                    " FROM " + DBManager.DBTables.TableManuals.TABLE_NAME +
                    " WHERE " + DBManager.DBTables.TableManuals.ID + " = " + manualID
        val cursor = readFromDB(sqlCommand)
        val items = readGameManualBaseInfoFromCursor(cursor)
        cursor.close()
        return items.elementAt(0)
    }

    fun getGameManualFullInfo(manualID: Int): GameManual {
        val sqlCommand =
            "SELECT * FROM " + DBManager.DBTables.TableManuals.TABLE_NAME +
                    " JOIN " + DBManager.DBTables.TableManualsNotes.TABLE_NAME + " ON " + DBManager.DBTables.TableManuals.ID + " = " + DBManager.DBTables.TableManualsNotes.ID +
                    " WHERE " + DBManager.DBTables.TableManuals.ID + " = " + manualID + " AND " + DBManager.DBTables.TableManualsNotes.LANGUAGE + " = " + _usedLanguage
        val cursor = readFromDB(sqlCommand)
        val manual = readGameManualFullInfoFromCursor(cursor).elementAt(0)
        cursor.close()
        val races = getGameManualRaces(manualID)
        val classes = getGameManualClasses(manualID)
        val talents = getGameManualTalents(manualID)
        manual.lateLoad(races, classes, talents)
        return manual
    }

    /**
     * Inserisce nel db il manuale, ritorna -1 se non è riuscito ad inserire, altrimenti l'id del manuale inserito
     */
    fun insertGameManual(manual: GameManual): Long {
        //creo una mappa dei valori da inserire
        val value = ContentValues().apply {
            put(DBManager.DBTables.TableManuals.ID, manual.manualID)
            put(DBManager.DBTables.TableManuals.VERSION, manual.manualVersion)
            put(DBManager.DBTables.TableManuals.DATE, manual.manualDate)
            put(DBManager.DBTables.TableManuals.POINTS, manual.manualUsablePoints)
            put(
                DBManager.DBTables.TableManuals.BASE_STR,
                manual.manualBaseStats.stats[FORZA]
            )
            put(
                DBManager.DBTables.TableManuals.BASE_DEX,
                manual.manualBaseStats.stats[DESTREZZA]
            )
            put(
                DBManager.DBTables.TableManuals.BASE_COS,
                manual.manualBaseStats.stats[COSTITUZIONE]
            )
            put(
                DBManager.DBTables.TableManuals.BASE_INT,
                manual.manualBaseStats.stats[INTELLIGENZA]
            )
            put(
                DBManager.DBTables.TableManuals.BASE_SAG,
                manual.manualBaseStats.stats[SAGGEZZA]
            )
            put(
                DBManager.DBTables.TableManuals.BASE_CAR,
                manual.manualBaseStats.stats[CARISMA]
            )
        }
        val idNewRecord = writeIntoDB(DBManager.DBTables.TableManuals.TABLE_NAME, value)
        //se è stato aggiunto correttamente finisco l'inserimento
        if (idNewRecord > 0) {
            //inserisco le patch notes se ci sono
            if (manual.manualNotes.hasText) {
                for (position in 0 until manual.manualNotes.size) {
                    //MI PRENDO LE NOTE ALLA POS POSITION E LA INSERISCO IN DB
                    val noteText = manual.manualNotes.get(position)
                    val noteValues = ContentValues().apply {
                        put(DBManager.DBTables.TableManualsNotes.ID, manual.manualID)
                        put(DBManager.DBTables.TableManualsNotes.LANGUAGE, noteText.first)
                        put(DBManager.DBTables.TableManualsNotes.NOTES, noteText.second)
                    }
                    writeIntoDB(DBManager.DBTables.TableManualsNotes.TABLE_NAME, noteValues)
                }
            }
            //inserisco le nuove razze se sono presenti
            if (manual.availableRaces.isNotEmpty()) {
                for (ddRace in manual.availableRaces) {
                    insertNewRace(manual.manualID, ddRace)
                }
            }
            //inserisco le nuove classi se sono presenti
            if (manual.availableClasses.isNotEmpty()) {
                for (ddClass in manual.availableClasses) {
                    insertNewClass(manual.manualID, ddClass)
                }
            }
            //inserisco le nuove classi se sono presenti
            if (manual.availableTalents.isNotEmpty()) {
                for (ddTalent in manual.availableTalents) {
                    insertNewTalent(manual.manualID, ddTalent)
                }
            }
        }
        return idNewRecord
    }

    private fun insertNewRace(manualID: Int, ddrace: DDRace): Long {
        val noteValues = ContentValues().apply {
            put(DBManager.DBTables.TableManualRaces.ID_MANUAL, manualID)
            put(DBManager.DBTables.TableManualRaces.ID_RACE, ddrace.id)
            put(DBManager.DBTables.TableManualRaces.NEW, boolToInt(ddrace.isNew))
        }
        val raceID = writeIntoDB(DBManager.DBTables.TableManualRaces.TABLE_NAME, noteValues)
        //controllo che l'inserimento sia avvenuto con successo
        if (raceID > 0) {
            //se è nuovo aggiungo anche i riferimenti ai suoi dati
            if (ddrace.isNew) {
                //inserisco valori della descrizione della razza
                if (ddrace.names.hasText) {
                    for (position in 0 until ddrace.names.size) {
                        val raceText = ddrace.names.get(position)
                        val descriptionValues = ContentValues().apply {
                            put(DBManager.DBTables.TableRacesDescription.ID_RACE, ddrace.id)
                            put(DBManager.DBTables.TableRacesDescription.LANGUAGE, raceText.first)
                            put(DBManager.DBTables.TableRacesDescription.RACE_NAME, raceText.second)
                        }
                        writeIntoDB(
                            DBManager.DBTables.TableRacesDescription.TABLE_NAME,
                            descriptionValues
                        )
                    }
                }
                //inserisco effettivamente la razza
                val raceValues = ContentValues().apply {
                    put(DBManager.DBTables.TableRaces.ID, ddrace.id)
                    put(DBManager.DBTables.TableRaces.ID_RACE_NAME, ddrace.nameID)
                    put(DBManager.DBTables.TableRaces.MOD_STR, ddrace.stats.stats[FORZA])
                    put(
                        DBManager.DBTables.TableRaces.MOD_DEX,
                        ddrace.stats.stats[DESTREZZA]
                    )
                    put(
                        DBManager.DBTables.TableRaces.MOD_COS,
                        ddrace.stats.stats[COSTITUZIONE]
                    )
                    put(
                        DBManager.DBTables.TableRaces.MOD_INT,
                        ddrace.stats.stats[INTELLIGENZA]
                    )
                    put(DBManager.DBTables.TableRaces.MOD_SAG, ddrace.stats.stats[SAGGEZZA])
                    put(DBManager.DBTables.TableRaces.MOD_CAR, ddrace.stats.stats[CARISMA])
                }
                writeIntoDB(DBManager.DBTables.TableRaces.TABLE_NAME, raceValues)
            }
        }
        return raceID
    }

    private fun insertNewClass(manualID: Int, ddClass: DDClass): Long {
        val noteValues = ContentValues().apply {
            put(DBManager.DBTables.TableManualClasses.ID_MANUAL, manualID)
            put(DBManager.DBTables.TableManualClasses.ID_CLASS, ddClass.id)
            put(DBManager.DBTables.TableManualClasses.NEW, boolToInt(ddClass.isNew))
        }

        val classID = writeIntoDB(DBManager.DBTables.TableManualClasses.TABLE_NAME, noteValues)
        //finisco l'inserimento solo se son riuscito ad inserire la nuova classe
        if (classID > 0) {
            //se è nuovo aggiungo anche i riferimenti ai suoi dati
            if (ddClass.isNew) {
                //inserisco valori della descrizione della classe
                if (ddClass.names.hasText) {
                    for (position in 0 until ddClass.names.size) {
                        val className = ddClass.names.get(position)
                        val descriptionValues = ContentValues().apply {
                            put(DBManager.DBTables.TableClassesDescription.ID_CLASS, ddClass.nameID)
                            put(
                                DBManager.DBTables.TableClassesDescription.LANGUAGE,
                                className.first
                            )
                            put(
                                DBManager.DBTables.TableClassesDescription.CLASS_NAME,
                                className.second
                            )
                        }
                        writeIntoDB(
                            DBManager.DBTables.TableClassesDescription.TABLE_NAME,
                            descriptionValues
                        )
                    }
                }
                //inserisco effettivamente la classe
                val classValues = ContentValues().apply {
                    put(DBManager.DBTables.TableClasses.ID, ddClass.id)
                    put(DBManager.DBTables.TableClasses.ID_CLASS_NAME, ddClass.nameID)
                    put(DBManager.DBTables.TableClasses.HITPOINTS_DICE, ddClass.hpDice)
                    put(DBManager.DBTables.TableClasses.HITPOINTS_DICE_NEXT, ddClass.hpDiceSucc)
                    put(DBManager.DBTables.TableClasses.MAIN_STAT, ddClass.mainModifier)
                    put(DBManager.DBTables.TableClasses.SAVE_1, ddClass.modifierSalv1)
                    put(DBManager.DBTables.TableClasses.SAVE_2, ddClass.modifierSalv2)
                    put(DBManager.DBTables.TableClasses.ABILITY_POINTS, ddClass.abilityPoints)
                    //allineamenti
                    put(
                        DBManager.DBTables.TableClasses.ALIGN_LB,
                        ddClass.allowedAlignments.alignments[DDAlignmentInfo.alg_lb]
                    )
                    put(
                        DBManager.DBTables.TableClasses.ALIGN_LN,
                        ddClass.allowedAlignments.alignments[DDAlignmentInfo.alg_ln]
                    )
                    put(
                        DBManager.DBTables.TableClasses.ALIGN_LM,
                        ddClass.allowedAlignments.alignments[DDAlignmentInfo.alg_lm]
                    )
                    put(
                        DBManager.DBTables.TableClasses.ALIGN_NB,
                        ddClass.allowedAlignments.alignments[DDAlignmentInfo.alg_nm]
                    )
                    put(
                        DBManager.DBTables.TableClasses.ALIGN_NN,
                        ddClass.allowedAlignments.alignments[DDAlignmentInfo.alg_nn]
                    )
                    put(
                        DBManager.DBTables.TableClasses.ALIGN_NM,
                        ddClass.allowedAlignments.alignments[DDAlignmentInfo.alg_nm]
                    )
                    put(
                        DBManager.DBTables.TableClasses.ALIGN_CB,
                        ddClass.allowedAlignments.alignments[DDAlignmentInfo.alg_cb]
                    )
                    put(
                        DBManager.DBTables.TableClasses.ALIGN_CN,
                        ddClass.allowedAlignments.alignments[DDAlignmentInfo.alg_cn]
                    )
                    put(
                        DBManager.DBTables.TableClasses.ALIGN_CM,
                        ddClass.allowedAlignments.alignments[DDAlignmentInfo.alg_cm]
                    )
                }
                writeIntoDB(DBManager.DBTables.TableClasses.TABLE_NAME, classValues)
            }
        }
        return classID
    }

    private fun insertNewTalent(manualID: Int, ddTalent: DDTalent): Long {
        val noteValues = ContentValues().apply {
            put(DBManager.DBTables.TableManualTalents.ID_MANUAL, manualID)
            put(DBManager.DBTables.TableManualTalents.ID_TALENT, ddTalent.id)
            put(DBManager.DBTables.TableManualTalents.NEW, boolToInt(ddTalent.isNew))
        }
        val talentID = writeIntoDB(DBManager.DBTables.TableManualTalents.TABLE_NAME, noteValues)
        //controllo che l'inserimento sia avvenuto con successo
        if (talentID > 0) {
            //se è nuovo aggiungo anche i riferimenti ai suoi dati
            if (ddTalent.isNew) {
                //inserisco I valori dei nomi del talento e sue descrizione
                if (ddTalent.names.hasText) {
                    for (position in 0 until ddTalent.names.size) {
                        val talentText = ddTalent.names.get(position)
                        val talentDescription = ddTalent.descriptions.get(position)
                        val descriptionValues = ContentValues().apply {
                            put(DBManager.DBTables.TableTalentsDescription.ID_TALENT, ddTalent.id)
                            put(
                                DBManager.DBTables.TableTalentsDescription.LANGUAGE,
                                talentText.first
                            )
                            put(
                                DBManager.DBTables.TableTalentsDescription.TALENT_NAME,
                                talentText.second
                            )
                            put(
                                DBManager.DBTables.TableTalentsDescription.TALENT_DESCRIPTION,
                                talentDescription.second
                            )
                        }
                        writeIntoDB(
                            DBManager.DBTables.TableTalentsDescription.TABLE_NAME,
                            descriptionValues
                        )
                    }
                }
                //inserisco effettivamente il talento
                val talentValues = ContentValues().apply {
                    put(DBManager.DBTables.TableTalents.ID, ddTalent.id)
                    put(DBManager.DBTables.TableTalents.ID_TALENT_NAME, ddTalent.nameID)
                    put(DBManager.DBTables.TableTalents.MIN_LVL, ddTalent.minLevel)
                    put(DBManager.DBTables.TableTalents.MOD_STR, ddTalent.stats.stats[FORZA])
                    put(
                        DBManager.DBTables.TableTalents.MOD_DEX,
                        ddTalent.stats.stats[DESTREZZA]
                    )
                    put(
                        DBManager.DBTables.TableTalents.MOD_COS,
                        ddTalent.stats.stats[COSTITUZIONE]
                    )
                    put(
                        DBManager.DBTables.TableTalents.MOD_INT,
                        ddTalent.stats.stats[INTELLIGENZA]
                    )
                    put(DBManager.DBTables.TableTalents.MOD_SAG, ddTalent.stats.stats[SAGGEZZA])
                    put(DBManager.DBTables.TableTalents.MOD_CAR, ddTalent.stats.stats[CARISMA])
                    //TIRI SALVEZZA
                    put(DBManager.DBTables.TableTalents.SAL_TEM, ddTalent.salvTempra)
                    put(DBManager.DBTables.TableTalents.SAL_RIF, ddTalent.salvRiflessi)
                    put(DBManager.DBTables.TableTalents.SAL_VOL, ddTalent.salvVolonta)
                    //TIRO COLPIRE + CLASSE ARMATURA
                    put(DBManager.DBTables.TableTalents.MOD_TC, ddTalent.modTC)
                    put(DBManager.DBTables.TableTalents.MOD_CA, ddTalent.modCA)
                }
                writeIntoDB(DBManager.DBTables.TableTalents.TABLE_NAME, talentValues)
            }
        }
        return talentID
    }

    fun getDDRaceByID(ddRaceID: Int, manualID: Int): DDRace {
        val sqlCommand =
            "SELECT * FROM " + DBManager.DBTables.TableManualRaces.TABLE_NAME +
                    " JOIN " + DBManager.DBTables.TableRaces.TABLE_NAME + " ON " + DBManager.DBTables.TableManualRaces.ID_RACE + " = " + DBManager.DBTables.TableRaces.ID +
                    " JOIN " + DBManager.DBTables.TableRacesDescription.TABLE_NAME + " ON " + DBManager.DBTables.TableRaces.ID_RACE_NAME + " = " + DBManager.DBTables.TableRacesDescription.ID_RACE +
                    " WHERE " + DBManager.DBTables.TableManualRaces.ID_MANUAL + " = " + manualID + " AND " + DBManager.DBTables.TableManualRaces.ID_RACE + " = " + ddRaceID + " AND " + DBManager.DBTables.TableRacesDescription.LANGUAGE + " = " + _usedLanguage
        val cursor = readFromDB(sqlCommand)
        val manualRaces = getDDRacesFromCursor(cursor)
        cursor.close()
        return manualRaces.elementAt(0)
    }

    fun getGameManualRaces(manualID: Int): Array<DDRace> {
        val sqlCommand =
            "SELECT * FROM " + DBManager.DBTables.TableManualRaces.TABLE_NAME +
                    " JOIN " + DBManager.DBTables.TableRaces.TABLE_NAME + " ON " + DBManager.DBTables.TableManualRaces.ID_RACE + " = " + DBManager.DBTables.TableRaces.ID +
                    " JOIN " + DBManager.DBTables.TableRacesDescription.TABLE_NAME + " ON " + DBManager.DBTables.TableRaces.ID_RACE_NAME + " = " + DBManager.DBTables.TableRacesDescription.ID_RACE +
                    " WHERE " + DBManager.DBTables.TableManualRaces.ID_MANUAL + " = " + manualID + " AND " + DBManager.DBTables.TableRacesDescription.LANGUAGE + " = " + _usedLanguage
        val cursor = readFromDB(sqlCommand)
        val manualRaces = getDDRacesFromCursor(cursor)
        cursor.close()
        return manualRaces
    }

    private fun getDDRacesFromCursor(cursor: Cursor): Array<DDRace> {
        val manualRaces = mutableListOf<DDRace>()
        with(cursor) {
            while (moveToNext()) {
                //LEGGO LE INFO BASE
                val raceID = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableRaces.ID))
                val raceNew = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableManualRaces.NEW))
                val raceNameID =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableRacesDescription.ID_RACE))
                //LEGGO LE STATS DI RAZZA
                val str = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableRaces.MOD_STR))
                val dex = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableRaces.MOD_DEX))
                val cos = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableRaces.MOD_COS))
                val int = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableRaces.MOD_INT))
                val sag = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableRaces.MOD_SAG))
                val car = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableRaces.MOD_CAR))
                val raceText =
                    MLanguageText()
                raceText.addMLanguageText(
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableRacesDescription.LANGUAGE)),
                    getString(getColumnIndexOrThrow(DBManager.DBTables.TableRacesDescription.RACE_NAME))
                )
                //CREO LA RAZZA
                manualRaces.add(
                    DDRace(
                        raceID,
                        intToBool(raceNew),
                        raceNameID,
                        raceText,
                        PlayerStats(str, dex, cos, int, sag, car)
                    )
                )
            }
        }
        return manualRaces.toTypedArray()
    }

    fun getDDClassByID(ddClassID: Int, manualID: Int): DDClass {
        val sqlCommand = "SELECT * FROM " + DBManager.DBTables.TableManualClasses.TABLE_NAME +
                " JOIN " + DBManager.DBTables.TableClasses.TABLE_NAME + " ON " + DBManager.DBTables.TableManualClasses.ID_CLASS + " = " + DBManager.DBTables.TableClasses.ID +
                " JOIN " + DBManager.DBTables.TableClassesDescription.TABLE_NAME + " ON " + DBManager.DBTables.TableClasses.ID_CLASS_NAME + " = " + DBManager.DBTables.TableClassesDescription.ID_CLASS +
                " WHERE " + DBManager.DBTables.TableManualClasses.ID_MANUAL + " = " + manualID + " AND " + DBManager.DBTables.TableManualClasses.ID_CLASS + " = " + ddClassID + " AND " + DBManager.DBTables.TableClassesDescription.LANGUAGE + " = " + _usedLanguage
        val cursor = readFromDB(sqlCommand)
        val manualClasses = getDDClassesFromCursor(cursor)
        cursor.close()
        return manualClasses.elementAt(0)
    }

    fun getGameManualClasses(manualID: Int): Array<DDClass> {
        val sqlCommand = "SELECT * FROM " + DBManager.DBTables.TableManualClasses.TABLE_NAME +
                " JOIN " + DBManager.DBTables.TableClasses.TABLE_NAME + " ON " + DBManager.DBTables.TableManualClasses.ID_CLASS + " = " + DBManager.DBTables.TableClasses.ID +
                " JOIN " + DBManager.DBTables.TableClassesDescription.TABLE_NAME + " ON " + DBManager.DBTables.TableClasses.ID_CLASS_NAME + " = " + DBManager.DBTables.TableClassesDescription.ID_CLASS +
                " WHERE " + DBManager.DBTables.TableManualClasses.ID_MANUAL + " = " + manualID + " AND " + DBManager.DBTables.TableClassesDescription.LANGUAGE + " = " + _usedLanguage
        val cursor = readFromDB(sqlCommand)
        val manualClasses = getDDClassesFromCursor(cursor)
        cursor.close()
        return manualClasses
    }

    private fun getDDClassesFromCursor(cursor: Cursor): Array<DDClass> {
        val manualClasses = mutableListOf<DDClass>()
        with(cursor) {
            while (moveToNext()) {
                //LEGGO LE INFORMAZIONI BASE
                val classID = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.ID))
                val classNew =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableManualClasses.NEW))
                val classNameID =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClassesDescription.ID_CLASS))

                val hpDice =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.HITPOINTS_DICE))
                val hpDiceSucc =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.HITPOINTS_DICE_NEXT))
                val mainModifier =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.MAIN_STAT))
                val modSalv1 =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.SAVE_1))
                val modSalv2 =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.SAVE_2))
                val abilityPoints =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.ABILITY_POINTS))
                //LEGGO GLI ALLINEAMENTI PERMESSI DALLA CLASSE
                val lb = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.ALIGN_LB))
                val ln = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.ALIGN_LN))
                val lm = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.ALIGN_LM))
                val nb = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.ALIGN_NB))
                val nn = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.ALIGN_NN))
                val nm = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.ALIGN_NM))
                val cb = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.ALIGN_CB))
                val cn = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.ALIGN_CN))
                val cm = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClasses.ALIGN_CM))
                val className =
                    MLanguageText()
                className.addMLanguageText(
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableClassesDescription.LANGUAGE)),
                    getString(getColumnIndexOrThrow(DBManager.DBTables.TableClassesDescription.CLASS_NAME))
                )
                //CREO LA CLASSE
                manualClasses.add(
                    DDClass(
                        classID,
                        intToBool(classNew),
                        classNameID,
                        className,
                        hpDice,
                        hpDiceSucc,
                        mainModifier,
                        modSalv1,
                        modSalv2,
                        abilityPoints,
                        DDAlignments(lb, ln, lm, nb, nn, nm, cb, cn, cm)
                    )
                )
            }
        }
        return manualClasses.toTypedArray()
    }

    fun getGameManualTalents(manualID: Int): Array<DDTalent> {
        val sqlCommand = "SELECT * FROM " + DBManager.DBTables.TableManualTalents.TABLE_NAME +
                " JOIN " + DBManager.DBTables.TableTalents.TABLE_NAME + " ON " + DBManager.DBTables.TableManualTalents.ID_TALENT + " = " + DBManager.DBTables.TableTalents.ID +
                " JOIN " + DBManager.DBTables.TableTalentsDescription.TABLE_NAME + " ON " + DBManager.DBTables.TableTalents.ID_TALENT_NAME + " = " + DBManager.DBTables.TableTalentsDescription.ID_TALENT +
                " WHERE " + DBManager.DBTables.TableManualTalents.ID_MANUAL + " = " + manualID + " AND " + DBManager.DBTables.TableTalentsDescription.LANGUAGE + " = " + _usedLanguage
        val cursor = readFromDB(sqlCommand)
        val manualTalents = getDDTalentsFromCursor(cursor)
        cursor.close()
        return manualTalents
    }

    private fun getDDTalentsFromCursor(cursor: Cursor): Array<DDTalent> {
        val manualTalents = mutableListOf<DDTalent>()
        with(cursor) {
            while (moveToNext()) {
                //LEGGO LE INFO BASE
                val talentID = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalents.ID))
                val talentNew =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableManualTalents.NEW))
                val talentNameID =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalentsDescription.ID_TALENT))
                val talentMinLvl =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalents.MIN_LVL))
                //LEGGO LE STATS DI RAZZA
                val str = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalents.MOD_STR))
                val dex = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalents.MOD_DEX))
                val cos = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalents.MOD_COS))
                val int = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalents.MOD_INT))
                val sag = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalents.MOD_SAG))
                val car = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalents.MOD_CAR))
                //mod salvezza
                val tempra = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalents.SAL_TEM))
                val riflessi =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalents.SAL_RIF))
                val volonta = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalents.SAL_VOL))
                val tColpire = getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalents.MOD_TC))
                val cArmatura =
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalents.MOD_CA))
                val talentText =
                    MLanguageText()
                talentText.addMLanguageText(
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalentsDescription.LANGUAGE)),
                    getString(getColumnIndexOrThrow(DBManager.DBTables.TableTalentsDescription.TALENT_NAME))
                )
                val talentDescription =
                    MLanguageText()
                talentDescription.addMLanguageText(
                    getInt(getColumnIndexOrThrow(DBManager.DBTables.TableTalentsDescription.LANGUAGE)),
                    getString(getColumnIndexOrThrow(DBManager.DBTables.TableTalentsDescription.TALENT_DESCRIPTION))
                )
                //CREO LA RAZZA
                manualTalents.add(
                    DDTalent(
                        talentID,
                        intToBool(talentNew),
                        talentNameID,
                        talentMinLvl,
                        talentText,
                        talentDescription,
                        PlayerStats(str, dex, cos, int, sag, car),
                        tempra,
                        riflessi,
                        volonta,
                        tColpire,
                        cArmatura
                    )
                )
            }
        }
        return manualTalents.toTypedArray()
    }

    fun getPlayerSheets(): Collection<PlayerSheet> {
        val sqlCommand = "SELECT * FROM " + DBManager.DBTables.TablePlayerSheets.TABLE_NAME
        val cursor = readFromDB(sqlCommand)
        val playerSheets = getPlayerSheetFromCursor(cursor)
        cursor.close()
        return playerSheets
    }

    fun getPlayerSheetByID(psID: Int): PlayerSheet {
        val sqlCommand = "SELECT * FROM " + DBManager.DBTables.TablePlayerSheets.TABLE_NAME +
                " WHERE " + BaseColumns._ID + " = " + psID
        val cursor = readFromDB(sqlCommand)
        val playerSheets = getPlayerSheetFromCursor(cursor)
        cursor.close()
        return playerSheets.elementAt(0)
    }

    fun insertNewPlayerSheet(playerSheet: PlayerSheet): Long {
        val psCValues = ContentValues().apply {
            put(DBManager.DBTables.TablePlayerSheets.ID_MANUAL, playerSheet.manualID)
            put(DBManager.DBTables.TablePlayerSheets.VERSION_MANUAL, playerSheet.manualVersion)
            put(DBManager.DBTables.TablePlayerSheets.PLAYER_NAME, playerSheet.playerName)
            put(DBManager.DBTables.TablePlayerSheets.LEVEL, playerSheet.playerLevel)
            put(DBManager.DBTables.TablePlayerSheets.HP, playerSheet.playerHP)
            put(DBManager.DBTables.TablePlayerSheets.ID_ALIGNMENT, playerSheet.playerAlignmentID)
            put(DBManager.DBTables.TablePlayerSheets.ID_RACE, playerSheet.playerRace.id)
            put(DBManager.DBTables.TablePlayerSheets.ID_MAIN_CLASS, playerSheet.playerMainClass.id)
            put(DBManager.DBTables.TablePlayerSheets.CAN_LEVEL, playerSheet.levelUpFlag)
            put(
                DBManager.DBTables.TablePlayerSheets.STAT_STR,
                playerSheet.playerStats.stats[FORZA]
            )
            put(
                DBManager.DBTables.TablePlayerSheets.STAT_DEX,
                playerSheet.playerStats.stats[DESTREZZA]
            )
            put(
                DBManager.DBTables.TablePlayerSheets.STAT_COS,
                playerSheet.playerStats.stats[COSTITUZIONE]
            )
            put(
                DBManager.DBTables.TablePlayerSheets.STAT_INT,
                playerSheet.playerStats.stats[INTELLIGENZA]
            )
            put(
                DBManager.DBTables.TablePlayerSheets.STAT_SAG,
                playerSheet.playerStats.stats[SAGGEZZA]
            )
            put(
                DBManager.DBTables.TablePlayerSheets.STAT_CAR,
                playerSheet.playerStats.stats[CARISMA]
            )
        }
        val playerSheetID = writeIntoDB(DBManager.DBTables.TablePlayerSheets.TABLE_NAME, psCValues)
        if (playerSheetID > 0) {
            for ((ddClass, level) in playerSheet.playerClasses.getAll()) {
                val psClassesValues = ContentValues().apply {
                    put(
                        DBManager.DBTables.TablePlayerSheetsClasses.ID_SHEET,
                        playerSheetID
                    )
                    put(
                        DBManager.DBTables.TablePlayerSheetsClasses.ID_CLASS,
                        ddClass.id
                    )
                    put(
                        DBManager.DBTables.TablePlayerSheetsClasses.LEVEL,
                        level
                    )
                }
                writeIntoDB(DBManager.DBTables.TablePlayerSheetsClasses.TABLE_NAME, psClassesValues)
            }

            for (ability in playerSheet.playerAbilities) {
                val psAbilitiesValues = ContentValues().apply {
                    put(
                        DBManager.DBTables.TablePlayerSheetsAbilities.ID_SHEET,
                        playerSheetID
                    )
                    put(
                        DBManager.DBTables.TablePlayerSheetsAbilities.ID_ABILITY,
                        ability.id
                    )
                    put(
                        DBManager.DBTables.TablePlayerSheetsAbilities.LEVEL,
                        ability.level
                    )
                }
                writeIntoDB(
                    DBManager.DBTables.TablePlayerSheetsAbilities.TABLE_NAME,
                    psAbilitiesValues
                )
            }

            for (talent in playerSheet.playerTalents) {
                val psTalentsValues = ContentValues().apply {
                    put(
                        DBManager.DBTables.TablePlayerSheetsTalents.ID_SHEET,
                        playerSheetID
                    )
                    put(
                        DBManager.DBTables.TablePlayerSheetsTalents.ID_TALENT,
                        talent.id
                    )
                }
                writeIntoDB(DBManager.DBTables.TablePlayerSheetsTalents.TABLE_NAME, psTalentsValues)
            }
        }
        return playerSheetID
    }

    fun deletePlayerSheet(playerSheetID: Int): Boolean {
        val sqlCommand = BaseColumns._ID + "=?"
        val result = db.delete(
            DBManager.DBTables.TablePlayerSheets.TABLE_NAME,
            sqlCommand,
            arrayOf(playerSheetID.toString())
        )
        db.close()
        return result != -1
    }

    fun getDDTalentByID(talentID: Int, manualID: Int): DDTalent {
        val sqlCommand = "SELECT * FROM " + DBManager.DBTables.TableManualTalents.TABLE_NAME +
                " JOIN " + DBManager.DBTables.TableTalents.TABLE_NAME + " ON " + DBManager.DBTables.TableManualTalents.ID_TALENT + " = " + DBManager.DBTables.TableTalents.ID +
                " JOIN " + DBManager.DBTables.TableTalentsDescription.TABLE_NAME + " ON " + DBManager.DBTables.TableTalents.ID_TALENT_NAME + " = " + DBManager.DBTables.TableTalentsDescription.ID_TALENT +
                " WHERE " + DBManager.DBTables.TableManualTalents.ID_MANUAL + " = " + manualID + " AND " + DBManager.DBTables.TableTalentsDescription.LANGUAGE + " = " + _usedLanguage + " AND " + DBManager.DBTables.TableTalents.ID + " = " + talentID
        val cursor = readFromDB(sqlCommand)
        val manualTalents = getDDTalentsFromCursor(cursor).elementAt(0)
        cursor.close()
        return manualTalents
    }

    fun insertNewGMLanguage(language: GMLanguage): Long {
        val languagesValues = ContentValues().apply {
            put(
                DBManager.DBTables.TableLanguages.ID_LANGUAGE,
                language.id
            )
            put(
                DBManager.DBTables.TableLanguages.LANGUAGE_NAME,
                language.name
            )
        }
        return writeIntoDB(DBManager.DBTables.TableLanguages.TABLE_NAME, languagesValues)
    }

    fun getGMLanguages(): MutableList<GMLanguage> {
        val sqlCommand = "SELECT * FROM " + DBManager.DBTables.TableLanguages.TABLE_NAME
        val cursor = readFromDB(sqlCommand)
        val manualLanguages = getGMLanguagesFromCursor(cursor)
        cursor.close()
        return manualLanguages
    }

    private fun getGMLanguagesFromCursor(cursor: Cursor): MutableList<GMLanguage> {
        val languages = mutableListOf<GMLanguage>()
        while (cursor.moveToNext()) {
            val languageID =
                cursor.getInt(cursor.getColumnIndexOrThrow(DBManager.DBTables.TableLanguages.ID_LANGUAGE))
            val languageName =
                cursor.getString(cursor.getColumnIndexOrThrow(DBManager.DBTables.TableLanguages.LANGUAGE_NAME))
            languages.add(
                GMLanguage(
                    languageID,
                    languageName
                )
            )
        }
        return languages
    }

    fun updatePlayerSheet(
        playerSheetID: Int,
        newLevel: Int,
        newHP: Int,
        newPlayerClass: DDClass,
        classLevel: Int,
        newPlayerStats: PlayerStats,
        oldPlayerAbilities: MutableList<DDAbility>,
        newPlayerAbilities: MutableList<DDAbility>,
        newPlayerTalents: MutableList<DDTalent>
    ): Int {
        //aggiorno le stats base del personaggio
        updatePlayerSheetStats(playerSheetID, newLevel, newHP, newPlayerStats, newPlayerClass)
        //se è nuova classe la aggiungo se no aggiorno il suo livello
        if (classLevel > 1)
            updatePlayerSheetClass(playerSheetID, newPlayerClass, classLevel)
        else
            addPlayerSheetClass(playerSheetID, newPlayerClass, classLevel)
        updatePlayerSheetAbilities(playerSheetID, oldPlayerAbilities, newPlayerAbilities)
        //se ci sono talenti li aggiorno/aggiungo
        if (newPlayerTalents.size > 0)
            addPlayerSheetTalents(playerSheetID, newPlayerTalents)
        return playerSheetID
    }

    private fun updatePlayerSheetAbilities(
        playerSheetID: Int,
        oldPlayerAbilities: MutableList<DDAbility>,
        newPlayerAbilities: MutableList<DDAbility>
    ) {
        //mi trovo le abilità in comune di cui è solo cambiato il livello
        val mutedAbilities =
            newPlayerAbilities.filter { ability -> oldPlayerAbilities.find { ability2 -> ability2.id == ability.id } != null }
        //mi trovo le abilità non in comune
        val newAbilities =
            newPlayerAbilities.filter { ability -> oldPlayerAbilities.find { ability2 -> ability2.id == ability.id } == null }
        //modifico tutte le abilità mutate
        for (ability in mutedAbilities) {
            val cvAbility = ContentValues().apply {
                put(
                    DBManager.DBTables.TablePlayerSheetsAbilities.LEVEL,
                    ability.level
                )
            }
            val whereClause =
                DBManager.DBTables.TablePlayerSheetsAbilities.ID_SHEET + " = ? AND " + DBManager.DBTables.TablePlayerSheetsAbilities.ID_ABILITY + " = ?"
            val whereArgs = arrayOf(playerSheetID.toString(), ability.id.toString())
            updateIntoDB(
                DBManager.DBTables.TablePlayerSheetsAbilities.TABLE_NAME,
                cvAbility,
                whereClause,
                whereArgs
            )
        }
        //aggiungo quelle nuove
        for (ability in newAbilities) {
            val cvAbility = ContentValues().apply {
                put(
                    DBManager.DBTables.TablePlayerSheetsAbilities.ID_SHEET,
                    playerSheetID
                )
                put(
                    DBManager.DBTables.TablePlayerSheetsAbilities.ID_ABILITY,
                    ability.id
                )
                put(
                    DBManager.DBTables.TablePlayerSheetsAbilities.LEVEL,
                    ability.level
                )
            }
            writeIntoDB(DBManager.DBTables.TablePlayerSheetsAbilities.TABLE_NAME, cvAbility)
        }
    }

    private fun updatePlayerSheetStats(
        playerSheetID: Int,
        newLevel: Int,
        newHP: Int,
        newStats: PlayerStats,
        mainClass: DDClass
    ) {
        val cvStats = ContentValues().apply {
            put(DBManager.DBTables.TablePlayerSheets.ID_MAIN_CLASS, mainClass.id)
            put(DBManager.DBTables.TablePlayerSheets.LEVEL, newLevel)
            put(DBManager.DBTables.TablePlayerSheets.HP, newHP)
            put(DBManager.DBTables.TablePlayerSheets.CAN_LEVEL, 0)
            put(DBManager.DBTables.TablePlayerSheets.STAT_STR, newStats.stats.getValue(FORZA))
            put(DBManager.DBTables.TablePlayerSheets.STAT_DEX, newStats.stats.getValue(DESTREZZA))
            put(
                DBManager.DBTables.TablePlayerSheets.STAT_COS,
                newStats.stats.getValue(COSTITUZIONE)
            )
            put(
                DBManager.DBTables.TablePlayerSheets.STAT_INT,
                newStats.stats.getValue(INTELLIGENZA)
            )
            put(DBManager.DBTables.TablePlayerSheets.STAT_SAG, newStats.stats.getValue(SAGGEZZA))
            put(DBManager.DBTables.TablePlayerSheets.STAT_CAR, newStats.stats.getValue(CARISMA))
        }
        val whereClause = BaseColumns._ID + " = ?"
        val whereArgs = arrayOf(playerSheetID.toString())
        updateIntoDB(
            DBManager.DBTables.TablePlayerSheets.TABLE_NAME,
            cvStats,
            whereClause,
            whereArgs
        )
    }

    private fun addPlayerSheetClass(playerSheetID: Int, newClass: DDClass, newLevel: Int) {
        val cvClass = ContentValues().apply {
            put(
                DBManager.DBTables.TablePlayerSheetsClasses.ID_SHEET,
                playerSheetID
            )
            put(
                DBManager.DBTables.TablePlayerSheetsClasses.ID_CLASS,
                newClass.id
            )
            put(
                DBManager.DBTables.TablePlayerSheetsClasses.LEVEL,
                newLevel
            )
        }
        writeIntoDB(DBManager.DBTables.TablePlayerSheetsClasses.TABLE_NAME, cvClass)
    }

    private fun updatePlayerSheetClass(playerSheetID: Int, newClass: DDClass, classLevel: Int) {
        val cvClass = ContentValues().apply {
            put(DBManager.DBTables.TablePlayerSheetsClasses.ID_SHEET, playerSheetID)
            put(DBManager.DBTables.TablePlayerSheetsClasses.ID_CLASS, newClass.id)
            put(DBManager.DBTables.TablePlayerSheetsClasses.LEVEL, classLevel)
        }
        val whereClause =
            DBManager.DBTables.TablePlayerSheetsClasses.ID_SHEET + " = ? AND " + DBManager.DBTables.TablePlayerSheetsClasses.ID_CLASS + " = ?"
        val whereArgs = arrayOf(playerSheetID.toString(), newClass.id.toString())
        updateIntoDB(
            DBManager.DBTables.TablePlayerSheetsClasses.TABLE_NAME,
            cvClass,
            whereClause,
            whereArgs
        )
    }

    private fun addPlayerSheetTalents(playerSheetID: Int, newTalents: MutableList<DDTalent>) {
        for (talent in newTalents) {
            val cvTalent = ContentValues().apply {
                put(
                    DBManager.DBTables.TablePlayerSheetsTalents.ID_SHEET,
                    playerSheetID
                )
                put(
                    DBManager.DBTables.TablePlayerSheetsTalents.ID_TALENT,
                    talent.id
                )
            }
            writeIntoDB(DBManager.DBTables.TablePlayerSheetsTalents.TABLE_NAME, cvTalent)
        }
    }

    fun needToInitDB(): Boolean {
        //RITORNO TRUE SE DEVO INIZIALIZZARE, SE NO TORNA FALSE
        var result = true
        val sqlCommand = "SELECT * FROM " + DBManager.DBTables.Checker.TABLE_NAME
        val cursor = readFromDB(sqlCommand)
        with(cursor) {
            if (moveToFirst()) {
                if (getInt(getColumnIndexOrThrow(DBManager.DBTables.Checker.DB_CREATED)) == 1)
                    result = false
            }
            close()
        }
        //SE DEVO INIZIALIZZARE, METTO 1 NELLA TABLE E AL PROSSIMO AVVIO NON DOVRò RICREARE NULLA
        if (result) {
            val cvInit = ContentValues().apply {
                put(DBManager.DBTables.Checker.DB_CREATED, 1)
            }
            writeIntoDB(DBManager.DBTables.Checker.TABLE_NAME, cvInit)
        }
        return result
    }

    fun getPlayerSheetsByGameManualID(manualID: Int): Collection<PlayerSheet> {
        val sqlCommand = "SELECT * FROM " + DBManager.DBTables.TablePlayerSheets.TABLE_NAME +
                " WHERE " + DBManager.DBTables.TablePlayerSheets.ID_MANUAL + " = " + manualID
        val cursor = readFromDB(sqlCommand)
        val playerSheets = getPlayerSheetFromCursor(cursor)
        cursor.close()
        return playerSheets
    }

    fun playerCanLevelUp(playerSheet: PlayerSheet): Int {
        with(playerSheet) {
            val cvStats = ContentValues().apply {
                put(DBManager.DBTables.TablePlayerSheets.CAN_LEVEL, 1)
            }
            val whereClause = BaseColumns._ID + " = ?"
            val whereArgs = arrayOf(playerSheetID.toString())
            return updateIntoDB(
                DBManager.DBTables.TablePlayerSheets.TABLE_NAME,
                cvStats,
                whereClause,
                whereArgs
            )
        }
    }
}
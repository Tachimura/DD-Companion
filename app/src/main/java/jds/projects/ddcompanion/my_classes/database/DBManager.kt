package jds.projects.ddcompanion.my_classes.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DBManager(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //database informations
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 7
        const val DATABASE_NAME = "JDDDCompanion.db"
    }

    //tabelle
    object DBTables {

        object Checker : BaseColumns {
            const val TABLE_NAME = "TABLE_CHECKER"

            const val DB_CREATED = "CHECKER_DB_CREATED"
        }

        object TableManuals : BaseColumns {
            const val TABLE_NAME = "TABLE_GAME_MANUALS"

            //colonne
            const val ID = "GM_ID"
            const val VERSION = "GM_VERSION"
            const val DATE = "GM_RELEASE_DATE"
            const val POINTS = "GM_USABLE_POINTS"
            const val BASE_STR = "GM_BASE_STR"
            const val BASE_DEX = "GM_BASE_DEX"
            const val BASE_COS = "GM_BASE_COS"
            const val BASE_INT = "GM_BASE_INT"
            const val BASE_SAG = "GM_BASE_SAG"
            const val BASE_CAR = "GM_BASE_CAR"
        }

        object TableManualsNotes : BaseColumns {
            const val TABLE_NAME = "TABLE_GAME_MANUALS_NOTES"

            //colonne
            const val ID = "GM_N_ID"
            const val LANGUAGE = "GM_N_LAN"
            const val NOTES = "GM_N_NOTES"
        }

        object TableManualRaces : BaseColumns {
            const val TABLE_NAME = "TABLE_GAME_MANUALS_RACES"

            //colonne
            const val ID_MANUAL = "GM_R_ID_MANUAL"
            const val ID_RACE = "GM_R_ID_RACE"
            const val NEW = "GM_R_NEW"
        }

        object TableRaces : BaseColumns {
            const val TABLE_NAME = "TABLE_RACES"

            //colonne
            const val ID = "R_ID"
            const val ID_RACE_NAME = "R_ID_NAME"

            //modificatori di statistica base
            const val MOD_STR = "R_MOD_STR"
            const val MOD_DEX = "R_MOD_DEX"
            const val MOD_COS = "R_MOD_COS"
            const val MOD_INT = "R_MOD_INT"
            const val MOD_SAG = "R_MOD_SAG"
            const val MOD_CAR = "R_MOD_CAR"
        }

        object TableRacesDescription : BaseColumns {
            const val TABLE_NAME = "TABLE_RACES_DESCRIPTION"

            //colonne
            const val ID_RACE = "RD_ID"
            const val LANGUAGE = "RD_LAN"
            const val RACE_NAME = "RD_NAME"
        }

        object TableManualClasses : BaseColumns {
            const val TABLE_NAME = "TABLE_GAME_MANUALS_CLASSES"

            //colonne
            const val ID_MANUAL = "GM_C_ID_MANUAL"
            const val ID_CLASS = "GM_C_ID_CLASS"
            const val NEW = "GM_C_NEW"
        }

        object TableClasses : BaseColumns {
            const val TABLE_NAME = "TABLE_CLASSES"

            //colonne
            const val ID = "C_ID"
            const val ID_CLASS_NAME = "C_ID_NAME"
            const val HITPOINTS_DICE = "C_HD"
            const val HITPOINTS_DICE_NEXT = "C_HD_NEXT"
            const val MAIN_STAT = "C_MAIN"
            const val SAVE_1 = "C_S1"
            const val SAVE_2 = "C_S2"
            const val ABILITY_POINTS = "C_ABILITY_POINTS"

            //allineamenti
            const val ALIGN_LB = "C_ALIGN_LB"
            const val ALIGN_LN = "C_ALIGN_LN"
            const val ALIGN_LM = "C_ALIGN_LM"
            const val ALIGN_NB = "C_ALIGN_NB"
            const val ALIGN_NN = "C_ALIGN_NN"
            const val ALIGN_NM = "C_ALIGN_NM"
            const val ALIGN_CB = "C_ALIGN_CB"
            const val ALIGN_CN = "C_ALIGN_CN"
            const val ALIGN_CM = "C_ALIGN_CM"
        }

        object TableClassesDescription : BaseColumns {
            const val TABLE_NAME = "TABLE_CLASSES_DESCRIPTION"

            //colonne
            const val ID_CLASS = "CD_ID"
            const val LANGUAGE = "CD_LAN"
            const val CLASS_NAME = "CD_NAME"
        }

        object TableManualTalents : BaseColumns {
            const val TABLE_NAME = "TABLE_GAME_MANUALS_TALENTS"

            //colonne
            const val ID_MANUAL = "GM_T_ID_MANUAL"
            const val ID_TALENT = "GM_T_ID_TALENT"
            const val NEW = "GM_T_NEW"
        }

        object TableTalents : BaseColumns {
            const val TABLE_NAME = "TABLE_TALENTS"

            //colonne
            const val ID = "T_ID"
            const val ID_TALENT_NAME = "T_ID_NAME"
            const val MIN_LVL = "T_MIN_LVL"

            //modificatori di statistica base
            const val MOD_STR = "T_MOD_STR"
            const val MOD_DEX = "T_MOD_DEX"
            const val MOD_COS = "T_MOD_COS"
            const val MOD_INT = "T_MOD_INT"
            const val MOD_SAG = "T_MOD_SAG"
            const val MOD_CAR = "T_MOD_CAR"

            //modificatori di statistica base
            const val SAL_TEM = "T_SAL_TEM"
            const val SAL_RIF = "T_SAL_RIF"
            const val SAL_VOL = "T_SAL_VOL"
            const val MOD_TC = "T_MOD_TC"
            const val MOD_CA = "T_MOD_CA"
        }

        object TableTalentsDescription : BaseColumns {
            const val TABLE_NAME = "TABLE_TALENTS_DESCRIPTION"

            //colonne
            const val ID_TALENT = "TD_ID"
            const val LANGUAGE = "TD_LAN"
            const val TALENT_NAME = "TD_NAME"
            const val TALENT_DESCRIPTION = "TD_DESCRIPTION"
        }

        object TablePlayerSheets : BaseColumns {
            const val TABLE_NAME = "TABLE_PLAYER_SHEETS"

            //colonne
            const val ID_MANUAL = "PS_MANUAL_ID"
            const val VERSION_MANUAL = "PS_MANUAL_VERSION"
            const val PLAYER_NAME = "PS_NAME"
            const val LEVEL = "PS_LEVEL"
            const val HP = "PS_HP"
            const val ID_ALIGNMENT = "PS_ALIGNMENT"
            const val ID_RACE = "PS_RACE"
            const val ID_MAIN_CLASS = "PS_MAIN_CLASS"
            const val STAT_STR = "PS_STAT_STR"
            const val STAT_DEX = "PS_STAT_DEX"
            const val STAT_COS = "PS_STAT_COS"
            const val STAT_INT = "PS_STAT_INT"
            const val STAT_SAG = "PS_STAT_SAG"
            const val STAT_CAR = "PS_STAT_CAR"
            const val CAN_LEVEL = "PS_CAN_LEVEL"
        }

        object TablePlayerSheetsClasses : BaseColumns {
            const val TABLE_NAME = "TABLE_PLAYER_SHEETS_CLASSES"

            //colonne
            const val ID_SHEET = "PSC_SHEET_ID"
            const val ID_CLASS = "PSC_CLASS_ID"
            const val LEVEL = "PSC_CLASS_LEVEL"
        }


        object TablePlayerSheetsAbilities : BaseColumns {
            const val TABLE_NAME = "TABLE_PLAYER_SHEETS_ABILITIES"

            //colonne
            const val ID_SHEET = "PSA_SHEET_ID"
            const val ID_ABILITY = "PSA_ABILITY_ID"
            const val LEVEL = "PSA_ABILITY_LEVEL"
        }

        object TablePlayerSheetsTalents : BaseColumns {
            const val TABLE_NAME = "TABLE_PLAYER_SHEETS_TALENTS"

            //colonne
            const val ID_SHEET = "PST_SHEET_ID"
            const val ID_TALENT = "PST_TALENT_ID"
        }

        object TableLanguages : BaseColumns {
            const val TABLE_NAME = "TABLE_GAME_MANUAL_LANGUAGES"

            //colonne
            const val ID_LANGUAGE = "LANG_LANGUAGE_ID"
            const val LANGUAGE_NAME = "LANG_LANGUAGE_NAME"
        }

    }

    override fun onCreate(db: SQLiteDatabase) {
        createTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        deleteTables(db)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    private fun createTables(db: SQLiteDatabase) {
        //CREO TABELLA CHECKER
        var sqlCommand = "CREATE TABLE ${DBTables.Checker.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.Checker.DB_CREATED} INTEGER)"
        db.execSQL(sqlCommand)

        //CREO TABELLA DB_MANUALS
        sqlCommand = "CREATE TABLE ${DBTables.TableManuals.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TableManuals.ID} INTEGER UNIQUE, " +
                "${DBTables.TableManuals.VERSION} FLOAT, " +
                "${DBTables.TableManuals.DATE} TEXT, " +
                "${DBTables.TableManuals.POINTS} INTEGER, " +
                "${DBTables.TableManuals.BASE_STR} INTEGER, " +
                "${DBTables.TableManuals.BASE_DEX} INTEGER, " +
                "${DBTables.TableManuals.BASE_COS} INTEGER, " +
                "${DBTables.TableManuals.BASE_INT} INTEGER, " +
                "${DBTables.TableManuals.BASE_SAG} INTEGER, " +
                "${DBTables.TableManuals.BASE_CAR} INTEGER)"
        db.execSQL(sqlCommand)

        //CREO TABELLA DB_NOTES
        sqlCommand = "CREATE TABLE ${DBTables.TableManualsNotes.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TableManualsNotes.ID} INTEGER, " +
                "${DBTables.TableManualsNotes.LANGUAGE} INTEGER, " +
                "${DBTables.TableManualsNotes.NOTES} TEXT, " +
                "UNIQUE(" + DBTables.TableManualsNotes.ID + "," + DBTables.TableManualsNotes.LANGUAGE + "))"
        db.execSQL(sqlCommand)

        //TABELLE X LE RAZZE
        //CREO TABELLA MANUAL_RACES
        sqlCommand = "CREATE TABLE ${DBTables.TableManualRaces.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TableManualRaces.ID_MANUAL} INTEGER, " +
                "${DBTables.TableManualRaces.ID_RACE} INTEGER, " +
                "${DBTables.TableManualRaces.NEW} INTEGER, " +
                "UNIQUE(" + DBTables.TableManualRaces.ID_MANUAL + "," + DBTables.TableManualRaces.ID_RACE + "))"
        db.execSQL(sqlCommand)

        //CREO TABELLA RACES
        sqlCommand = "CREATE TABLE ${DBTables.TableRaces.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TableRaces.ID} INTEGER UNIQUE, " +
                "${DBTables.TableRaces.ID_RACE_NAME} INTEGER, " +
                "${DBTables.TableRaces.MOD_STR} INTEGER, " +
                "${DBTables.TableRaces.MOD_DEX} INTEGER, " +
                "${DBTables.TableRaces.MOD_COS} INTEGER, " +
                "${DBTables.TableRaces.MOD_INT} INTEGER, " +
                "${DBTables.TableRaces.MOD_SAG} INTEGER, " +
                "${DBTables.TableRaces.MOD_CAR} INTEGER)"
        db.execSQL(sqlCommand)

        //CREO TABELLA RACES_DESCRIPTION
        sqlCommand = "CREATE TABLE ${DBTables.TableRacesDescription.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TableRacesDescription.ID_RACE} INTEGER, " +
                "${DBTables.TableRacesDescription.LANGUAGE} INTEGER, " +
                "${DBTables.TableRacesDescription.RACE_NAME} TEXT, " +
                "UNIQUE(" + DBTables.TableRacesDescription.ID_RACE + "," + DBTables.TableRacesDescription.LANGUAGE + "))"
        db.execSQL(sqlCommand)


        //TABELLE PER LE CLASSI
        //CREO TABELLA MANUAL_CLASSES
        sqlCommand = "CREATE TABLE ${DBTables.TableManualClasses.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TableManualClasses.ID_MANUAL} INTEGER, " +
                "${DBTables.TableManualClasses.ID_CLASS} INTEGER, " +
                "${DBTables.TableManualClasses.NEW} INTEGER, " +
                "UNIQUE(" + DBTables.TableManualClasses.ID_MANUAL + "," + DBTables.TableManualClasses.ID_CLASS + "))"
        db.execSQL(sqlCommand)

        //CREO TABELLA CLASSES
        sqlCommand = "CREATE TABLE ${DBTables.TableClasses.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TableClasses.ID} INTEGER UNIQUE, " +
                "${DBTables.TableClasses.ID_CLASS_NAME} INTEGER, " +
                "${DBTables.TableClasses.HITPOINTS_DICE} INTEGER, " +
                "${DBTables.TableClasses.HITPOINTS_DICE_NEXT} INTEGER, " +
                "${DBTables.TableClasses.MAIN_STAT} INTEGER, " +
                "${DBTables.TableClasses.SAVE_1} INTEGER, " +
                "${DBTables.TableClasses.SAVE_2} INTEGER, " +
                "${DBTables.TableClasses.ABILITY_POINTS} INTEGER, " +
                "${DBTables.TableClasses.ALIGN_LB} INTEGER, " +
                "${DBTables.TableClasses.ALIGN_LN} INTEGER, " +
                "${DBTables.TableClasses.ALIGN_LM} INTEGER, " +
                "${DBTables.TableClasses.ALIGN_NB} INTEGER, " +
                "${DBTables.TableClasses.ALIGN_NN} INTEGER, " +
                "${DBTables.TableClasses.ALIGN_NM} INTEGER, " +
                "${DBTables.TableClasses.ALIGN_CB} INTEGER, " +
                "${DBTables.TableClasses.ALIGN_CN} INTEGER, " +
                "${DBTables.TableClasses.ALIGN_CM} INTEGER)"
        db.execSQL(sqlCommand)

        //CREO TABELLA CLASSES_DESCRIPTION
        sqlCommand = "CREATE TABLE ${DBTables.TableClassesDescription.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TableClassesDescription.ID_CLASS} INTEGER, " +
                "${DBTables.TableClassesDescription.LANGUAGE} INTEGER, " +
                "${DBTables.TableClassesDescription.CLASS_NAME} TEXT, " +
                "UNIQUE(" + DBTables.TableClassesDescription.ID_CLASS + "," + DBTables.TableClassesDescription.LANGUAGE + "))"
        db.execSQL(sqlCommand)

        //TABELLE PER I TALENTI
        //CREO TABELLA MANUAL_TALENTS
        sqlCommand = "CREATE TABLE ${DBTables.TableManualTalents.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TableManualTalents.ID_MANUAL} INTEGER, " +
                "${DBTables.TableManualTalents.ID_TALENT} INTEGER, " +
                "${DBTables.TableManualTalents.NEW} INTEGER, " +
                "UNIQUE(" + DBTables.TableManualTalents.ID_MANUAL + "," + DBTables.TableManualTalents.ID_TALENT + "))"
        db.execSQL(sqlCommand)

        //CREO TABELLA TALENTS
        sqlCommand = "CREATE TABLE ${DBTables.TableTalents.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TableTalents.ID} INTEGER UNIQUE, " +
                "${DBTables.TableTalents.ID_TALENT_NAME} INTEGER, " +
                "${DBTables.TableTalents.MIN_LVL} INTEGER, " +
                "${DBTables.TableTalents.MOD_STR} INTEGER, " +
                "${DBTables.TableTalents.MOD_DEX} INTEGER, " +
                "${DBTables.TableTalents.MOD_COS} INTEGER, " +
                "${DBTables.TableTalents.MOD_INT} INTEGER, " +
                "${DBTables.TableTalents.MOD_SAG} INTEGER, " +
                "${DBTables.TableTalents.MOD_CAR} INTEGER, " +
                "${DBTables.TableTalents.SAL_TEM} INTEGER, " +
                "${DBTables.TableTalents.SAL_RIF} INTEGER, " +
                "${DBTables.TableTalents.SAL_VOL} INTEGER, " +
                "${DBTables.TableTalents.MOD_TC} INTEGER, " +
                "${DBTables.TableTalents.MOD_CA} INTEGER)"
        db.execSQL(sqlCommand)

        //CREO TABELLA CLASSES_DESCRIPTION
        sqlCommand = "CREATE TABLE ${DBTables.TableTalentsDescription.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TableTalentsDescription.ID_TALENT} INTEGER, " +
                "${DBTables.TableTalentsDescription.LANGUAGE} INTEGER, " +
                "${DBTables.TableTalentsDescription.TALENT_NAME} TEXT, " +
                "${DBTables.TableTalentsDescription.TALENT_DESCRIPTION} TEXT, " +
                "UNIQUE(" + DBTables.TableTalentsDescription.ID_TALENT + "," + DBTables.TableTalentsDescription.LANGUAGE + "))"
        db.execSQL(sqlCommand)

        //CREO TABELLA PLAYER_SHEETS
        sqlCommand = "CREATE TABLE ${DBTables.TablePlayerSheets.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TablePlayerSheets.ID_MANUAL} INTEGER, " +
                "${DBTables.TablePlayerSheets.VERSION_MANUAL} FLOAT, " +
                "${DBTables.TablePlayerSheets.LEVEL} INTEGER, " +
                "${DBTables.TablePlayerSheets.HP} INTEGER, " +
                "${DBTables.TablePlayerSheets.ID_ALIGNMENT} INTEGER, " +
                "${DBTables.TablePlayerSheets.ID_RACE} INTEGER, " +
                "${DBTables.TablePlayerSheets.ID_MAIN_CLASS} INTEGER, " +
                "${DBTables.TablePlayerSheets.PLAYER_NAME} TEXT, " +
                "${DBTables.TablePlayerSheets.STAT_STR} INTEGER, " +
                "${DBTables.TablePlayerSheets.STAT_DEX} INTEGER, " +
                "${DBTables.TablePlayerSheets.STAT_COS} INTEGER, " +
                "${DBTables.TablePlayerSheets.STAT_INT} INTEGER, " +
                "${DBTables.TablePlayerSheets.STAT_SAG} INTEGER, " +
                "${DBTables.TablePlayerSheets.STAT_CAR} INTEGER, " +
                "${DBTables.TablePlayerSheets.CAN_LEVEL} INTEGER)"
        db.execSQL(sqlCommand)

        //CREO TABELLA PLAYER_SHEETS CLASSES
        sqlCommand = "CREATE TABLE ${DBTables.TablePlayerSheetsClasses.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TablePlayerSheetsClasses.ID_SHEET} INTEGER, " +
                "${DBTables.TablePlayerSheetsClasses.ID_CLASS} INTEGER, " +
                "${DBTables.TablePlayerSheetsClasses.LEVEL} INTEGER, " +
                "UNIQUE(" + DBTables.TablePlayerSheetsClasses.ID_SHEET + "," + DBTables.TablePlayerSheetsClasses.ID_CLASS + "))"
        db.execSQL(sqlCommand)

        //CREO TABELLA PLAYER_SHEETS ABILITY
        sqlCommand = "CREATE TABLE ${DBTables.TablePlayerSheetsAbilities.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TablePlayerSheetsAbilities.ID_SHEET} INTEGER," +
                "${DBTables.TablePlayerSheetsAbilities.ID_ABILITY} INTEGER," +
                "${DBTables.TablePlayerSheetsAbilities.LEVEL} INTEGER, " +
                "UNIQUE(" + DBTables.TablePlayerSheetsAbilities.ID_SHEET + "," + DBTables.TablePlayerSheetsAbilities.ID_ABILITY + "))"
        db.execSQL(sqlCommand)

        //CREO TABELLA PLAYER_SHEETS TALENTS
        sqlCommand = "CREATE TABLE ${DBTables.TablePlayerSheetsTalents.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TablePlayerSheetsTalents.ID_SHEET} INTEGER, " +
                "${DBTables.TablePlayerSheetsTalents.ID_TALENT} INTEGER, " +
                "UNIQUE(" + DBTables.TablePlayerSheetsTalents.ID_SHEET + ", " + DBTables.TablePlayerSheetsTalents.ID_TALENT + "))"
        db.execSQL(sqlCommand)

        //CREO TABELLA LANGUAGES
        sqlCommand = "CREATE TABLE ${DBTables.TableLanguages.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${DBTables.TableLanguages.ID_LANGUAGE} INTEGER UNIQUE, " +
                "${DBTables.TableLanguages.LANGUAGE_NAME} TEXT)"
        db.execSQL(sqlCommand)
    }

    private fun deleteTables(db: SQLiteDatabase) {
        //rimuovo tabella CHECKER
        var sqlCommand = "DROP TABLE IF EXISTS ${DBTables.Checker.TABLE_NAME}"
        db.execSQL(sqlCommand)

        //rimuovo tabella DB_VERSION
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TableManuals.TABLE_NAME}"
        db.execSQL(sqlCommand)
        //rimuovo tabella DB_VERSION_NOTES
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TableManualsNotes.TABLE_NAME}"
        db.execSQL(sqlCommand)


        //rimuovo tabella MANUAL_RACES
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TableManualRaces.TABLE_NAME}"
        db.execSQL(sqlCommand)
        //rimuovo tabella MANUAL_CLASSES
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TableManualClasses.TABLE_NAME}"
        db.execSQL(sqlCommand)
        //rimuovo tabella MANUAL_TALENTS
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TableManualTalents.TABLE_NAME}"
        db.execSQL(sqlCommand)


        //rimuovo tabella RACES
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TableRaces.TABLE_NAME}"
        db.execSQL(sqlCommand)
        //rimuovo tabella RACES_DESCRIPTION
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TableRacesDescription.TABLE_NAME}"
        db.execSQL(sqlCommand)


        //rimuovo tabella CLASSES
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TableClasses.TABLE_NAME}"
        db.execSQL(sqlCommand)
        //rimuovo tabella CLASSES_DESCRIPTION
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TableClassesDescription.TABLE_NAME}"
        db.execSQL(sqlCommand)


        //rimuovo tabella TALENTS
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TableTalents.TABLE_NAME}"
        db.execSQL(sqlCommand)
        //rimuovo tabella TALENTS_DESCRIPTION
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TableTalentsDescription.TABLE_NAME}"
        db.execSQL(sqlCommand)


        //rimuovo tabella PLAYER_SHEETS
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TablePlayerSheets.TABLE_NAME}"
        db.execSQL(sqlCommand)
        //rimuovo tabella PLAYER_SHEETS CLASSES
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TablePlayerSheetsClasses.TABLE_NAME}"
        db.execSQL(sqlCommand)
        //rimuovo tabella PLAYER_SHEETS ABILITIES
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TablePlayerSheetsAbilities.TABLE_NAME}"
        db.execSQL(sqlCommand)
        //rimuovo tabella PLAYER_SHEETS TALENTS
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TablePlayerSheetsTalents.TABLE_NAME}"
        db.execSQL(sqlCommand)


        //rimuovo tabella PLAYER_SHEETS LANGUAGES
        sqlCommand = "DROP TABLE IF EXISTS ${DBTables.TableLanguages.TABLE_NAME}"
        db.execSQL(sqlCommand)
    }

    //METODI ESTERNI X ESEGUIRE OPERAZIONI DI READ E WRITE
    fun read(query: String): Cursor {
        return readableDatabase.rawQuery(query, null)
    }

    fun write(tableName: String, value: ContentValues): Long {
        return writableDatabase.insert(tableName, null, value)
    }

    fun delete(tableName: String, sqlCommand: String, idsArray: Array<String>): Int {
        return writableDatabase.delete(tableName, sqlCommand, idsArray)
    }

    fun update(
        tableName: String,
        value: ContentValues,
        whereClause: String,
        whereArgs: Array<String>
    ): Int {
        return writableDatabase.update(tableName, value, whereClause, whereArgs)
    }
}
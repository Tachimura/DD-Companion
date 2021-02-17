package jds.projects.ddcompanion.my_classes.io.files

import android.content.Context
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.dd_classes.DDAbilityManager
import jds.projects.ddcompanion.my_classes.dd_classes.DDTalent
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerDDClass
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetFileManager.CODE.ERROR_FILE_NOT_SUPPORTED
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetFileManager.CODE.ERROR_MANUAL_NOT_PRESENT
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetFileManager.CODE.SUCCESS
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.manualID
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.manualVersion
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerAbilities
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerAbilityID
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerAbilityLevel
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerAlignmentID
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerClassID
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerClassLevel
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerClasses
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerHP
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerLevel
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerLevelUpFlag
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerMainClass
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerName
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerRaceID
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerStats
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerTalentID
import jds.projects.ddcompanion.my_classes.io.files.PlayerSheetJSON.JSONKeys.playerTalents
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class PlayerSheetJSON(private var context: Context) {
    private var _json: JSONObject = JSONObject()
    val jsonResult: JSONObject
        get() = _json

    private var _playerSheet =
        PlayerSheet()
    val playerSheetResult: PlayerSheet
        get() = _playerSheet

    private var _lastManualIDRead: Int = 0
    val manualIDError: Int
        get() = _lastManualIDRead

    object JSONKeys {
        const val manualID = "ManualID"
        const val manualVersion = "ManualVersion"
        const val playerLevel = "Livello"
        const val playerName = "Nome"
        const val playerRaceID = "Razza"
        const val playerMainClass = "MainClass"
        const val playerClasses = "Classi"
        const val playerClassID = "ID"
        const val playerClassLevel = "Livello"
        const val playerHP = "HP"
        const val playerAlignmentID = "Allineamento"
        const val playerStats = "Statistiche"
        const val playerLevelUpFlag = "LevelUp"

        const val playerAbilities = "Abilities"
        const val playerAbilityID = "ID"
        const val playerAbilityLevel = "Livello"

        const val playerTalents = "Talents"
        const val playerTalentID = "ID"
    }

    fun makeJSON(playerSheet: PlayerSheet): Boolean {
        _json = JSONObject()
        val pStats = JSONArray()
        val pClasses = JSONArray()
        val pAbilities = JSONArray()
        val pTalents = JSONArray()
        try {
            //PLAYER BASE
            _json.put(manualID, playerSheet.manualID)
            _json.put(manualVersion, playerSheet.manualVersion)
            _json.put(playerLevel, playerSheet.playerLevel)
            _json.put(playerHP, playerSheet.playerHP)
            _json.put(playerAlignmentID, playerSheet.playerAlignmentID)
            _json.put(playerName, playerSheet.playerName)
            _json.put(playerRaceID, playerSheet.playerRace.id)
            _json.put(playerMainClass, playerSheet.playerMainClass.id)
            _json.put(playerLevelUpFlag, playerSheet.levelUpFlag)

            //PLAYER CLASSES
            for ((ddClass, level) in (playerSheet.playerClasses.getAll())) {
                val pClass = JSONObject()
                pClass.put(playerClassID, ddClass.id)
                pClass.put(playerClassLevel, level)
                pClasses.put(pClass)
            }
            _json.put(playerClasses, pClasses)

            //PLAYER STATS
            for (element in playerSheet.playerStats.stats)
                pStats.put(element.value)
            _json.put(playerStats, pStats)

            //ABILITIES
            for (ability in playerSheet.playerAbilities) {
                val abilityJSON = JSONObject()
                abilityJSON.put(playerAbilityID, ability.id)
                abilityJSON.put(playerAbilityLevel, ability.level)
                pAbilities.put(abilityJSON)
            }
            _json.put(playerAbilities, pAbilities)

            //TALENTS
            for (talent in playerSheet.playerTalents) {
                val talentJSON = JSONObject()
                talentJSON.put(playerTalentID, talent.id)
                pTalents.put(talentJSON)
            }
            _json.put(playerTalents, pTalents)
        } catch (e: JSONException) {
            return false
        }
        return true
    }

    /**
     * ritorna 1 se ok, -1 se il file non è supportato, -2 se il db locale non ha il manualID del playersheet
     */
    fun makePlayerSheet(playerJSON: JSONObject): Int {
        val result: Int
        result = try {
            val manualID = playerJSON.getInt(manualID)
            _lastManualIDRead = manualID
            DBInterface.getInstance(context)
                .getGameManualBaseInfo(manualID)//se non esiste tira la indexout exception

            val manualVersion = playerJSON.getDouble(manualVersion).toFloat()
            //PLAYER BASE
            val playerLevel = playerJSON.getInt(playerLevel)
            val playerHP = playerJSON.getInt(playerHP)
            val alignmentID = playerJSON.getInt(playerAlignmentID)
            val playerName = playerJSON.getString(playerName)
            val playerRace = DBInterface.getInstance(context)
                .getDDRaceByID(playerJSON.getInt(playerRaceID), manualID)
            val playerMainClassID = playerJSON.getInt(playerMainClass)
            val levelUpFlag = playerJSON.getInt(playerLevelUpFlag)

            //PLAYER STATS
            val mPlayerStats = PlayerStats()
            val playerStatsArray = playerJSON.getJSONArray(playerStats)
            for (cont in 0 until playerStatsArray.length())
                mPlayerStats.updateStat(cont, playerStatsArray.getInt(cont))

            //PLAYER CLASSES
            val mPlayerClasses =
                PlayerDDClass()
            val classesJSON = playerJSON.getJSONArray(playerClasses)
            for (cont in 0 until classesJSON.length()) {
                val classJSON = classesJSON.getJSONObject(cont)
                val ddClass = DBInterface.getInstance(context)
                    .getDDClassByID(classJSON.getInt(playerClassID), manualID)
                val ddClassLevel = classJSON.getInt(playerClassLevel)
                mPlayerClasses.load(ddClass, ddClassLevel)
            }
            val mainClass =
                mPlayerClasses.getAll().find { pair -> pair.first.id == playerMainClassID }!!.first
            //PLAYER ABILITIES
            val mPlayerAbilities = DDAbilityManager.getInstance(context).getAbilities()
            val abilitiesJSON = playerJSON.getJSONArray(playerAbilities)
            for (cont in 0 until abilitiesJSON.length()) {
                val abilityJSON = abilitiesJSON.getJSONObject(cont)
                val ddAbility = abilityJSON.getInt(playerAbilityID)
                val ddAbilityLevel = abilityJSON.getInt(playerAbilityLevel)
                mPlayerAbilities[ddAbility].updateLevel(ddAbilityLevel)
            }

            //PLAYER TALENTS
            val mPlayerTalents = mutableListOf<DDTalent>()
            val talentsJSON = playerJSON.getJSONArray(playerTalents)
            for (cont in 0 until talentsJSON.length()) {
                val talentJSON = talentsJSON.getJSONObject(cont)
                val ddTalent = DBInterface.getInstance(context)
                    .getDDTalentByID(talentJSON.getInt(playerTalentID), manualID)
                mPlayerTalents.add(ddTalent)
            }

            _playerSheet =
                PlayerSheet(
                    manualID,
                    manualVersion,
                    playerName,
                    alignmentID,
                    playerLevel,
                    playerHP,
                    mPlayerStats,
                    playerRace,
                    mPlayerClasses,
                    mainClass,
                    mPlayerAbilities,
                    mPlayerTalents,
                    levelUpFlag
                )
            SUCCESS
        } catch (e: JSONException) {
            ERROR_FILE_NOT_SUPPORTED
        } catch (e: IndexOutOfBoundsException) {
            ERROR_MANUAL_NOT_PRESENT
        }
        return result
    }

}
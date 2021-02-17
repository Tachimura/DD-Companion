package jds.projects.ddcompanion.activities.levelupplayersheet

import android.content.Context
import android.widget.Toast
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.activities.createplayersheet.fragments.abilityinfo.AbilityInformationFragment
import jds.projects.ddcompanion.activities.createplayersheet.fragments.classinfo.ClassInformationFragment
import jds.projects.ddcompanion.activities.createplayersheet.fragments.statsinfo.StatsInformationFragment
import jds.projects.ddcompanion.activities.createplayersheet.fragments.talentsinfo.TalentsInformationFragment
import jds.projects.ddcompanion.activities.levelupplayersheet.fragments.LevelUpPlayerSheetFragment
import jds.projects.ddcompanion.my_classes.dd_classes.DDAbility
import jds.projects.ddcompanion.my_classes.dd_classes.DDClass
import jds.projects.ddcompanion.my_classes.dd_classes.DDTalent
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.CARISMA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.COSTITUZIONE
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.DESTREZZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.FORZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.INTELLIGENZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.SAGGEZZA
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet
import jds.projects.ddcompanion.my_classes.utils.PlayerSheetViewModel

class LevelUpPlayerSheetActivityViewModel(context: Context, playerSheetID: Int) :
    PlayerSheetViewModel(context) {
    private var playerSheet: PlayerSheet = dbHelper.getPlayerSheetByID(playerSheetID)

    //OGGETTI PER GESTIONE FRAGMENTS + VIEWPAGER
    private object FRAGMENTS {
        const val BASE: Int = 0
        const val CLASS: Int = 1
        const val STATS: Int = 2
        const val ABILITY: Int = 3
        const val TALENTS: Int = 4
        const val END: Int = 5
    }

    private var levelUPType: Int = TYPE.NORMAL

    private object TYPE {
        const val NORMAL: Int = 0
        const val STATS: Int = 1
        const val TALENTS: Int = 2
        const val MIXED: Int = 3
    }

    fun getPlayerSheet(): PlayerSheet {
        return playerSheet
    }

    init {
        //INIT DELLE ROBE
        initPlayerStats(newStatsPoints)

        //CAMBIO IL TIPO DI LEVEL-UP DA FARE IN BASE AL LIVELLO DEL PG
        //OGNI 3 LIVELLI PRENDO UN TALENTO
        newLevel = playerSheet.playerLevel + 1
        if (newLevel % 3 == 0)
            levelUPType = TYPE.TALENTS
        //OGNI 4 LIVELLI PRENDO 1 PUNTO STATISTICA
        if (newLevel % 4 == 0) {
            levelUPType = if (levelUPType == TYPE.TALENTS)
            //OGNI %4 E %3 MI DA ENTRAMBI
                TYPE.MIXED
            else
                TYPE.STATS
        }

        //GESTIONE DEI NOMI DEI TABS
        setUpTabsTitles()
        //GESTIONE FRAGMENTS
        setUpFragments()
    }

    private fun setUpTabsTitles() {
        tabsTitles = mutableListOf()
        tabsTitles.add(context.getString(R.string.title_fragment_base_information))
        tabsTitles.add(context.getString(R.string.title_fragment_class_information))
        when (levelUPType) {
            TYPE.STATS -> {
                tabsTitles.add(context.getString(R.string.title_fragment_stats_information))
                tabsTitles.add(context.getString(R.string.title_fragment_ability_information))
            }

            TYPE.TALENTS -> {
                tabsTitles.add(context.getString(R.string.title_fragment_ability_information))
                tabsTitles.add(context.getString(R.string.title_fragment_talents_information))
            }

            TYPE.MIXED -> {
                tabsTitles.add(context.getString(R.string.title_fragment_stats_information))
                tabsTitles.add(context.getString(R.string.title_fragment_ability_information))
                tabsTitles.add(context.getString(R.string.title_fragment_talents_information))
            }
            TYPE.NORMAL -> {
                tabsTitles.add(context.getString(R.string.title_fragment_ability_information))
            }
        }
    }

    private fun setUpFragments() {
        fragmentList = mutableListOf()
        val baseInfoFragment = LevelUpPlayerSheetFragment()
        val classInfoFragment = ClassInformationFragment()
        var statsInfoFragment: StatsInformationFragment? = null
        val abilityInfoFragment = AbilityInformationFragment()
        var talentsInfoFragment: TalentsInformationFragment? = null

        //INSERISCO NELL'ORDINE CORRETTO I FRAGMENTS
        fragmentList.add(baseInfoFragment)
        fragmentList.add(classInfoFragment)
        when (levelUPType) {
            TYPE.NORMAL -> {
                fragmentList.add(abilityInfoFragment)
            }
            TYPE.STATS -> {
                statsInfoFragment = StatsInformationFragment()
                fragmentList.add(statsInfoFragment)
                fragmentList.add(abilityInfoFragment)
            }
            TYPE.TALENTS -> {
                talentsInfoFragment = TalentsInformationFragment()
                fragmentList.add(abilityInfoFragment)
                fragmentList.add(talentsInfoFragment)
            }
            TYPE.MIXED -> {
                statsInfoFragment = StatsInformationFragment()
                talentsInfoFragment = TalentsInformationFragment()
                fragmentList.add(statsInfoFragment)
                fragmentList.add(abilityInfoFragment)
                fragmentList.add(talentsInfoFragment)
            }
        }

        //SET-UP LISTENER
        baseInfoFragment.setOnLevelUpFragmentListener(object :
            LevelUpPlayerSheetFragment.OnLevelUpFragmentListener {
            override fun onFragmentReady() {
                baseInfoFragment.loadPlayerInfo(playerSheet)
            }
        })

        classInfoFragment.setOnClassInfoChangedListener(object :
            ClassInformationFragment.OnClassInfoChangedListener {
            override fun onClassSelected(classSelected: DDClass) {
                if (classSelected.id != newPlayerClass.id) {
                    reInitClass()
                    newPlayerClass = classSelected
                    flagModifyAbility = true
                    flagModifyTalents = true
                }
            }
        })

        statsInfoFragment?.setOnStatsInfoChangedListener(object :
            StatsInformationFragment.OnStatsInfoChangedListener {
            override fun onStatChanged(statChanged: Int, newValue: Int, remainingPoints: Int) {
                newPlayerStats.updateStat(statChanged, newValue)
                statsRemainingPoints = remainingPoints
            }

            override fun onStatInit(remainingPoints: Int) {
                statsRemainingPoints = remainingPoints
            }
        })

        abilityInfoFragment.setOnAbilityLevelChangedListener(object :
            AbilityInformationFragment.OnAbilityLevelChangedListener {
            override fun onAbilityLevelChanged(abilityChanged: DDAbility, remainingPoints: Int) {
                var found = false
                for (ability in newPlayerAbilities) {
                    if (ability.id == abilityChanged.id) {
                        found = true
                        break
                    }
                }
                if (!found)
                    newPlayerAbilities.add(abilityChanged)
                abilityRemainingPoints = remainingPoints
            }
        })

        talentsInfoFragment?.setOnTalentInfoChangedListener(object :
            TalentsInformationFragment.OnTalentInfoChangedListener {
            override fun onTalentSelected(talentSelected: DDTalent, remainingPoints: Int) {
                newPlayerTalents.add(talentSelected)
                talentsRemainingPoints = remainingPoints
            }

            override fun onTalentDeselected(talentSelected: DDTalent, remainingPoints: Int) {
                newPlayerTalents.remove(talentSelected)
                talentsRemainingPoints = remainingPoints
            }
        })
    }

    private fun reInitClass() {
        newPlayerAbilities.clear()
        if (newPlayerTalents.size > 0) {
            initPlayerTalents(newTalentsPoints)
            (fragmentList[FRAGMENTS.TALENTS] as TalentsInformationFragment).resetSelection()
        }
    }

    private fun initPlayerStats(remainingPoints: Int) {
        statsRemainingPoints = remainingPoints
        newPlayerStats = PlayerStats(
            playerSheet.playerStats.stats.getValue(FORZA),
            playerSheet.playerStats.stats.getValue(DESTREZZA),
            playerSheet.playerStats.stats.getValue(COSTITUZIONE),
            playerSheet.playerStats.stats.getValue(INTELLIGENZA),
            playerSheet.playerStats.stats.getValue(SAGGEZZA),
            playerSheet.playerStats.stats.getValue(CARISMA)
        )
    }

    private fun initPlayerTalents(remainingPoints: Int) {
        talentsRemainingPoints = remainingPoints
        newPlayerTalents.clear()
    }

    private fun initPlayerAbilities() {
        //punti abilità sono dati da (class.points + int.mod)
        abilityRemainingPoints =
            (newPlayerClass.abilityPoints + newPlayerStats.statModifier(INTELLIGENZA))
        newPlayerAbilities.clear()
    }

    private fun filteredGameManualTalents(gameManualTalents: Array<DDTalent>): Array<DDTalent> {
        //prendo solo i talenti ancora non acquisiti dall'utente
        return gameManualTalents.filter { talent -> playerSheet.playerTalents.find { talent2 -> talent2.id == talent.id } == null }
            .toTypedArray()
    }

    override fun updateView(newPosition: Int): Boolean {
        when (transformIntoFragmentPosition(newPosition)) {
            FRAGMENTS.BASE -> {
                if (flagModifyBase) {
                    flagModifyBase = false
                    (fragmentList[newPosition] as LevelUpPlayerSheetFragment).loadPlayerInfo(
                        playerSheet
                    )
                }
            }

            FRAGMENTS.CLASS -> {
                if (flagModifyClass) {
                    flagModifyClass = false
                    (fragmentList[newPosition] as ClassInformationFragment).loadClasses(
                        dbHelper.getGameManualClasses(playerSheet.manualID),
                        playerSheet.manualID,
                        playerSheet.playerAlignmentID
                    )
                }
            }

            FRAGMENTS.STATS -> {
                //SE HO SELEZIONATO UNA CLASSE
                if (newPlayerClass.id > 0) {
                    if (flagModifyStats) {
                        flagModifyStats = false
                        initPlayerStats(newStatsPoints)
                        (fragmentList[newPosition] as StatsInformationFragment).loadStats(
                            PlayerStats(
                                newPlayerStats.stats.getValue(FORZA),
                                newPlayerStats.stats.getValue(DESTREZZA),
                                newPlayerStats.stats.getValue(COSTITUZIONE),
                                newPlayerStats.stats.getValue(INTELLIGENZA),
                                newPlayerStats.stats.getValue(SAGGEZZA),
                                newPlayerStats.stats.getValue(CARISMA)
                            ), playerSheet.manualID,
                            statsRemainingPoints
                        )
                    }
                } else {
                    //INFORMO CHE DEVE SELEZIONARE LA CLASSE
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.create_player_sheet_error_missing_class),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            }

            FRAGMENTS.ABILITY -> {
                if (newPlayerClass.id > 0) {
                    if (levelUPType == TYPE.STATS || levelUPType == TYPE.MIXED)
                        if (statsRemainingPoints > 0) {
                            Toast.makeText(
                                context,
                                "$statsRemainingPoints " + context.resources.getString(R.string.create_player_sheet_error_stats_points_notused),
                                Toast.LENGTH_SHORT
                            ).show()
                            return false
                        }
                    //SE CI SONO STATE MODIFICHE AL MODIFICATORE DI INTELLIGENZA
                    if (newPlayerStats.statModifier(INTELLIGENZA) != oldIntModifier) {
                        oldIntModifier = newPlayerStats.statModifier(INTELLIGENZA)
                        flagModifyAbility = true
                    }
                    if (flagModifyAbility) {
                        flagModifyAbility = false
                        initPlayerAbilities()
                        (fragmentList[newPosition] as AbilityInformationFragment).loadAbilities(
                            playerSheet.playerAbilities.map { el -> el.level }.toMutableList(),
                            abilityRemainingPoints,
                            newPlayerClass.modifierSalv1,
                            newPlayerClass.modifierSalv2
                        )
                    }
                } else {
                    //INFORMO CHE MANCA LA CLASSE
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.create_player_sheet_error_missing_class),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            }

            FRAGMENTS.TALENTS -> {
                if (abilityRemainingPoints == 0) {
                    if (flagModifyTalents) {
                        flagModifyTalents = false
                        initPlayerTalents(newTalentsPoints)
                        val talents =
                            filteredGameManualTalents(dbHelper.getGameManualTalents(playerSheet.manualID))
                                .filter { talent -> talent.minLevel <= newLevel }
                                .toMutableList()
                        availableTalents = talents.size
                        (fragmentList[newPosition] as TalentsInformationFragment).loadTalents(
                            talents,
                            playerSheet.manualID,
                            talentsRemainingPoints
                        )
                    }
                } else {
                    Toast.makeText(
                        context,
                        "$abilityRemainingPoints " + context.resources.getString(R.string.create_player_sheet_error_ability_points_notused),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            }
            FRAGMENTS.END -> {
                if (levelUPType == TYPE.TALENTS || levelUPType == TYPE.MIXED) {
                    if (availableTalents > 0) {
                        var stampError = false
                        val margin = availableTalents - newTalentsPoints
                        if (margin > 0) {
                            if (talentsRemainingPoints > 0)
                                stampError = true
                        } else {
                            if (talentsRemainingPoints > -margin)
                                stampError = true
                        }
                        if (stampError) {
                            Toast.makeText(
                                context,
                                "$talentsRemainingPoints " + context.resources.getString(R.string.create_player_sheet_error_talent_points_notused),
                                Toast.LENGTH_SHORT
                            ).show()
                            return false
                        }
                    }
                } else {
                    if (abilityRemainingPoints > 0) {
                        Toast.makeText(
                            context,
                            "$abilityRemainingPoints " + context.resources.getString(R.string.create_player_sheet_error_ability_points_notused),
                            Toast.LENGTH_SHORT
                        ).show()
                        return false
                    }
                }
            }
        }
        //SE è OK AGGIORNO IL VALORE
        actualPosition = newPosition
        return true
    }

    private fun transformIntoFragmentPosition(position: Int): Int {
        return when (position) {
            FRAGMENTS.BASE -> FRAGMENTS.BASE
            FRAGMENTS.CLASS -> FRAGMENTS.CLASS
            FRAGMENTS.STATS -> {
                when (levelUPType) {
                    TYPE.MIXED -> FRAGMENTS.STATS
                    TYPE.NORMAL -> FRAGMENTS.ABILITY
                    TYPE.STATS -> FRAGMENTS.STATS
                    else -> FRAGMENTS.ABILITY
                }
            }
            FRAGMENTS.ABILITY -> {
                when (levelUPType) {
                    TYPE.MIXED -> FRAGMENTS.ABILITY
                    TYPE.NORMAL -> FRAGMENTS.END
                    TYPE.STATS -> FRAGMENTS.ABILITY
                    else -> FRAGMENTS.TALENTS
                }
            }
            FRAGMENTS.TALENTS -> {
                when (levelUPType) {
                    TYPE.MIXED -> FRAGMENTS.TALENTS
                    else -> FRAGMENTS.END
                }
            }
            else -> FRAGMENTS.END
        }
    }

    override fun savePlayerSheet(): Int {
        val classNotNew = playerSheet.playerClasses.contain(newPlayerClass)
        val classLevel = if (classNotNew) playerSheet.playerClasses.getAll()
            .find { pair -> pair.first.id == newPlayerClass.id }!!.second + 1 else 1
        val newHP = playerSheet.playerHP + playerSheet.playerStats.statModifier(COSTITUZIONE) +
                //se non è livello 1 prendo hp successivi
                if (classNotNew)
                    newPlayerClass.hpDiceSucc
                //altrimenti prendo hp base
                else
                    newPlayerClass.hpDice
        playerSheet.updatePlayerSheet(
            newHP,
            newPlayerClass,
            newPlayerStats,
            newPlayerAbilities,
            newPlayerTalents
        )
        return dbHelper.updatePlayerSheet(
            playerSheet.playerSheetID,
            newLevel,
            newHP,
            newPlayerClass,
            classLevel,
            newPlayerStats,
            playerSheet.playerAbilities.filter { ability -> ability.level > 0 }.toMutableList(),
            newPlayerAbilities,
            newPlayerTalents
        )
    }

    fun updatePlayerSheet(): Int {
        return savePlayerSheet()
    }
}
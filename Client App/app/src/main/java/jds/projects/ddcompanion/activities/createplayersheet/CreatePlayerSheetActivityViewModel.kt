package jds.projects.ddcompanion.activities.createplayersheet

import android.content.Context
import android.widget.Toast
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.activities.createplayersheet.fragments.abilityinfo.AbilityInformationFragment
import jds.projects.ddcompanion.activities.createplayersheet.fragments.baseinfo.BaseInformationsFragment
import jds.projects.ddcompanion.activities.createplayersheet.fragments.classinfo.ClassInformationFragment
import jds.projects.ddcompanion.activities.createplayersheet.fragments.raceinfo.RaceInformationFragment
import jds.projects.ddcompanion.activities.createplayersheet.fragments.statsinfo.StatsInformationFragment
import jds.projects.ddcompanion.activities.createplayersheet.fragments.talentsinfo.TalentsInformationFragment
import jds.projects.ddcompanion.my_classes.database.DBInterface
import jds.projects.ddcompanion.my_classes.dd_classes.*
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.CARISMA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.COSTITUZIONE
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.DESTREZZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.FORZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.INTELLIGENZA
import jds.projects.ddcompanion.my_classes.dd_classes.PlayerStats.PlayerStat.SAGGEZZA
import jds.projects.ddcompanion.my_classes.dd_classes.gamemanual.GameManual
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerDDClass
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet
import jds.projects.ddcompanion.my_classes.utils.PlayerSheetViewModel

class CreatePlayerSheetActivityViewModel(context: Context) : PlayerSheetViewModel(context) {
    private object FRAGMENTS {
        const val BASE = 0
        const val RACE = 1
        const val CLASS = 2
        const val STATS = 3
        const val ABILITIES = 4
        const val TALENTS = 5
        const val END = 6
    }

    //a parte il livello
    private var _playerName: String
    private var _manualSelected: GameManual
    private var _alignmentSelected = -1
    private var _raceSelected: DDRace

    val manual: GameManual
        get() = _manualSelected

    val ddRace: DDRace
        get() = _raceSelected

    val playerName: String
        get() = _playerName

    val ddClass: DDClass
        get() = newPlayerClass

    init {
        //INIT VARIABILI X CREAZIONE SCHEDA GIOCATORE
        _manualSelected =
            GameManual(
                -1,
                -1F,
                ""
            )
        initPlayerStats(newStatsPoints)
        _playerName = ""
        _raceSelected = DDRace(-1, false)

        //GESTIONE DEI NOMI DEI TABS
        setUpTabsTitles()
        //GESTIONE FRAGMENTS
        setUpFragments()
    }

    private fun initPlayerStats(remainingPoints: Int) {
        statsRemainingPoints = remainingPoints
        newPlayerStats = PlayerStats()
    }

    private fun initPlayerTalents(remainingPoints: Int) {
        talentsRemainingPoints = remainingPoints
        newPlayerTalents.clear()
    }

    private fun setUpTabsTitles() {
        tabsTitles = mutableListOf(
            context.getString(R.string.title_fragment_base_information),
            context.getString(R.string.title_fragment_race_information),
            context.getString(R.string.title_fragment_class_information),
            context.getString(R.string.title_fragment_stats_information),
            context.getString(R.string.title_fragment_ability_information),
            context.getString(R.string.title_fragment_talents_information)
        )
    }

    private fun setUpFragments() {
        fragmentList = mutableListOf()
        //INIT FRAGMENTS
        val baseInfoFragment = BaseInformationsFragment()
        val raceInfoFragment = RaceInformationFragment()
        val classInfoFragment = ClassInformationFragment()
        val statsInfoFragment = StatsInformationFragment()
        val abilityInfoFragment = AbilityInformationFragment()
        val talentsInfoFragment = TalentsInformationFragment()

        //INIT LISTENER DEI FRAGMENTS
        baseInfoFragment.setOnBaseInfoChangedListener(object :
            BaseInformationsFragment.OnBaseInfoChangedListener {
            override fun onPlayerNameChanged(playerName: String) {
                _playerName = playerName
            }

            override fun onManualSelected(manual: GameManual) {
                if (_manualSelected.manualID != manual.manualID) {
                    _manualSelected = manual
                    reInitFull()
                    flagModifyRace = true
                    flagModifyClass = true
                    flagModifyStats = true
                    flagModifyAbility = true
                    flagModifyTalents = true
                    //imposto le stats base a quelle base del manuale
                    for (stat in manual.manualBaseStats.stats)
                        newPlayerStats.updateStat(stat.key, stat.value)
                }
            }

            override fun onAlignmentSelected(alignmentID: Int) {
                if (_alignmentSelected != alignmentID) {
                    _alignmentSelected = alignmentID
                    reInitClass()
                    flagModifyClass = true
                }
            }
        })

        raceInfoFragment.setOnRaceInfoChangedListener(object :
            RaceInformationFragment.OnRaceInfoChangedListener {
            override fun onRaceSelected(raceSelected: DDRace) {
                if (_raceSelected.id != raceSelected.id) {
                    reInitFull()
                    _raceSelected = raceSelected
                    flagModifyClass = true
                    flagModifyStats = true
                }
            }
        })

        classInfoFragment.setOnClassInfoChangedListener(object :
            ClassInformationFragment.OnClassInfoChangedListener {
            override fun onClassSelected(classSelected: DDClass) {
                if (newPlayerClass.id != classSelected.id) {
                    reInitClass()
                    newPlayerClass = classSelected
                    flagModifyAbility = true
                    flagModifyTalents = true
                }
            }
        })

        statsInfoFragment.setOnStatsInfoChangedListener(object :
            StatsInformationFragment.OnStatsInfoChangedListener {
            override fun onStatInit(remainingPoints: Int) {
                statsRemainingPoints = remainingPoints
            }

            override fun onStatChanged(statChanged: Int, newValue: Int, remainingPoints: Int) {
                newPlayerStats.updateStat(statChanged, newValue)
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

        talentsInfoFragment.setOnTalentInfoChangedListener(object :
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

        //li aggiungo alla fragmentList
        fragmentList.add(baseInfoFragment)
        fragmentList.add(raceInfoFragment)
        fragmentList.add(classInfoFragment)
        fragmentList.add(statsInfoFragment)
        fragmentList.add(abilityInfoFragment)
        fragmentList.add(talentsInfoFragment)
    }

    private fun reInitFull() {
        if (_raceSelected.id != -1)
            _raceSelected = DDRace(-1, false)
        if (newPlayerClass.id != -1) {
            newPlayerClass = DDClass(-1, false)
            (fragmentList[FRAGMENTS.CLASS] as ClassInformationFragment).resetSelection()
        }
        reInitClass()
    }

    private fun reInitClass() {
        newPlayerAbilities.clear()
        if (newPlayerTalents.size > 0) {
            initPlayerTalents(newTalentsPoints)
            (fragmentList[FRAGMENTS.TALENTS] as TalentsInformationFragment).resetSelection()
        }
    }

    override fun updateView(newPosition: Int): Boolean {
        when (newPosition) {
            FRAGMENTS.BASE -> {
            }
            FRAGMENTS.RACE -> {
                if (_alignmentSelected >= 0) {
                    if (_playerName.isNotEmpty()) {
                        if (flagModifyRace) {
                            flagModifyRace = false
                            (fragmentList[newPosition] as RaceInformationFragment).loadRaces(
                                dbHelper.getGameManualRaces(manual.manualID).toMutableList(),
                                manual.manualID
                            )
                        }
                    } else {
                        Toast.makeText(
                            context,
                            context.resources.getString(R.string.create_player_sheet_error_missing_name),
                            Toast.LENGTH_SHORT
                        ).show()
                        return false
                    }
                } else {
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.create_player_sheet_error_missing_alignment),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            }
            FRAGMENTS.CLASS -> {
                if (_raceSelected.id > 0) {
                    if (flagModifyClass) {
                        flagModifyClass = false
                        (fragmentList[newPosition] as ClassInformationFragment).loadClasses(
                            manual.availableClasses,
                            manual.manualID,
                            _alignmentSelected
                        )
                    }
                } else {
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.create_player_sheet_error_missing_race),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            }
            FRAGMENTS.STATS -> {
                if (newPlayerClass.id > 0) {
                    //se ho modificato dei valori per cui i vecchi punti sono da reimpostare a 0 allora eseguo il setup
                    //in caso contrario i vecchi valori delle statistiche possono essere mantenuti
                    if (flagModifyStats) {
                        flagModifyStats = false
                        initPlayerStats(manual.manualUsablePoints)
                        (fragmentList[newPosition] as StatsInformationFragment).loadStats(
                            PlayerStats(
                                manual.manualBaseStats.stats.getValue(FORZA) + _raceSelected.stats.stats.getValue(
                                    FORZA
                                ),
                                manual.manualBaseStats.stats.getValue(DESTREZZA) + _raceSelected.stats.stats.getValue(
                                    DESTREZZA
                                ),
                                manual.manualBaseStats.stats.getValue(COSTITUZIONE) + _raceSelected.stats.stats.getValue(
                                    COSTITUZIONE
                                ),
                                manual.manualBaseStats.stats.getValue(INTELLIGENZA) + _raceSelected.stats.stats.getValue(
                                    INTELLIGENZA
                                ),
                                manual.manualBaseStats.stats.getValue(SAGGEZZA) + _raceSelected.stats.stats.getValue(
                                    SAGGEZZA
                                ),
                                manual.manualBaseStats.stats.getValue(CARISMA) + _raceSelected.stats.stats.getValue(
                                    CARISMA
                                )
                            ),
                            manual.manualID,
                            statsRemainingPoints
                        )
                    }
                } else {
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.create_player_sheet_error_missing_class),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            }
            FRAGMENTS.ABILITIES -> {
                if (statsRemainingPoints == 0) {
                    if (newPlayerStats.statModifier(INTELLIGENZA) != oldIntModifier) {
                        oldIntModifier = newPlayerStats.statModifier(INTELLIGENZA)
                        flagModifyAbility = true
                    }
                    if (flagModifyAbility) {
                        flagModifyAbility = false
                        initPlayerAbilities()
                        //I PUNTI INIZIALI SONO DATI DA (MODIFICATORE_CLASSE + MODIFICATORE_INTELLIGENZA) * 4
                        (fragmentList[newPosition] as AbilityInformationFragment).loadAbilities(
                            mutableListOf(),
                            abilityRemainingPoints,
                            newPlayerClass.modifierSalv1, newPlayerClass.modifierSalv2
                        )
                    }
                } else {
                    Toast.makeText(
                        context,
                        "$statsRemainingPoints " + context.resources.getString(R.string.create_player_sheet_error_stats_points_notused),
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
                            manual.availableTalents.filter { talent -> talent.minLevel <= newLevel }
                                .toMutableList()
                        availableTalents = talents.size
                        (fragmentList[newPosition] as TalentsInformationFragment).loadTalents(
                            talents,
                            manual.manualID,
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
            }
        }
        //AGGIORNO LA POSIZIONE ATTUALE
        actualPosition = newPosition
        return true
    }

    private fun initPlayerAbilities() {
        //punti abilità iniziali sono dati da (class.points + int.mod)*4
        abilityRemainingPoints =
            ((newPlayerClass.abilityPoints + newPlayerStats.statModifier(INTELLIGENZA)) * 4)
        newPlayerAbilities.clear()
    }

    override fun savePlayerSheet(): Int {
        val playerClasses =
            PlayerDDClass()
        playerClasses.load(newPlayerClass, 1)
        return DBInterface.getInstance(context).insertNewPlayerSheet(
            PlayerSheet(
                manual.manualID,
                manual.manualVersion,
                playerName,
                _alignmentSelected,
                newLevel,
                (newPlayerClass.hpDice + newPlayerStats.statModifier(COSTITUZIONE)),
                newPlayerStats,
                ddRace,
                playerClasses,
                newPlayerClass,
                newPlayerAbilities,
                newPlayerTalents,
                //TODO METTERE LA FLAG A 0 SE NO PUò SUBITO LIVELLARE, è A 1 SOLO X TESTING
                1
            )
        ).toInt()
    }

    fun createPlayerSheet(): Int {
        return savePlayerSheet()
    }
}
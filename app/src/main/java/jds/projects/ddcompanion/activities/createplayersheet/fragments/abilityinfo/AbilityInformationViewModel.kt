package jds.projects.ddcompanion.activities.createplayersheet.fragments.abilityinfo

import android.content.Context
import jds.projects.ddcompanion.my_classes.adapters.AbilityAdapter
import jds.projects.ddcompanion.my_classes.dd_classes.DDAbility
import jds.projects.ddcompanion.my_classes.dd_classes.DDAbilityManager

class AbilityInformationViewModel(private var context: Context) {
    private lateinit var _abilityAdapter: AbilityAdapter
    private lateinit var _abilities: MutableList<DDAbility>
    private lateinit var _abilitiesMinLevel: MutableList<Int>

    val adapter: AbilityAdapter
        get() = _abilityAdapter

    private fun setUpAbilities(abilities: MutableList<Int>) {
        if (abilities.size != _abilities.size)
            initAbilitiesToZero()
        else
            for (cont in 0 until abilities.size) {
                _abilitiesMinLevel[cont] = abilities[cont]
                _abilities[cont].updateLevel(abilities[cont])
            }
    }

    private fun initAbilitiesToZero() {
        _abilitiesMinLevel.clear()
        for (cont in 0 until _abilities.size) {
            _abilitiesMinLevel.add(0)
            _abilities[cont].updateLevel(0)
        }
    }

    fun updateAdapter(
        abilitiesMinLevel: MutableList<Int>,
        useablePoints: Int,
        playerModifier1: Int,
        playerModifier2: Int
    ) {
        setUpAbilities(abilitiesMinLevel)
        _abilityAdapter.updatePlayerData(useablePoints, playerModifier1, playerModifier2)
        _abilityAdapter.notifyDataSetChanged()
    }

    fun createAdapter(): AbilityAdapter {
        _abilities = DDAbilityManager.getInstance(context).getAbilities()
        _abilitiesMinLevel = mutableListOf()
        initAbilitiesToZero()
        _abilityAdapter = AbilityAdapter(_abilities, 0, _abilitiesMinLevel, -1, -1)
        return _abilityAdapter
    }
}
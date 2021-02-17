package jds.projects.ddcompanion.my_classes.dd_classes

import android.content.Context
import jds.projects.ddcompanion.R

class DDAbilityManager private constructor(private var context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: DDAbilityManager? = null

        @Synchronized
        fun getInstance(context: Context): DDAbilityManager =
            INSTANCE ?: DDAbilityManager(context).also { INSTANCE = it }
    }

    fun getAbilities(): MutableList<DDAbility> {
        val abilities = mutableListOf<DDAbility>()
        //load delle abilità se non sono presenti
        val abilitiesResource: Array<String> =
            context.resources.getStringArray(R.array.abilities)
        val abilitiesModifierResource: Array<Int> =
            context.resources.getIntArray(R.array.abilities_modifier).toTypedArray()
        var cont = 0
        //load delle risorse e dato che è la creazione del pg, imposto il livello ed il livello minimo a 0
        for ((ability, modifier) in abilitiesResource zip abilitiesModifierResource) {
            abilities.add(DDAbility(cont, ability, modifier, 0))
            cont += 1
        }
        return abilities
    }
}
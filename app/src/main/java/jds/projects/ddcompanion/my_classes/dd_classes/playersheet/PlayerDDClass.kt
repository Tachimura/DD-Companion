package jds.projects.ddcompanion.my_classes.dd_classes.playersheet

import jds.projects.ddcompanion.my_classes.dd_classes.DDClass

class PlayerDDClass {
    private var classes: MutableList<DDClass> = mutableListOf()
    private var levels: MutableList<Int> = mutableListOf()

    fun levelUp(ddClassToLevel: DDClass): Boolean {
        val ddClass = classes.find { ddClass -> ddClass.id == ddClassToLevel.id }
        //se ho trovato qualcosa alzo il suo livello
        if (ddClass != null) {
            levels[classes.indexOf(ddClass)] += 1
            return true
        }
        //altrimenti ritorno falso
        return false
    }

    fun load(ddClass: DDClass, level: Int) {
        this.classes.add(ddClass)
        this.levels.add(level)
    }

    fun loadAll(classes: List<DDClass>, levels: List<Int>): Boolean {
        if (classes.size != levels.size)
            return false
        for ((ddClass, level) in (classes zip levels)) {
            this.classes.add(ddClass)
            this.levels.add(level)
        }
        return true
    }

    fun getAll(): List<Pair<DDClass, Int>> {
        return classes zip levels
    }

    fun contain(newPlayerClass: DDClass): Boolean {
        return classes.find { ddClass -> ddClass.id == newPlayerClass.id } != null
    }
}
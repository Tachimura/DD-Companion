package jds.projects.ddcompanion.my_classes.dd_classes

class DDAbility(
    private var _id: Int,
    private var _name: String,
    private var _modifier: Int,
    private var _level: Int
) {
    val id: Int
        get() = _id

    val name: String
        get() = _name

    val modifier: Int
        get() = _modifier

    val level: Int
        get() = _level

    fun updateLevel(newLevel: Int) {
        this._level = newLevel
    }
}
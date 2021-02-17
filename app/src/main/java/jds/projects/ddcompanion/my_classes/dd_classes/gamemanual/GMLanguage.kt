package jds.projects.ddcompanion.my_classes.dd_classes.gamemanual

class GMLanguage(private val _id: Int, private val _name: String) {
    val id: Int
        get() = _id

    val name: String
        get() = _name
}
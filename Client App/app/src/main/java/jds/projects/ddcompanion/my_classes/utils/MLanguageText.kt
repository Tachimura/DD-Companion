package jds.projects.ddcompanion.my_classes.utils

class MLanguageText {
    private var languageIDs = mutableListOf<Int>()
    private var texts = mutableListOf<String>()

    val hasText: Boolean
        get() = texts.size > 0

    val size: Int
        get() = texts.size

    fun addMLanguageText(languageID: Int, note: String) {
        languageIDs.add(languageID)
        texts.add(note)
    }

    fun getLanguageIDAtPosition(position: Int): Int {
        return if (position >= 0 && languageIDs.size >= position)
            languageIDs[position]
        else
            -1
    }

    fun getTextAtPosition(position: Int): String {
        return if (position >= 0 && texts.size >= position)
            texts[position]
        else
            ""
    }

    fun getTextWithLanguageID(languageID: Int): String {
        return getTextAtPosition(languageIDs.indexOf(languageID))
    }

    fun get(position: Int): Pair<Int, String> {
        return Pair(getLanguageIDAtPosition(position), getTextAtPosition(position))
    }
}
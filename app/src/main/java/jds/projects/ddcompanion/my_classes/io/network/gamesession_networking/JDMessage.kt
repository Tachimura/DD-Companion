package jds.projects.ddcompanion.my_classes.io.network.gamesession_networking

import java.io.Serializable

class JDMessage : Serializable {
    var requestID: Int = -1

    var message: String = String()
    var address: String = String()

    //risultato tiro del dado
    var throwDice: Int = 0

    //risultato con le variabili del tiro
    var throwResult: Int = 0

    var ability: Int = 0
    var punteggioBase: Int = 0
    var punteggioBonus: Int = 0

    //difficoltà del tiro inserita dal master
    var difficulty: Int = 0

    //tipo tiro salvezza
    var savingType: Int = 0
}
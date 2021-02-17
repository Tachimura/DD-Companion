package jds.projects.ddcompanion.my_classes.io.network.gamesession_networking

class JDMessageManager {
    object TYPE {
        const val REQUEST_CONNECTION = 1
        const val REQUEST_DISCONNECTION = 2
        const val REQUEST_GAME_MANUAL = 3
        const val REQUEST_THROW_TC = 4
        const val REQUEST_THROW_ABILITY = 5
        const val REQUEST_THROW_CA = 6
        const val REQUEST_THROW_SAVING = 7
        const val REQUEST_LEVEL_UP = 8
    }

    object VALUES {
        const val SUCCESS = "OK"
        const val FAILURE = "NO"
    }

    object SAVES {
        const val FORTITUDE = 0
        const val REFLEXES = 1
        const val WILL = 2
    }

    companion object {
        //RICHIESTA CONNESSIONE SESSIONE DI GIOCO
        fun requestToMasterConnection(address: String, name: String): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_CONNECTION
                this.address = address
                message = name
            }
        }

        fun replyToPlayerConnection(accept: Boolean): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_CONNECTION
                message = if (accept)
                    VALUES.SUCCESS
                else
                    VALUES.FAILURE
            }
        }

        //RICHIESTA DISCONNESSIONE SESSIONE DI GIOCO
        fun requestDisconnection(address: String): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_DISCONNECTION
                this.address = address
            }
        }

        fun replyDisconnection(): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_DISCONNECTION
            }
        }

        //RICHIESTA MANUALE SESSIONE DI GIOCO
        fun requestToMasterSessionManual(address: String): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_GAME_MANUAL
                this.address = address
            }
        }

        fun replyToPlayerSessionManual(manualID: Int): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_GAME_MANUAL
                message = manualID.toString()
            }
        }

        //RICHIESTA TIRO PER COLPIRE
        fun requestToMasterThrowTC(address: String, base: Int, bonus: Int): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_THROW_TC
                this.address = address
                punteggioBase = base
                punteggioBonus = bonus
            }
        }

        fun replyToPlayerThrowTC(
            dice: Int,
            result: Int,
            base: Int,
            bonus: Int,
            difficulty: Int
        ): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_THROW_TC
                throwDice = dice
                throwResult = result
                this.punteggioBase = base
                this.punteggioBonus = bonus
                this.difficulty = difficulty
            }
        }

        fun requestToPlayerThrowTC(difficulty: Int): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_THROW_TC
                this.difficulty = difficulty
            }
        }

        fun replyToMasterThrowTC(
            address: String,
            dice: Int,
            result: Int,
            base: Int,
            bonus: Int,
            difficulty: Int
        ): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_THROW_TC
                this.address = address
                this.throwDice = dice
                this.throwResult = result
                punteggioBase = base
                punteggioBonus = bonus
                this.difficulty = difficulty
            }
        }

        //RICHIESTA TIRO ABILITA
        fun requestToMasterThrowAbility(
            address: String,
            ability: Int,
            base: Int,
            bonus: Int
        ): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_THROW_ABILITY
                this.address = address
                this.ability = ability
                punteggioBase = base
                punteggioBonus = bonus
            }
        }

        fun replyToPlayerThrowAbility(
            ability: Int,
            dice: Int,
            result: Int,
            base: Int,
            bonus: Int,
            difficulty: Int
        ): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_THROW_ABILITY
                this.ability = ability
                throwDice = dice
                throwResult = result
                this.punteggioBase = base
                this.punteggioBonus = bonus
                this.difficulty = difficulty
            }
        }

        fun requestToPlayerThrowAbility(ability: Int, difficulty: Int): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_THROW_ABILITY
                this.ability = ability
                this.difficulty = difficulty
            }
        }

        fun replyToMasterThrowAbility(
            address: String,
            ability: Int,
            dice: Int,
            result: Int,
            base: Int,
            bonus: Int,
            difficulty: Int
        ): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_THROW_ABILITY
                this.address = address
                this.ability = ability
                throwDice = dice
                throwResult = result
                punteggioBase = base
                punteggioBonus = bonus
                this.difficulty = difficulty
            }
        }

        //RICHIESTA TIRO SALVEZZA
        fun requestToPlayerThrowSaving(savingType: Int, difficulty: Int): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_THROW_SAVING
                this.savingType = savingType
                this.difficulty = difficulty
            }
        }

        fun replyToMasterThrowSaving(
            address: String,
            savingType: Int,
            dice: Int,
            result: Int,
            base: Int,
            bonus: Int,
            difficulty: Int
        ): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_THROW_SAVING
                this.address = address
                this.savingType = savingType
                throwDice = dice
                throwResult = result
                punteggioBase = base
                punteggioBonus = bonus
                this.difficulty = difficulty
            }
        }

        //RICHIESTA TIRO CLASSE ARMATURA
        fun requestToPlayerThrowCA(difficulty: Int): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_THROW_CA
                this.difficulty = difficulty
            }
        }

        fun replyToMasterThrowCA(
            address: String,
            dice: Int,
            result: Int,
            base: Int,
            bonus: Int,
            difficulty: Int
        ): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_THROW_CA
                this.address = address
                throwDice = dice
                throwResult = result
                punteggioBase = base
                punteggioBonus = bonus
                this.difficulty = difficulty
            }
        }

        //RICHIESTA TIRO LEVEL-UP
        fun requestToPlayerLevelUp(): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_LEVEL_UP
            }
        }

        fun replyToMasterLevelUp(address: String): JDMessage {
            return JDMessage().apply {
                requestID = TYPE.REQUEST_LEVEL_UP
                this.address = address
            }
        }
    }
}
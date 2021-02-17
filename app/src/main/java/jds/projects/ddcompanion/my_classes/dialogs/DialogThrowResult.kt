package jds.projects.ddcompanion.my_classes.dialogs

import android.app.AlertDialog
import android.content.Context
import jds.projects.ddcompanion.R
import jds.projects.ddcompanion.my_classes.dd_classes.DDAbility
import jds.projects.ddcompanion.my_classes.dd_classes.playersheet.PlayerSheet
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessage
import jds.projects.ddcompanion.my_classes.io.network.gamesession_networking.JDMessageManager

class DialogThrowResult {
    companion object {
        fun createHitThrowDialog(
            context: Context,
            playerName: String?,
            answer: JDMessage
        ): AlertDialog {
            val dialog = AlertDialog.Builder(context)
            val dice = answer.throwDice
            val result = answer.throwResult
            when (dice) {
                20 -> {
                    if (playerName != null)
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.answer_by,
                                playerName,
                                context.resources.getString(
                                    R.string.hit_throw_success,
                                    context.resources.getString(
                                        R.string.dice_result, dice,
                                        context.resources.getString(
                                            R.string.critical_success,
                                            context.resources.getString(
                                                R.string.throw_result,
                                                result,
                                                context.resources.getString(
                                                    R.string.throw_stats,
                                                    answer.punteggioBase,
                                                    answer.punteggioBonus,
                                                    answer.difficulty
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    else
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.hit_throw_success,
                                context.resources.getString(
                                    R.string.dice_result, dice,
                                    context.resources.getString(
                                        R.string.critical_success,
                                        context.resources.getString(
                                            R.string.throw_result, result,
                                            context.resources.getString(
                                                R.string.throw_stats,
                                                answer.punteggioBase,
                                                answer.punteggioBonus,
                                                answer.difficulty
                                            )
                                        )
                                    )
                                )
                            )
                        )
                }
                1 -> {
                    if (playerName != null)
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.answer_by,
                                playerName,
                                context.resources.getString(
                                    R.string.hit_throw_failure,
                                    context.resources.getString(
                                        R.string.dice_result, dice,
                                        context.resources.getString(
                                            R.string.critical_failure,
                                            context.resources.getString(
                                                R.string.throw_result,
                                                result,
                                                context.resources.getString(
                                                    R.string.throw_stats,
                                                    answer.punteggioBase,
                                                    answer.punteggioBonus,
                                                    answer.difficulty
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    else
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.hit_throw_failure,
                                context.resources.getString(
                                    R.string.dice_result, dice,
                                    context.resources.getString(
                                        R.string.critical_failure,
                                        context.resources.getString(
                                            R.string.throw_result, result,
                                            context.resources.getString(
                                                R.string.throw_stats,
                                                answer.punteggioBase,
                                                answer.punteggioBonus,
                                                answer.difficulty
                                            )
                                        )
                                    )
                                )
                            )
                        )
                }
                else -> {
                    if (result > 0)
                        if (playerName != null)
                            dialog.setMessage(
                                context.resources.getString(
                                    R.string.answer_by,
                                    playerName,
                                    context.resources.getString(
                                        R.string.hit_throw_success,
                                        context.resources.getString(
                                            R.string.dice_result, dice,
                                            context.resources.getString(
                                                R.string.throw_result,
                                                result,
                                                context.resources.getString(
                                                    R.string.throw_stats,
                                                    answer.punteggioBase,
                                                    answer.punteggioBonus,
                                                    answer.difficulty
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        else
                            dialog.setMessage(
                                context.resources.getString(
                                    R.string.hit_throw_success,
                                    context.resources.getString(
                                        R.string.dice_result, dice,
                                        context.resources.getString(
                                            R.string.throw_result, result,
                                            context.resources.getString(
                                                R.string.throw_stats,
                                                answer.punteggioBase,
                                                answer.punteggioBonus,
                                                answer.difficulty
                                            )
                                        )
                                    )
                                )
                            )
                    else
                        if (playerName != null)
                            dialog.setMessage(
                                context.resources.getString(
                                    R.string.answer_by,
                                    playerName,
                                    context.resources.getString(
                                        R.string.hit_throw_failure,
                                        context.resources.getString(
                                            R.string.dice_result, dice,
                                            context.resources.getString(
                                                R.string.throw_result,
                                                result,
                                                context.resources.getString(
                                                    R.string.throw_stats,
                                                    answer.punteggioBase,
                                                    answer.punteggioBonus,
                                                    answer.difficulty
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        else
                            dialog.setMessage(
                                context.resources.getString(
                                    R.string.hit_throw_failure,
                                    context.resources.getString(
                                        R.string.dice_result, dice,
                                        context.resources.getString(
                                            R.string.throw_result, result,
                                            context.resources.getString(
                                                R.string.throw_stats,
                                                answer.punteggioBase,
                                                answer.punteggioBonus,
                                                answer.difficulty
                                            )
                                        )
                                    )
                                )
                            )
                }
            }
            dialog.setCancelable(true)
            return dialog.create()
        }

        fun createCAThrowDialog(
            context: Context,
            playerName: String?,
            answer: JDMessage
        ): AlertDialog {
            val dialog = AlertDialog.Builder(context)
            val dice = answer.throwDice
            val result = answer.throwResult
            when (dice) {
                20 -> {
                    if (playerName != null)
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.answer_by,
                                playerName,
                                context.resources.getString(
                                    R.string.ca_throw_success,
                                    context.resources.getString(
                                        R.string.dice_result, dice,
                                        context.resources.getString(
                                            R.string.critical_success,
                                            context.resources.getString(
                                                R.string.throw_result,
                                                result,
                                                context.resources.getString(
                                                    R.string.throw_stats,
                                                    answer.punteggioBase,
                                                    answer.punteggioBonus,
                                                    answer.difficulty
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    else
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.ca_throw_success,
                                context.resources.getString(
                                    R.string.dice_result, dice,
                                    context.resources.getString(
                                        R.string.critical_success,
                                        context.resources.getString(
                                            R.string.throw_result, result,
                                            context.resources.getString(
                                                R.string.throw_stats,
                                                answer.punteggioBase,
                                                answer.punteggioBonus,
                                                answer.difficulty
                                            )
                                        )
                                    )
                                )
                            )
                        )
                }
                1 -> {
                    if (playerName != null)
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.answer_by,
                                playerName,
                                context.resources.getString(
                                    R.string.ca_throw_failure,
                                    context.resources.getString(
                                        R.string.dice_result, dice,
                                        context.resources.getString(
                                            R.string.critical_failure,
                                            context.resources.getString(
                                                R.string.throw_result,
                                                result,
                                                context.resources.getString(
                                                    R.string.throw_stats,
                                                    answer.punteggioBase,
                                                    answer.punteggioBonus,
                                                    answer.difficulty
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    else
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.ca_throw_failure,
                                context.resources.getString(
                                    R.string.dice_result, dice,
                                    context.resources.getString(
                                        R.string.critical_failure,
                                        context.resources.getString(
                                            R.string.throw_result, result,
                                            context.resources.getString(
                                                R.string.throw_stats,
                                                answer.punteggioBase,
                                                answer.punteggioBonus,
                                                answer.difficulty
                                            )
                                        )
                                    )
                                )
                            )
                        )
                }
                else -> {
                    if (result > 0)
                        if (playerName != null)
                            dialog.setMessage(
                                context.resources.getString(
                                    R.string.answer_by,
                                    playerName,
                                    context.resources.getString(
                                        R.string.ca_throw_success,
                                        context.resources.getString(
                                            R.string.dice_result, dice,
                                            context.resources.getString(
                                                R.string.throw_result,
                                                result,
                                                context.resources.getString(
                                                    R.string.throw_stats,
                                                    answer.punteggioBase,
                                                    answer.punteggioBonus,
                                                    answer.difficulty
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        else
                            dialog.setMessage(
                                context.resources.getString(
                                    R.string.ca_throw_success,
                                    context.resources.getString(
                                        R.string.dice_result, dice,
                                        context.resources.getString(
                                            R.string.throw_result, result,
                                            context.resources.getString(
                                                R.string.throw_stats,
                                                answer.punteggioBase,
                                                answer.punteggioBonus,
                                                answer.difficulty
                                            )
                                        )
                                    )
                                )
                            )
                    else
                        if (playerName != null)
                            dialog.setMessage(
                                context.resources.getString(
                                    R.string.answer_by,
                                    playerName,
                                    context.resources.getString(
                                        R.string.ca_throw_failure,
                                        context.resources.getString(
                                            R.string.dice_result, dice,
                                            context.resources.getString(
                                                R.string.throw_result,
                                                result,
                                                context.resources.getString(
                                                    R.string.throw_stats,
                                                    answer.punteggioBase,
                                                    answer.punteggioBonus,
                                                    answer.difficulty
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        else
                            dialog.setMessage(
                                context.resources.getString(
                                    R.string.ca_throw_failure,
                                    context.resources.getString(
                                        R.string.dice_result, dice,
                                        context.resources.getString(
                                            R.string.throw_result, result,
                                            context.resources.getString(
                                                R.string.throw_stats,
                                                answer.punteggioBase,
                                                answer.punteggioBonus,
                                                answer.difficulty
                                            )
                                        )
                                    )
                                )
                            )
                }
            }
            dialog.setCancelable(true)
            return dialog.create()
        }

        fun createAbilityThrowDialog(
            context: Context,
            playerName: String?,
            answer: JDMessage,
            ability: DDAbility
        ): AlertDialog {
            val dialog = AlertDialog.Builder(context)
            val dice = answer.throwDice
            val result = answer.throwResult
            when (dice) {
                20 -> {
                    if (playerName != null)
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.answer_by,
                                playerName,
                                context.resources.getString(
                                    R.string.ability_throw,
                                    ability.name,
                                    context.resources.getString(
                                        R.string.dice_result, dice,
                                        context.resources.getString(
                                            R.string.critical_success,
                                            context.resources.getString(
                                                R.string.throw_result,
                                                result,
                                                context.resources.getString(
                                                    R.string.throw_stats,
                                                    answer.punteggioBase,
                                                    answer.punteggioBonus,
                                                    answer.difficulty
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    else
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.ability_throw,
                                ability.name,
                                context.resources.getString(
                                    R.string.dice_result, dice,
                                    context.resources.getString(
                                        R.string.critical_success,
                                        context.resources.getString(
                                            R.string.throw_result, result,
                                            context.resources.getString(
                                                R.string.throw_stats,
                                                answer.punteggioBase,
                                                answer.punteggioBonus,
                                                answer.difficulty
                                            )
                                        )
                                    )
                                )
                            )
                        )
                }
                1 -> {
                    if (playerName != null)
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.answer_by,
                                playerName,
                                context.resources.getString(
                                    R.string.ability_throw,
                                    ability.name,
                                    context.resources.getString(
                                        R.string.dice_result, dice,
                                        context.resources.getString(
                                            R.string.critical_failure,
                                            context.resources.getString(
                                                R.string.throw_result,
                                                result,
                                                context.resources.getString(
                                                    R.string.throw_stats,
                                                    answer.punteggioBase,
                                                    answer.punteggioBonus,
                                                    answer.difficulty
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    else
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.ability_throw,
                                ability.name,
                                context.resources.getString(
                                    R.string.dice_result, dice,
                                    context.resources.getString(
                                        R.string.critical_failure,
                                        context.resources.getString(
                                            R.string.throw_result, result,
                                            context.resources.getString(
                                                R.string.throw_stats,
                                                answer.punteggioBase,
                                                answer.punteggioBonus,
                                                answer.difficulty
                                            )
                                        )
                                    )
                                )
                            )
                        )
                }
                else -> {
                    if (playerName != null)
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.answer_by,
                                playerName,
                                context.resources.getString(
                                    R.string.ability_throw,
                                    ability.name,
                                    context.resources.getString(
                                        R.string.dice_result, dice,
                                        context.resources.getString(
                                            R.string.throw_result, result,
                                            context.resources.getString(
                                                R.string.throw_stats,
                                                answer.punteggioBase,
                                                answer.punteggioBonus,
                                                answer.difficulty
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    else
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.ability_throw,
                                ability.name,
                                context.resources.getString(
                                    R.string.dice_result, dice,
                                    context.resources.getString(
                                        R.string.throw_result, result,
                                        context.resources.getString(
                                            R.string.throw_stats,
                                            answer.punteggioBase,
                                            answer.punteggioBonus,
                                            answer.difficulty
                                        )
                                    )
                                )
                            )
                        )
                }
            }
            dialog.setCancelable(true)
            return dialog.create()
        }

        fun createSavingThrowDialog(
            context: Context,
            playerName: String?,
            answer: JDMessage
        ): AlertDialog {
            val dialog = AlertDialog.Builder(context)
            val dice = answer.throwDice
            val result = answer.throwResult
            var title = when (answer.savingType) {
                JDMessageManager.SAVES.FORTITUDE ->
                    context.resources.getString(R.string.throw_saving_tempra)
                JDMessageManager.SAVES.REFLEXES ->
                    context.resources.getString(R.string.throw_saving_riflessi)
                else ->
                    context.resources.getString(R.string.throw_saving_volonta)
            }
            title += "\n"
            when (dice) {
                20 -> {
                    if (playerName != null)
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.answer_by,
                                playerName,
                                title +
                                        context.resources.getString(
                                            R.string.dice_result, dice,
                                            context.resources.getString(
                                                R.string.critical_success,
                                                context.resources.getString(
                                                    R.string.throw_result,
                                                    result,
                                                    context.resources.getString(
                                                        R.string.throw_stats,
                                                        answer.punteggioBase,
                                                        answer.punteggioBonus,
                                                        answer.difficulty
                                                    )
                                                )
                                            )
                                        )
                            )
                        )
                    else
                        dialog.setMessage(
                            title +
                                    context.resources.getString(
                                        R.string.dice_result, dice,
                                        context.resources.getString(
                                            R.string.critical_success,
                                            context.resources.getString(
                                                R.string.throw_result,
                                                result,
                                                context.resources.getString(
                                                    R.string.throw_stats,
                                                    answer.punteggioBase,
                                                    answer.punteggioBonus,
                                                    answer.difficulty
                                                )
                                            )
                                        )
                                    )
                        )
                }
                1 -> {
                    if (playerName != null)
                        dialog.setMessage(
                            context.resources.getString(
                                R.string.answer_by,
                                playerName,
                                title +
                                        context.resources.getString(
                                            R.string.dice_result, dice,
                                            context.resources.getString(
                                                R.string.critical_failure,
                                                context.resources.getString(
                                                    R.string.throw_result,
                                                    result,
                                                    context.resources.getString(
                                                        R.string.throw_stats,
                                                        answer.punteggioBase,
                                                        answer.punteggioBonus,
                                                        answer.difficulty
                                                    )
                                                )
                                            )
                                        )
                            )
                        )
                    else
                        dialog.setMessage(
                            title +
                                    context.resources.getString(
                                        R.string.dice_result, dice,
                                        context.resources.getString(
                                            R.string.critical_failure,
                                            context.resources.getString(
                                                R.string.throw_result,
                                                result,
                                                context.resources.getString(
                                                    R.string.throw_stats,
                                                    answer.punteggioBase,
                                                    answer.punteggioBonus,
                                                    answer.difficulty
                                                )
                                            )
                                        )
                                    )
                        )
                }
                else -> {
                    if (result > 0)
                        if (playerName != null)
                            dialog.setMessage(
                                context.resources.getString(
                                    R.string.answer_by,
                                    playerName,
                                    "$title${context.resources.getString(R.string.succeeded)}" +
                                            context.resources.getString(
                                                R.string.dice_result, dice,
                                                context.resources.getString(
                                                    R.string.throw_result,
                                                    result,
                                                    context.resources.getString(
                                                        R.string.throw_stats,
                                                        answer.punteggioBase,
                                                        answer.punteggioBonus,
                                                        answer.difficulty
                                                    )
                                                )
                                            )
                                )
                            )
                        else
                            dialog.setMessage(
                                "$title${context.resources.getString(R.string.succeeded)}" +
                                        context.resources.getString(
                                            R.string.dice_result, dice,
                                            context.resources.getString(
                                                R.string.throw_result,
                                                result,
                                                context.resources.getString(
                                                    R.string.throw_stats,
                                                    answer.punteggioBase,
                                                    answer.punteggioBonus,
                                                    answer.difficulty
                                                )
                                            )
                                        )
                            )
                    else
                        if (playerName != null)
                            dialog.setMessage(
                                context.resources.getString(
                                    R.string.answer_by,
                                    playerName,
                                    "$title${context.resources.getString(R.string.failed)}" +
                                            context.resources.getString(
                                                R.string.dice_result, dice,
                                                context.resources.getString(
                                                    R.string.throw_result,
                                                    result,
                                                    context.resources.getString(
                                                        R.string.throw_stats,
                                                        answer.punteggioBase,
                                                        answer.punteggioBonus,
                                                        answer.difficulty
                                                    )
                                                )
                                            )
                                )
                            )
                        else
                            dialog.setMessage(
                                "$title${context.resources.getString(R.string.failed)}" +
                                        context.resources.getString(
                                            R.string.dice_result, dice,
                                            context.resources.getString(
                                                R.string.throw_result,
                                                result,
                                                context.resources.getString(
                                                    R.string.throw_stats,
                                                    answer.punteggioBase,
                                                    answer.punteggioBonus,
                                                    answer.difficulty
                                                )
                                            )
                                        )
                            )
                }
            }
            dialog.setCancelable(true)
            return dialog.create()
        }

        fun createLevelUpDialog(context: Context, playerSheet: PlayerSheet): AlertDialog {
            val dialog = AlertDialog.Builder(context)
            dialog.setMessage(
                playerSheet.playerName + " " + context.getString(
                    R.string.dialog_level_up_text,
                    (playerSheet.playerLevel + 1)
                )
            )
            dialog.setCancelable(true)
            return dialog.create()
        }
    }
}
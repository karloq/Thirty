package umu.kaoq0003.thirty.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import umu.kaoq0003.thirty.enums.CountMethod
import umu.kaoq0003.thirty.enums.GameState

/**
 *  Author: Karl Öqvist
 *
 * Data object for holding information needed to manage the game
 *   - round: current round (1-10)
 *   - throws: current throw (1-3)
 *   - countMethod: chosen counting method for current round
 *   - points: total accumulated points, shown in result screen
 *   - roundPoints: points for the last count
 *   - dices: list of Dice, containing the six dices in the game
 *   - gameState: holds the current GameState
 */

@Parcelize
data class Game(
    var round : Int,
    var throws : Int,
    var countMethod : CountMethod,
    var points : Int,
    var roundPoints : Int,
    var dices : List<Dice>,
    var gameState : GameState
) : Parcelable
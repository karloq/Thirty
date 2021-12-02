package umu.kaoq0003.thirty.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import umu.kaoq0003.thirty.enums.DiceStatus

/**
 * Author: Karl Öqvist
 *
 * Data object for holding information needed to manage a die
 *   - id: used for choosing the right source image for ImageButton
 *   - value: value of the dice
 *   - status: hold the status of the dice, used for holding dices between throws
 *   - source: holds the image source for the ImageButton
 */

@Parcelize
data class Dice(val id: Int,
    var value : Int,
    var status : DiceStatus,
    var source : Int
) : Parcelable

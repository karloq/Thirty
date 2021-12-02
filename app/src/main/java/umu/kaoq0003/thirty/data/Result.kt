package umu.kaoq0003.thirty.data

import android.os.Parcelable
import umu.kaoq0003.thirty.enums.CountMethod
import kotlinx.android.parcel.Parcelize

/**
 * Author: Karl Öqvist
 *
 * Data object for holding information needed for saving and presenting the results.
 *  Gets parcelized to be sent as extra to ResultActivity through the intent.
 *   - method: chosen count method for the round
 *   - points: points scored for chosen countMethod
 */

@Parcelize
data class Result(
    var method: CountMethod,
    var points: Int
) : Parcelable
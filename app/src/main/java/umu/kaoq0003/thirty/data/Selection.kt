package umu.kaoq0003.thirty.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import umu.kaoq0003.thirty.enums.CountMethod
import umu.kaoq0003.thirty.enums.Status

/**
 * Author: Karl Öqvist
 *
 * Data object for counting method selection
 *   - id: CountMethod ordinal
 *   - status: holds the status of the counting method
 *   - activated: keeps status of whether selection is activated or not
 */

@Parcelize
class Selection( val countMethodOrdinal : CountMethod,
    var status : Status,
    var activated : Boolean
    ) : Parcelable
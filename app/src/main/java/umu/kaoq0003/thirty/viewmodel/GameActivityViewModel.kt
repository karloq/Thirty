package umu.kaoq0003.thirty.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import umu.kaoq0003.thirty.data.Game
import umu.kaoq0003.thirty.data.Result
import umu.kaoq0003.thirty.data.Selection
import umu.kaoq0003.thirty.enums.CountMethod
import umu.kaoq0003.thirty.handlers.GameHandler

/**
 * Author: Karl Öqvist
 *
 * ViewModel that holds all vital data that needs to be kept throughout the whole lifecycle of the
 * application to be able to handle configuration change and pause/resume
 */

class GameActivityViewModel : ViewModel() {


    lateinit var game: Game

    lateinit var gameHandler: GameHandler

    var countMethodSelected = false

    // Sets to true when configuration change happens or application gets put in background
    var resume = false

    var countSelections: MutableList<Selection> = mutableListOf()

    var results: ArrayList<Result> = arrayListOf(
        Result(CountMethod.FOUR, 0),
        Result(CountMethod.FIVE, 0),
        Result(CountMethod.SIX, 0),
        Result(CountMethod.SEVEN, 0),
        Result(CountMethod.EIGHT, 0),
        Result(CountMethod.NINE, 0),
        Result(CountMethod.TEN, 0),
        Result(CountMethod.ELEVEN, 0),
        Result(CountMethod.TWELVE, 0),
        Result(CountMethod.LOW, 0)
    )
}
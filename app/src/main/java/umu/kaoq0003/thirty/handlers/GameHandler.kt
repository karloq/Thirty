package umu.kaoq0003.thirty.handlers

import umu.kaoq0003.thirty.enums.GameState
import umu.kaoq0003.thirty.viewmodel.GameActivityViewModel

/**
 * Author: Karl Öqvist
 *
 * Class that handles the game. Handles the throws and points as well as updating GUI strings
 */

class GameHandler(val viewModel: GameActivityViewModel) {
    private val throwStrings: List<String> = listOf(
        "0/3", "1/3", "2/3", "3/3"
    )

    /*
    * Called in conjunction with click on throw button.
    * Increases throws, and round when three throws has been done
     */
    fun updateThrow() {
        val game = viewModel.game
        game.throws++
        if (game.throws > 3) {
            game.throws = 1
            game.round++
        }
    }

    /*
    * Updates the viewModel with new points from counting and saving result
    * from the round for use in the ResultActivity
     */
    fun updatePoints(points: Int) {
        val game = viewModel.game
        val results = viewModel.results
        game.roundPoints = points
        game.points = game.points + game.roundPoints

        results[game.countMethod.ordinal].points = game.roundPoints

    }

    /*
    * Sets string for the throws in the GUI
    */
    fun getGUIStrings(throws: Int): String {
        return when (throws) {
            4 -> throwStrings[0]
            else -> throwStrings[throws]
        }
    }

    /*
    * Changes the throw button string based on the state of the game
    */
    fun getButtonString(): String {
        val gameState = viewModel.game.gameState
        val round = viewModel.game.round
        lateinit var result: String
        when (gameState) {
            GameState.START -> result = "start"
            GameState.COUNT -> {
                result = "next round"
                if (round == 10) {
                    result = "see results"
                }
            }
            GameState.END -> result = "see results"
            GameState.THREE -> result = "count"
            else -> result = "throw"
        }
        return result
    }
}



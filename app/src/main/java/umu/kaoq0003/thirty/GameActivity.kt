package umu.kaoq0003.thirty

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.thirty.R
import com.example.thirty.databinding.ActivityGameBinding
import umu.kaoq0003.thirty.data.Game
import umu.kaoq0003.thirty.data.Result
import umu.kaoq0003.thirty.data.Selection
import umu.kaoq0003.thirty.enums.CountMethod
import umu.kaoq0003.thirty.enums.GameState
import umu.kaoq0003.thirty.handlers.DiceHandler
import umu.kaoq0003.thirty.handlers.GameHandler
import umu.kaoq0003.thirty.handlers.SelectionHandler
import umu.kaoq0003.thirty.viewmodel.GameActivityViewModel

/**
 * Author: Karl Öqvist
 *
 * The main activity for the game.
 *   - Handles the state machine that drives the game
 *   - Instantiates the GameHandler, SelectionHandler and DiceHandler
 *   - Creates the viewModel
 *   - Updates GUI
 */

class GameActivity : AppCompatActivity() {
    private var doubleBackPressedOnce = false

    private lateinit var viewModel: GameActivityViewModel
    private lateinit var binding: ActivityGameBinding

    private lateinit var throwButton: Button
    private lateinit var roundText: TextView
    private lateinit var throwText: TextView

    private lateinit var diceHandler: DiceHandler
    private lateinit var selectionHandler: SelectionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(GameActivityViewModel::class.java)

        diceHandler = DiceHandler(binding)
        selectionHandler = SelectionHandler(binding, viewModel)
        diceHandler.setupDices()

        throwButton = findViewById(R.id.game_throw_button)

        throwButton.setOnClickListener {
            throwHandler()
        }

        throwText = findViewById(R.id.game_throw_variable_textview)
        roundText = findViewById(R.id.game_round_variable_textview)


        if (!viewModel.resume) {
            // Game is started for the first time
            selectionHandler.setupSelections()
            viewModel.gameHandler = GameHandler(viewModel)
            viewModel.game =
                Game(1, 0, CountMethod.UNDEFINED, 0, 0, diceHandler.dices, GameState.START)
        } else {
            //Game is resumed
            restoreGame()
        }
    }

    /*
    * Save crucial data if process is killed
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("game", viewModel.game)
        outState.putBoolean("countMethodSelected", viewModel.countMethodSelected)
        val countMethodArray: ArrayList<Selection> =
            viewModel.countSelections as ArrayList<Selection>
        outState.putParcelableArrayList("selection_array", countMethodArray)
        outState.putParcelableArrayList("result_array", viewModel.results)
    }

    /*
    * Restore the game and gui after rotation or process killed
     */
    fun restoreGame() {
        diceHandler.resumeDices(viewModel.game.dices)
        viewModel.game.dices = diceHandler.dices
        selectionHandler.resumeSelections()
        selectionHandler.updateMethodSelection()
        updateGUI()
    }

    /*
    * Restore crucial data after process has been killed
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        viewModel.resume = true
        viewModel.game = savedInstanceState.getParcelable<Game>("game")!!
        viewModel.countMethodSelected = savedInstanceState.getBoolean("countMethodSelected")
        val countMethodList: List<Selection> =
            savedInstanceState.getParcelableArrayList<Selection>("selection_array")!!
        viewModel.countSelections = countMethodList as MutableList<Selection>
        viewModel.results = savedInstanceState.getParcelableArrayList<Result>("result_array")!!

        restoreGame()

        super.onRestoreInstanceState(savedInstanceState)
    }

    /*
    * Double clicking the back button will return to start screen
     */
    override fun onBackPressed() {
        if (doubleBackPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackPressedOnce = true
        Toast.makeText(this, "Click back again to return to start screen", Toast.LENGTH_LONG).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackPressedOnce = false
        }, 2000)
    }

    /*
    * Sets boolean in ViewModel for use in OnCreate when resumed
     */
    override fun onPause() {
        viewModel.resume = true
        super.onPause()
    }

    /*
    * Called from the OnClickListener of the throw button
     */
    private fun throwHandler() {
        updateState()
    }


    /*
    * Called when game is finished to start ResultActivity
     */
    private fun finishGame() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putParcelableArrayListExtra("results", viewModel.results)
        intent.putExtra("totalPoints", viewModel.game.points)

        startActivity(intent)
    }

    /*
    * Updates the gameState based on previous state
     */
    private fun updateState() {
        val state = viewModel.game.gameState
        var newState = viewModel.game.gameState

        when (state) {
            GameState.COUNT -> {
                newState = if (viewModel.game.round == 10) {
                    GameState.END
                } else {
                    GameState.ONE
                }
            }
            GameState.START -> {
                newState = GameState.ONE
            }
            GameState.ONE -> {
                newState = GameState.TWO
            }
            GameState.TWO -> {
                newState = GameState.THREE
            }
            GameState.THREE -> {
                // Guarantees countingMethod is chosen
                if (!viewModel.countMethodSelected) {
                    val string = "You must choose a count method"
                    Toast.makeText(
                        applicationContext,
                        string,
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                newState = GameState.COUNT
            }
            GameState.END -> {
                newState = GameState.END
            }
        }
        viewModel.game.gameState = newState

        tick()
    }

    /*
    * Performs all actions that should be performed when state is changed
     */
    private fun tick() {
        when (viewModel.game.gameState) {
            GameState.START -> {
                updateGUI()
            }
            GameState.ONE -> {
                diceHandler.throwDices()
                viewModel.game.dices = diceHandler.dices
                viewModel.gameHandler.updateThrow()
                updateGUI()
            }
            GameState.TWO -> {
                diceHandler.throwDices()
                viewModel.game.dices = diceHandler.dices
                viewModel.gameHandler.updateThrow()
                updateGUI()
            }
            GameState.THREE -> {
                diceHandler.throwDices()
                viewModel.game.dices = diceHandler.dices
                viewModel.gameHandler.updateThrow()
                updateGUI()
            }
            GameState.COUNT -> {
                val points = diceHandler.countDices(viewModel.game.countMethod)
                viewModel.gameHandler.updatePoints(points)
                selectionHandler.lockCountMethod()
                selectionHandler.updateMethodSelection()
                selectionHandler.activateAllSelections()
                updateGUI()
                diceHandler.resetDices()
                viewModel.game.dices = diceHandler.dices
                Toast.makeText(
                    applicationContext,
                    viewModel.game.roundPoints.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
            GameState.END -> {
                viewModel.game.throws = 3
                viewModel.game.round = 10
                finishGame()
            }
        }
    }

    /*
    * Updates the GUI for each throw
     */
    private fun updateGUI() {
        throwText.text = viewModel.gameHandler.getGUIStrings(viewModel.game.throws)
        roundText.text = viewModel.game.round.toString()
        throwButton.text = viewModel.gameHandler.getButtonString()
    }

}

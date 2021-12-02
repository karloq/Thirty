package umu.kaoq0003.thirty.handlers

import umu.kaoq0003.thirty.data.Selection
import com.example.thirty.databinding.ActivityGameBinding
import umu.kaoq0003.thirty.enums.CountMethod
import umu.kaoq0003.thirty.enums.Status
import umu.kaoq0003.thirty.viewmodel.GameActivityViewModel

/**
 * Author: Karl Öqvist
 *
 * Class that handles countingMethod selections.
 *   - Uses binding for setting up the buttons
 *   - The countMethod selections are held in the viewModel
 */


class SelectionHandler(binding: ActivityGameBinding, private val viewModel: GameActivityViewModel) {

    // Fetches all buttons from binding for easier setup
    private val countMethodButtons = mutableListOf(
        binding.gameCountButton4,
        binding.gameCountButton5,
        binding.gameCountButton6,
        binding.gameCountButton7,
        binding.gameCountButton8,
        binding.gameCountButton9,
        binding.gameCountButton10,
        binding.gameCountButton11,
        binding.gameCountButton12,
        binding.gameCountButtonLow
    )

    /*
    * Adds all selections to the viewModel and sets up the onClickListener
     */
    fun setupSelections() {
        for (i in 0..9) {
            val method: CountMethod = CountMethod.values()[i]
            viewModel.countSelections.add(Selection(method, Status.UNUSED, true))
        }

        for (i in countMethodButtons.indices) {
            countMethodButtons[i].setOnClickListener {
                countMethodHandler(i)
            }
        }
    }

    /*
    * Sets up the OnClickListener for the buttons when they already have been added to the viewModel
     */
    fun resumeSelections() {
        for (i in countMethodButtons.indices) {
            countMethodButtons[i].setOnClickListener {
                countMethodHandler(i)
            }
        }
    }

    /*
    * Called from te OnClickListener of the selections buttons
    *  - Selected method button is activated all other deactivated
    *  - Clicking already selected method will undo the selection and activate all buttons again
     */
    private fun countMethodHandler(index: Int) {
        if (!viewModel.countMethodSelected) {
            deactivateAllSelections(index)
            viewModel.countMethodSelected = true
            viewModel.game.countMethod = viewModel.countSelections[index].countMethodOrdinal
        } else {
            activateAllSelections()
            viewModel.countMethodSelected = false
            viewModel.game.countMethod = CountMethod.UNDEFINED
        }
        updateMethodSelection()
    }

    /*
    * Activates all the countingMethod selection buttons
     */
    fun activateAllSelections() {
        for (i in viewModel.countSelections.indices) {
            viewModel.countSelections[i].activated = true
        }
    }

    /*
    * Deactivates all the countingMethod selection buttons
     */
    private fun deactivateAllSelections(index: Int) {
        for (i in viewModel.countSelections.indices) {
            viewModel.countSelections[i].activated = false
            if (index == i) {
                viewModel.countSelections[i].activated = true
            }
        }
    }

    /*
    * Enables/disables counting method selection buttons based on their activated status
     */
    fun updateMethodSelection() {
        for (i in viewModel.countSelections.indices) {
            if (viewModel.countSelections[i].status == Status.USED) {
                countMethodButtons[i].isEnabled = false
            } else {
                countMethodButtons[i].isEnabled = viewModel.countSelections[i].activated
            }
        }
    }

    /*
    * Locks current counting method by reporting it to the viewModel and sets it to USED.
    * Reactivates all buttons again.
     */
    fun lockCountMethod() {
        val index = viewModel.game.countMethod.ordinal
        if (index < 10) {
            viewModel.countSelections[index].status = Status.USED
        }
        viewModel.countMethodSelected = false
        activateAllSelections()
    }
}
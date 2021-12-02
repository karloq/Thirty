package umu.kaoq0003.thirty.handlers

import com.example.thirty.R
import umu.kaoq0003.thirty.data.Dice
import com.example.thirty.databinding.ActivityGameBinding
import umu.kaoq0003.thirty.enums.CountMethod
import umu.kaoq0003.thirty.enums.DiceStatus
import java.util.function.Predicate

/**
 * Author: Karl Öqvist
 *
 * Class that handles the dices. All dice functions are defined here.
 * Uses Binding as constructor parameter for setting up the buttons.
 * The public dices list holds all the dices so that the GameActivity can reach it.
 */

class DiceHandler(binding: ActivityGameBinding) {
    private val inplaySources: List<Int> = listOf(
        R.drawable.white1, R.drawable.white2,
        R.drawable.white3, R.drawable.white4,
        R.drawable.white5, R.drawable.white6
    )

    private val reservedSources: List<Int> = listOf(
        R.drawable.red1, R.drawable.red2,
        R.drawable.red3, R.drawable.red4,
        R.drawable.red5, R.drawable.red6
    )

    private val unreservedSources: List<Int> = listOf(
        R.drawable.grey1, R.drawable.grey2,
        R.drawable.grey3, R.drawable.grey4,
        R.drawable.grey5, R.drawable.grey6
    )

    private val diceButtons = mutableListOf(
        binding.gameDiceButton0,
        binding.gameDiceButton1,
        binding.gameDiceButton2,
        binding.gameDiceButton3,
        binding.gameDiceButton4,
        binding.gameDiceButton5
    )

    var dices: MutableList<Dice> = mutableListOf()

    /*
    * Adds all dices to 'dices' and sets the onClickListener
    */
    fun setupDices() {
        for (i in 0..5) {
            this.dices.add(Dice(i, 1, DiceStatus.INPLAY, inplaySources[0]))
        }

        for (i in diceButtons.indices) {
            val button = diceButtons[i]
            val dice = this.dices[i]
            button.setOnClickListener {
                changeDiceStatus(dice)
            }
        }
    }

    /*
    * Called when GameActivity has been paused and then resumed.
    */
    fun resumeDices(diceList: List<Dice>) {
        for (i in diceList.indices) {
            val savedDice = diceList[i]
            dices[i].status = savedDice.status
            dices[i].value = savedDice.value

            val dice = dices[i]
            changeButtonSource(dice)
        }
    }

    /*
   * Assign all dices that are not reserved a new random value
   */
    fun throwDices() {
        for (i in dices.indices) {
            val dice = dices[i]
            when (dice.status) {
                DiceStatus.INPLAY -> dice.value = (1..6).random()
                DiceStatus.UNRESERVED -> {
                    dice.value = (1..6).random()
                    updateUnreserved(dice)
                }
                else -> continue
            }
            changeButtonSource(dice)
        }
    }

    /*
   * Change status of dices that has been previously reserved
   */
    private fun updateUnreserved(dice: Dice) {
        dice.status = DiceStatus.INPLAY
    }

    /*
   * Called before new round to reset all dices to value 1
   */
    fun resetDices() {
        for (i in dices.indices) {
            val dice = dices[i]
            dice.value = 1
            updateUnreserved(dice)
            changeButtonSource(dice)
        }
    }

    /*
   * Called when a dice is clicked on to change its status
   */
    private fun changeDiceStatus(dice: Dice) {
        when (dice.status) {
            DiceStatus.INPLAY -> dice.status = DiceStatus.RESERVED
            DiceStatus.RESERVED -> dice.status = DiceStatus.UNRESERVED
            DiceStatus.UNRESERVED -> dice.status = DiceStatus.INPLAY
        }
        changeButtonSource(dice)
    }

    /*
   * Change colours on the dices according to their status
   */
    private fun changeButtonSource(dice: Dice) {
        when (dice.status) {
            DiceStatus.INPLAY -> dice.source = inplaySources[dice.value - 1]
            DiceStatus.RESERVED -> dice.source = reservedSources[dice.value - 1]
            DiceStatus.UNRESERVED -> dice.source = unreservedSources[dice.value - 1]
        }

        diceButtons[dice.id].setImageResource(dice.source)
    }


    /*
   * Handles repetitive calls to countSortedValues as well as counting LOW selection
   */
    fun countDices(countMethod: CountMethod): Int {
        var returnValue = 0

        val unsortedValues = mutableListOf<Int>()

        val wantedSum = countMethod.ordinal + 4

        // Remove values larger than the wanted sum
        for (i in dices.indices) {
            val diceValue = dices[i].value
            if (diceValue <= wantedSum)
                unsortedValues.add(dices[i].value)
        }

        // Sort values in descending order
        var sortedValues = unsortedValues.sortedDescending() as MutableList<Int>

        // Algorithm for the special case of LOW counting method, returns when finished
        if (countMethod == CountMethod.LOW) {
            var sum = 0
            for (i in 0..5) {
                val value = sortedValues[i]
                if (value <= 3) {
                    sum += value
                }
            }
            return sum
        }

        // Calls countSortedValues a maximum of six times, returns when countSortedPairs has finished
        for (i in 0..5) {
            val returnPair = countSortedValues(sortedValues, wantedSum)
            // One successful combination has been found
            if (returnPair.second == 1) {
                returnValue += wantedSum
            }
            // No successful combination found
            if (returnPair.first.size > 0) {
                sortedValues = returnPair.first
            } else {
                // All combinations tested
                break
            }
        }
        return returnValue
    }

    /*
   * Counting algorithm for the game.
   *    - Starts with biggest value in list
   *    - Recursively adds next biggest value in list that doesn't make the sum surpass the wanted sum
   *    - If wanted sum is found: removes all used values and returns the new list
   *    - If not found: returns new list with all values but the biggest
   */
    private fun countSortedValues(
        values: MutableList<Int>,
        wantedSum: Int
    ): Pair<MutableList<Int>, Int> {
        val sum: Int
        val sortedValues = values.toMutableList()
        // Add largest value to to sum
        sum = sortedValues[0]
        // Remove largest
        sortedValues.removeAt(0)
        val unsuccessfulReturnValues = sortedValues

        var remainingSum = wantedSum - sum

        for (i in sortedValues.indices) {
            val temp = sortedValues[i]
            // Decrease remainingSum if usable value is found and change that value to zero
            if (temp <= remainingSum && temp != 0) {
                remainingSum -= temp
                sortedValues[i] = 0
            }
            // Wanted sum is reached
            if (remainingSum == 0) {
                break
            }
        }
        // Removes all values that are zero in the list
        remove(sortedValues)

        return if (remainingSum == 0) {
            // Successful
            Pair(sortedValues, 1)
        } else {
            // Unsuccessful
            Pair(unsuccessfulReturnValues, 0)
        }
    }

    /*
    *Helper function for removing zero values
    */
    private fun remove(list: MutableList<Int>) {
        val newList: MutableList<Int> = ArrayList()
        val predicate = Predicate { value: Int -> value == 0 }
        list.filter { predicate.test(it) }.forEach { newList.add(it) }
        list.removeAll(newList)
    }
}



package umu.kaoq0003.thirty.enums

/**
 * Author: Karl Öqvist
 *
 *  Statuses for the dice
 *      - INPLAY: default status, will be rolled next throw (white dice)
 *      - RESERVED: will be held next throw (red dice)
 *      - UNRESERVED: was previously reserved (grey dice)
 */
enum class DiceStatus {
    INPLAY, RESERVED, UNRESERVED
}
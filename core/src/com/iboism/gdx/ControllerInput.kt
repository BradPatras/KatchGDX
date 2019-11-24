package com.iboism.gdx

/**
 * Created by Brad on 11/3/2017.
 */
class ControllerInputCreator(
        private val left: Boolean,
        private val right: Boolean,
        private val top: Boolean) {

    fun create(): ControllerInput {
        return if (left && right) {
            ControllerInput.LeftRight
        } else if (left) {
            ControllerInput.Left
        } else if (right) {
            ControllerInput.Right
        } else {
            ControllerInput.None
        }
    }
}

enum class ControllerInput {
    None,
    Left,
    Right,
    LeftRight
}
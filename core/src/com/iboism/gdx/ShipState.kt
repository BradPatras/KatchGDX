package com.iboism.gdx

import com.badlogic.gdx.math.Vector3

interface ShipState {
    fun newStateForInput(input: ControllerInput): ShipState
    fun update(ship: Ship, delta: Float)
}

open class ShipBaseState : ShipState {
    override fun newStateForInput(input: ControllerInput): ShipState {
        return when(input) {
            ControllerInput.Left -> ShipLeftThrustState()
            ControllerInput.Right -> ShipRightThrustState()
            ControllerInput.LeftRight -> ShipBothThrustState()
            else -> ShipIdleState()
        }
    }

    override fun update(ship: Ship, delta: Float) {}
}

class ShipLeftThrustState : ShipBaseState() {
    override fun newStateForInput(input: ControllerInput): ShipState {
        return when(input) {
            ControllerInput.Left -> this
            else -> super.newStateForInput(input)
        }
    }

    override fun update(ship: Ship, delta: Float) {
        ship.spriteCurrent = ship.sprite_l

        val radians = ship.getPosition().z * Math.PI.toFloat() / 180f
        val tv = Vector3()
        tv.z = -ship.accel
        tv.x = -(ship.accel / 7f) * Math.sin(radians.toDouble()).toFloat()
        tv.y = ship.accel / 7f * Math.cos(radians.toDouble()).toFloat()
        ship.setVelocity(ship.getVelocity().add(tv.scl(delta)))
    }
}

class ShipRightThrustState : ShipBaseState() {
    override fun newStateForInput(input: ControllerInput): ShipState {
        return when(input) {
            ControllerInput.Right -> this
            else -> super.newStateForInput(input)
        }
    }

    override fun update(ship: Ship, delta: Float) {
        ship.spriteCurrent = ship.sprite_r
        val radians = ship.getPosition().z * Math.PI.toFloat() / 180f
        val tv = Vector3()
        tv.z = ship.accel
        tv.x = -(ship.accel / 7f) * Math.sin(radians.toDouble()).toFloat()
        tv.y = ship.accel / 7f * Math.cos(radians.toDouble()).toFloat()
        ship.setVelocity(ship.getVelocity().add(tv.scl(delta)))
    }
}

class ShipBothThrustState : ShipBaseState() {
    override fun newStateForInput(input: ControllerInput): ShipState {
        return when(input) {
            ControllerInput.LeftRight -> this
            else -> super.newStateForInput(input)
        }
    }

    override fun update(ship: Ship, delta: Float) {
        ship.spriteCurrent = ship.sprite_lr

        val radians = ship.getPosition().z * Math.PI.toFloat() / 180f
        var tv = Vector3()
        tv.x = -ship.accel * Math.sin(radians.toDouble()).toFloat()
        tv.y = ship.accel * Math.cos(radians.toDouble()).toFloat()
        ship.setVelocity(ship.getVelocity().add(tv.scl(delta)))
    }
}

class ShipIdleState : ShipBaseState() {
    override fun newStateForInput(input: ControllerInput): ShipState {
        return when(input) {
            ControllerInput.None -> this
            else -> super.newStateForInput(input)
        }
    }

    override fun update(ship: Ship, delta: Float) {
        ship.spriteCurrent = ship.sprite_n
    }
}
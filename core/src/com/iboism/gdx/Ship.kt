package com.iboism.gdx

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array

/**
 * Created by Calm on 5/24/2017.
 */

class Ship: Viewable, Controllable, Dynamic, Plotted, Motile, VisiblyThrusted, MainCharacter, FlightAssistEquipped {
    lateinit var sprite_lr: TextureAtlas.AtlasRegion
    lateinit var sprite_l: TextureAtlas.AtlasRegion
    lateinit var sprite_r: TextureAtlas.AtlasRegion
    lateinit var sprite_n: TextureAtlas.AtlasRegion

    var spriteCurrent: TextureAtlas.AtlasRegion? = null

    private lateinit var thrustAtlas: TextureAtlas

    private var state: ShipState = ShipIdleState()

    /* SHIP VALUES */

    /*
	Dimensions
	 */
    var dim: Vector2 = Vector2()

    /*
    Position
     */
    var pos: Vector3 = Vector3()

    /*
    Velocity
     */
    var vel: Vector3 = Vector3()

    /*
    Acceleration
     */
    var accel: Float = 0f

    private var controllerInput: ControllerInput? = null

    constructor(
            atlas: TextureAtlas,
            dimensions: Vector2) {
        load(atlas)
        dim = dimensions
    }

    private fun thrustVectorFor(state: ShipState, thrustAccel: Float): Vector3 {
        if (state is ShipIdleState) return Vector3()
        val radians = getPosition().z * Math.PI.toFloat() / 180f
        var tv = Vector3()
        if (state is ShipBothThrustState) {
            tv.x = -thrustAccel * Math.sin(radians.toDouble()).toFloat()
            tv.y = thrustAccel * Math.cos(radians.toDouble()).toFloat()
        } else {
            tv.z = (if (state is ShipLeftThrustState) -thrustAccel else thrustAccel)
            tv.x = -(thrustAccel / 7f) * Math.sin(radians.toDouble()).toFloat()
            tv.y = thrustAccel / 7f * Math.cos(radians.toDouble()).toFloat()
        }

        return tv
    }

    fun thrustForInput(state: ShipState): ThrustParticle.Thrust? {
        if (state is ShipBothThrustState)
            return ThrustParticle.Thrust.Both
        else if (state is ShipLeftThrustState)
            return ThrustParticle.Thrust.Left
        else if (state is ShipRightThrustState)
            return ThrustParticle.Thrust.Right

        return null
    }

    override fun load(spriteSheet: TextureAtlas) {

        var kshipSprites: Array<TextureAtlas.AtlasRegion> = spriteSheet.regions

        sprite_r = kshipSprites.get(0)
        sprite_n = kshipSprites.get(1)
        sprite_l = kshipSprites.get(2)
        sprite_lr = kshipSprites.get(3)
    }

    /* Viewable */
    override fun getView(): TextureAtlas.AtlasRegion {
        return spriteCurrent ?: sprite_n
    }

    /* Controllable */
    override fun receiveInput(input: ControllerInput) {
        controllerInput = input
    }

    /* Dynamic */
    override fun update(delta: Float) {
        spriteCurrent = sprite_n
        controllerInput?.let {
            state = state.newStateForInput(it)
            //Flight assist: kill rotational velocity if either both or neither thrusters activated
//            if ((it.left && it.right) || !(it.left || it.right)) {
//                scaleRotation(.98f)
//            }
        }

        state.update(this, delta)

        setPosition(getPosition().add(getVelocity()))
    }

    private fun scaleRotation(scalar: Float) {
        var velocity = getVelocity()

        velocity.z = velocity.z * scalar
        setVelocity(velocity)
    }

    /* Plotted */
    override fun getPosition(): Vector3 {
        return pos
    }

    override fun setPosition(position: Vector3) {
        pos = position
    }

    override fun getCenter(): Vector2 {
        return Vector2(pos.x + dim.x/2, pos.y + dim.y/2)
    }

    override fun getSize(): Vector2 {
        return dim
    }

    /* Motile */
    override fun getVelocity(): Vector3 {
        return vel
    }

    override fun setVelocity(velocity: Vector3) {
        vel = velocity
    }

    override fun setAcceleration(acceleration: Float) {
        accel = acceleration
    }

    override fun setThrustSprites(atlas: TextureAtlas) {
        thrustAtlas = atlas
    }

    override fun generateThrust(): ThrustParticle? {
        return thrustForInput(state)?.let {
            val particle = ThrustParticle(Vector2(dim), it, thrustAtlas)
            particle.setPosition(Vector3(pos))
            particle
        }
    }
}

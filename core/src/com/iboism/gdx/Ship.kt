package com.iboism.gdx

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array

/**
 * Created by Calm on 5/24/2017.
 */

class Ship: Viewable, Controllable, Dynamic, Plotted, Motile  {

    private lateinit var sprite_lr: TextureAtlas.AtlasRegion
    private lateinit var sprite_l: TextureAtlas.AtlasRegion
    private lateinit var sprite_r: TextureAtlas.AtlasRegion
    private lateinit var sprite_n: TextureAtlas.AtlasRegion

    private var sprite_current: TextureAtlas.AtlasRegion? = null

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
            spriteSheetFileName: String,
            dimensions: Vector2) {
        load(spriteSheetFileName)
        dim = dimensions


    }

    override fun load(spriteSheet: String) {
        val assets = AssetManager()
        assets.load<TextureAtlas>(spriteSheet, TextureAtlas::class.java)
        assets.finishLoading()

        val kshipAtlas: TextureAtlas = assets.get(spriteSheet)
        var kshipSprites: Array<TextureAtlas.AtlasRegion> = kshipAtlas.regions

        sprite_r = kshipSprites.get(0)
        sprite_n = kshipSprites.get(1)
        sprite_l = kshipSprites.get(2)
        sprite_lr = kshipSprites.get(3)
    }


    /* Viewable */
    override fun getView(): TextureAtlas.AtlasRegion {
        return sprite_current?: sprite_n
    }

    /* Controllable */
    override fun receiveInput(input: ControllerInput) {
        controllerInput = input
    }

    /* Dynamic */
    override fun update(delta: Float) {
        // Do math update velocity, position, sprite

        sprite_current = sprite_n

        controllerInput?.let {
            sprite_current = spriteFor(it)
            val thrustDelta = thrustVectorFor(it, accel)
            setVelocity(getVelocity().add(thrustDelta.scl(delta)))
        }

        setPosition(getPosition().add(getVelocity()))
    }

    private fun thrustVectorFor(input: ControllerInput, thrustAccel: Float): Vector3 {
        val radians = getPosition().z * Math.PI.toFloat() / 180f
        var tv = Vector3()
        if (input.right && input.left) {
            tv.x = -thrustAccel * Math.sin(radians.toDouble()).toFloat()
            tv.y = thrustAccel * Math.cos(radians.toDouble()).toFloat()
        } else {
            tv.z = (if (input.left) -thrustAccel else thrustAccel)
            tv.x = -(thrustAccel / 7f) * Math.sin(radians.toDouble()).toFloat()
            tv.y = thrustAccel / 7f * Math.cos(radians.toDouble()).toFloat()
        }

        return tv
    }

    private fun spriteFor(input: ControllerInput): TextureAtlas.AtlasRegion? {
        if (input.right && input.left)
            return sprite_lr
        else if (input.right || input.left)
            return if (input.left) sprite_l else sprite_r

        return sprite_n
    }

    /* Plotted */
    override fun getPosition(): Vector3 {
        return pos
    }

    override fun setPosition(position: Vector3) {
        pos = position
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
}

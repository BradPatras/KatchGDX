package com.iboism.gdx

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array



/**
 * Created by Brad on 11/13/2017.
 */
class Besucher(
        atlas: TextureAtlas,
        dimensions: Vector2
) :  Viewable, Dynamic, Plotted, Motile, VisiblyThrusted, FlightAssistEquipped {

    private var spriteCurrent: TextureAtlas.AtlasRegion? = null

    private lateinit var thrustAtlas: TextureAtlas

    /* SHIP VALUES */

    /*
	Dimensions
	 */
    var dim: Vector2 = dimensions

    /*
    Position
     */
    var pos: Vector3 = Vector3()

    /*
    Velocity
     */
    var vel: Vector3 = Vector3(0f,10f,6f)

    /*
    Acceleration
     */
    var accel: Float = 0f

    override fun load(spriteSheet: TextureAtlas) {
        var shipSprites: Array<TextureAtlas.AtlasRegion> = spriteSheet.regions

        spriteCurrent = shipSprites[0]

    }

    override fun getView(): TextureAtlas.AtlasRegion? {
        return spriteCurrent
    }

    override fun update(delta: Float) {
        setPosition(getPosition().add(getVelocity()))
    }

    override fun getPosition(): Vector3 {
        return pos
    }

    override fun setPosition(position: Vector3) {
        pos = position
    }

    override fun getSize(): Vector2 {
        return dim
     }

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
        var thrust =  nextThrust()
        val particle = ThrustParticle(Vector2(dim), thrust, thrustAtlas)
        particle.setPosition(Vector3(pos))
        return particle
    }

    private fun nextThrust() : ThrustParticle.Thrust {
        return ThrustParticle.Thrust.None
    }

    init {
        load(atlas)
    }
}
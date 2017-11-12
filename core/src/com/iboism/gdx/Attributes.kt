package com.iboism.gdx

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

/**
 * Created by Brad on 11/3/2017.
 */
interface Viewable {
    fun load(spriteSheet: TextureAtlas)
    fun getView(): TextureAtlas.AtlasRegion?
}

interface Controllable {
    fun receiveInput(input: ControllerInput)
}

interface Dynamic {
    fun update(delta: Float)
}

interface Plotted {
    fun getPosition(): Vector3
    fun setPosition(position: Vector3)
    fun getSize(): Vector2
}

interface Motile {
    fun getVelocity(): Vector3
    fun setVelocity(velocity: Vector3)
    fun setAcceleration(acceleration: Float)
}

interface Mortal {
    fun isDead(): Boolean
}

interface VisiblyThrusted {
    fun generateThrust(): ThrustParticle?
    fun setThrustSprites(atlas: TextureAtlas)
}

interface DynamicVisibility {
    fun getOpacity(): Float
}

interface MainCharacter {
    fun getCenter(): Vector2
    fun getVelocity(): Vector3
}

interface FlightAssistEquipped
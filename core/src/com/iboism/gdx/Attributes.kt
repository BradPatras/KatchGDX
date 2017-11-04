package com.iboism.gdx

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector3

/**
 * Created by Brad on 11/3/2017.
 */
interface Viewable {
    fun load(spriteSheet: String)
    fun getView(): TextureAtlas.AtlasRegion?
}

interface Controllable {
    fun processInput(input: ControllerInput)
}

interface Dynamic {
    fun update()
}

interface Plotted {
    fun getPosition(): Vector3
}

interface Motile {
    fun getVelocity(): Vector3
    fun getAcceleration(): Vector3
}

interface Mortal {
    fun isDead(): Boolean
}
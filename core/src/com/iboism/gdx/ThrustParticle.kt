package com.iboism.gdx

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array

/**
 * Created by Brad on 11/5/17.
 */
class ThrustParticle: Viewable, Dynamic, Mortal, Plotted, DynamicVisibility {

    var dim = Vector2()
    var initialDim = Vector2()
    var life: Int = 0
    var initialLife: Int = 0
    var pos = Vector3()
    var thrust: Thrust?

    lateinit var particleSprites: Array<TextureAtlas.AtlasRegion>

    enum class Thrust(val index: Int) {
        Left(2),
        Right(0),
        Both(1),
        None(3)
    }

    constructor(size: Vector2, thrust: Thrust, atlas: TextureAtlas, lifetime: Int = 100) {
        load(atlas)
        dim = size
        initialDim = size
        life = lifetime
        initialLife = lifetime
        this.thrust = thrust
    }

    override fun load(atlas: TextureAtlas) {
        particleSprites = atlas.regions
    }

    override fun getView(): TextureAtlas.AtlasRegion? {
        return thrust?.let { particleSprites[it.index] }
    }

    override fun update(delta: Float) {
        // slowly kill off the poor lil fella
        life -= 1
    }

    override fun isDead(): Boolean {
        return life < 1
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

    override fun getOpacity(): Float {
        return life.toFloat()/initialLife.toFloat()
    }
}
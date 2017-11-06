package com.iboism.gdx

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array

/**
 * Created by Brad on 11/5/17.
 */
class ThrustParticle: Viewable, Dynamic, Mortal, Plotted {

    var dim = Vector2()

    var initialDim = Vector2()

    var life: Int = 0
    var initialLife: Int = 0
    var pos = Vector3()

    lateinit var sprite: TextureAtlas.AtlasRegion


    constructor(spriteFilename: String, size: Vector2, lifetime: Int = 100) {
        load(spriteFilename)
        dim = size
        initialDim = size
        life = lifetime
    }

    override fun load(spriteSheet: String) {
        val assets = AssetManager()
        assets.load<TextureAtlas>(spriteSheet, TextureAtlas::class.java)
        assets.finishLoading()

        val particleAtlas: TextureAtlas = assets.get(spriteSheet)
        var particleSprites: Array<TextureAtlas.AtlasRegion> = particleAtlas.regions

        sprite = particleSprites.get(0)
    }

    override fun getView(): TextureAtlas.AtlasRegion {
        return sprite
    }

    override fun update(delta: Float) {
        // slowly kill off the poor lil fella
        dim.sub(1f,1f)
        life - 1
    }

    override fun isDead(): Boolean {
        return life > 0
    }

    override fun getPosition(): Vector3 {
        return pos
    }

    override fun setPosition(position: Vector3) {
        pos = position
    }

    override fun getSize(): Vector2 {
        if (initialLife == 0) return dim
        return dim.scl(life.toFloat()/initialLife.toFloat())
    }
}
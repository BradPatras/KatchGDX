package com.iboism.gdx

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Array

/**
 * Created by Calm on 5/24/2017.
 */

class Ship{
    private val assets: AssetManager = AssetManager()
    private val kshipAtlas: TextureAtlas
    internal var kshipSprites: Array<TextureAtlas.AtlasRegion>
    internal var sprite_lr: TextureAtlas.AtlasRegion
    internal var sprite_l: TextureAtlas.AtlasRegion
    internal var sprite_r: TextureAtlas.AtlasRegion
    internal var sprite_n: TextureAtlas.AtlasRegion
    internal var sprite_current: TextureAtlas.AtlasRegion? = null

    /* SHIP VALUES

	Dimensions
	 */
    var height: Float = 0f
    var width: Float = 0f

    /*
    Position
     */
    var x: Float = 0f
    var y: Float = 0f
    var r: Float = 0f

    /*
    Velocity
     */
    var xv: Float = 0f
    var yv: Float = 0f
    var rv: Float = 0f

    init {
        assets.load<TextureAtlas>("kship2.pack", TextureAtlas::class.java)
        assets.finishLoading()

        kshipAtlas = assets.get("kship2.pack")
        kshipSprites = kshipAtlas.regions

        sprite_r = kshipSprites.get(0)
        sprite_n = kshipSprites.get(1)
        sprite_l = kshipSprites.get(2)
        sprite_lr = kshipSprites.get(3)

    }
}

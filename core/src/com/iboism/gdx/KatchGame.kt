package com.iboism.gdx

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.utils.Array

import java.util.ArrayList

class KatchGame : ApplicationAdapter(), InputProcessor {
    private var batch: SpriteBatch? = null

    //stuff that shouldn't be here
    private var isLeftThusting = false
    private var isRightThrusting = false
    private lateinit var background: TextureRegion

    private val actors = ArrayList<Any>()

    private lateinit var camera: OrthographicCamera

    /* GAME STUFF */
    private var game_height: Float = 0f
    private var game_width: Float = 0f

    private var world_rect: Rectangle = Rectangle()

    private var thrust_accel = 8f

    private val CAMERA_ZOOM_MIN = 1f
    private val CAMERA_ZOOM_MAX = 2.5f
    private val CAMERA_ZOOM_THRESHOLD = thrust_accel * 5.5f

    override fun create() {
        Gdx.input.inputProcessor = this
        batch = SpriteBatch()

        game_height = Gdx.graphics.height.toFloat()
        game_width = Gdx.graphics.width.toFloat()

        camera = OrthographicCamera(game_width, game_height)
        camera.zoom = CAMERA_ZOOM_MIN

        /*
		generate dimensions based on screen size
		 */
        val width = Gdx.graphics.width / 8f
        val height = width * .6f

        val assets = AssetManager()
        assets.load<TextureAtlas>("kship2.pack", TextureAtlas::class.java)
        assets.load<TextureAtlas>("thrust.pack", TextureAtlas::class.java)
        assets.load<TextureAtlas>("besucher2.pack", TextureAtlas::class.java)
        assets.load<TextureAtlas>("besucherthrustt2.pack", TextureAtlas::class.java)
        assets.finishLoading()

        // background
        val bk = Texture("pixelspacebig2.png")
        bk.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
        background = TextureRegion(bk)
        background.setRegion(0, 0, bk.width * 100, bk.height * 100)
        world_rect = Rectangle(0f, 0f, bk.width * 100f, bk.height * 100f)
        val worldC = world_rect.getCenter(Vector2.Zero)

        // besucher
        val bHeight = height * 1.5f
        val bWidth = bHeight
        val besucher = Besucher(assets.get("besucher2.pack"), Vector2(bWidth, bHeight))
        besucher.pos = Vector3(worldC.x, worldC.y - bHeight * 2, 0f)
        besucher.setThrustSprites(assets.get("besucherthrustt2.pack"))
        actors.add(besucher)

        // kShip
        val kship = Ship(assets.get("kship2.pack"), Vector2(width, height))
        kship.pos =  Vector3(worldC.x, worldC.y, 0f)
        kship.accel = thrust_accel
        kship.setThrustSprites(assets.get("thrust.pack"))
        actors.add(kship)
    }

    override fun render() {
        Gdx.gl.glClearColor(.5f, .5f, .5f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch!!.projectionMatrix = camera.combined
        batch!!.begin()

        batch!!.draw(background, -Gdx.graphics.width.toFloat(), -Gdx.graphics.height.toFloat(), background.regionWidth * 5f, background.regionHeight * 5f)

        val toAdd = ArrayList<Any>()

        actors.forEach {
            if (it is Dynamic) {
                it.update(Gdx.graphics.deltaTime)
            }

            if (it is VisiblyThrusted && it.generateThrust() != null) {
                toAdd.add(it.generateThrust()!!)
            }

            if (it is Viewable && it is Plotted && it.getView() != null) {

                batch!!.setColor(1f,1f,1f, (it as? DynamicVisibility)?.getOpacity() ?: 1f)

                batch!!.draw(it.getView(),
                        it.getPosition().x, it.getPosition().y,
                        it.getSize().x / 2f, it.getSize().y / 2f,
                        it.getSize().x, it.getSize().y,
                        1f, 1f,
                        it.getPosition().z)

               batch!!.setColor(1f,1f,1f,1f)
            }

            if (it is MainCharacter) {
                camera.position.set(it.getCenter().x,it.getCenter().y,0f)

                if (it.getVelocity().len() > CAMERA_ZOOM_THRESHOLD) {
                    var speedZoom = CAMERA_ZOOM_MIN + (it.getVelocity().len() - CAMERA_ZOOM_THRESHOLD) / 30f
                    camera.zoom = speedZoom //if (speedZoom > CAMERA_ZOOM_MAX) CAMERA_ZOOM_MAX else speedZoom
                }
            }
        }

        camera.update()
        batch!!.end()

        actors.addAll(0, toAdd)
        actors.removeAll { it is Mortal && it.isDead() }
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (screenX < Gdx.graphics.width / 2) {
            // Touched left side of screen
            isLeftThusting = true
        } else {
            // Touched right side of screen
            isRightThrusting = true
        }

        actors.filterIsInstance<Controllable>()
                .forEach {
                    it.receiveInput(ControllerInputCreator(isLeftThusting, isRightThrusting, false).create())
                }

        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (screenX < Gdx.graphics.width / 2) {
            // Stopped touching left side of screen
            isLeftThusting = false
        } else {
            // Stopped touching right side of screen
            isRightThrusting = false
        }

        actors.filterIsInstance<Controllable>().forEach {
            it.receiveInput(ControllerInputCreator(isLeftThusting, isRightThrusting, false).create())
        }

        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}

fun Rectangle.getCenter(): Vector2 {
    return this.getCenter(Vector2.Zero)
}

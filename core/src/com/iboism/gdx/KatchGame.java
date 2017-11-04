package com.iboism.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

public class KatchGame extends ApplicationAdapter implements InputProcessor {
	private SpriteBatch batch;

	private boolean isLeftThusting = false;
	private boolean isRightThrusting = false;

	private Ship kship;

	private TextureAtlas kshipAtlas;
	private Array<TextureAtlas.AtlasRegion> kshipSprites;
	private float elapsed;
	OrthographicCamera camera;

	/* GAME STUFF */
	float game_height;
	float game_width;

	float thrust_accel = 5f;
	
	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);
		batch = new SpriteBatch();

		game_height = Gdx.graphics.getHeight();
		game_width = Gdx.graphics.getWidth();

		camera = new OrthographicCamera(game_width,game_height);

		/*
		generate dimensions based on screen size
		 */
		float width = Gdx.graphics.getWidth() / 6f;
		float height = width * .6f;

		kship = new Ship("kship2.pack", new Vector2(width, height));
		kship.setPos(new Vector3(Gdx.graphics.getWidth() / 2,Gdx.graphics.getHeight() / 2, 0));
		kship.setAccel(thrust_accel);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.5f, .5f, .5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		kship.update(Gdx.graphics.getDeltaTime());
		batch.begin();
		batch.draw(kship.getView(), kship.getPos().x, kship.getPos().y, kship.getSize().x /2f, kship.getSize().y /2f, kship.getSize().x, kship.getSize().y,1,1, kship.getPosition().z);
		batch.end();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (screenX < (Gdx.graphics.getWidth()/2)){
			// Touched left side of screen
			isLeftThusting = true;
		} else {
			// Touched right side of screen
			isRightThrusting = true;
		}
		kship.receiveInput(new ControllerInput(isLeftThusting,isRightThrusting,false));
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (screenX < (Gdx.graphics.getWidth()/2)){
			// Stopped touching left side of screen
			isLeftThusting = false;
		} else {
			// Stopped touching right side of screen
			isRightThrusting = false;
		}
		kship.receiveInput(new ControllerInput(isLeftThusting,isRightThrusting,false));
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}

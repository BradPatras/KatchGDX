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
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

public class KatchGame extends ApplicationAdapter implements InputProcessor {
	private SpriteBatch batch;

	private boolean isLeftThusting;
	private boolean isRightThrusting;

	private Ship kship = new Ship();

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
		kship.setWidth(Gdx.graphics.getWidth() / 6f);
		kship.setHeight(kship.getWidth() * .6f);

		kship.setX(Gdx.graphics.getWidth() / 2);
		kship.setY(Gdx.graphics.getHeight() / 2);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.5f, .5f, .5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		updateShip(Gdx.graphics.getDeltaTime());
		batch.begin();
		batch.draw(kship.getSprite_current(), kship.getX(), kship.getY(), kship.getWidth() /2f, kship.getHeight() /2f, kship.getWidth(), kship.getHeight(),1,1, kship.getR());
		batch.end();
	}


	private void updateShip(float deltaTime){
		float radians = kship.getR() * ((float) Math.PI) / 180f;
		float thrustdx = 0;
		float thrustdy = 0;
		kship.setSprite_current(kship.getSprite_n());

		if (isRightThrusting && isLeftThusting){
			thrustdx = -thrust_accel * ((float) Math.sin(radians));
			thrustdy = (thrust_accel * ((float) Math.cos(radians)));
			kship.setSprite_current(kship.getSprite_lr());
		} else if (isLeftThusting || isRightThrusting){
			kship.setSprite_current((isLeftThusting) ? kship.getSprite_l() : kship.getSprite_r());
			kship.setRv(kship.getRv() + ((isLeftThusting) ? -thrust_accel : thrust_accel) * deltaTime); //calculate new rotational velocity
			thrustdx = -(thrust_accel / 7f) * ((float) Math.sin(radians));
			thrustdy = ((thrust_accel / 7f) * ((float) Math.cos(radians)));
		}

		kship.setXv(kship.getXv() + thrustdx * deltaTime); //calculate new horizontal velocity
		kship.setYv(kship.getYv() + thrustdy * deltaTime); //calculate new vertical velocity

		//Apply new velocities to ship position and rotation
		kship.setY(kship.getY() + kship.getYv());
		kship.setX(kship.getX() + kship.getXv());
		kship.setR(kship.getR() + kship.getRv());

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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (screenX < (Gdx.graphics.getWidth()/2)){
			// Touched left side of screen
			isLeftThusting = true;
		} else {
			// Touched right side of screen
			isRightThrusting = true;
		}
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

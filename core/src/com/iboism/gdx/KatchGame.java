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
	private AssetManager assets;SpriteBatch batch;

	private boolean isLeftThusting;
	private boolean isRightThrusting;

	private TextureAtlas kshipAtlas;
	private Array<TextureAtlas.AtlasRegion> kshipSprites;
	private TextureAtlas.AtlasRegion sprite_lr;
	private TextureAtlas.AtlasRegion sprite_l;
	private TextureAtlas.AtlasRegion sprite_r;
	private TextureAtlas.AtlasRegion sprite_n;
	private TextureAtlas.AtlasRegion sprite_current;
	private float elapsed;
	OrthographicCamera camera;

	/* GAME STUFF */
	float game_height;
	float game_width;

	float thrust_accel = 5f;

	/* SHIP STUFF

	Dimensions
	 */
	float kship_height;
	float kship_width;

	/*
	Position
	 */
	float kship_x;
	float kship_y;
	float kship_r;

	/*
	Velocity
	 */
	float kship_xv;
	float kship_yv;
	float kship_rv;

	
	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);
		batch = new SpriteBatch();
		assets = new AssetManager();
		assets.load("kship2.pack", TextureAtlas.class);
		assets.finishLoading();
		/*
		assign sprites from the texture atlas
		 */

		kshipAtlas = assets.get("kship2.pack");
		kshipSprites = kshipAtlas.getRegions();

		sprite_r = kshipSprites.get(0);
		sprite_n = kshipSprites.get(1);
		sprite_l = kshipSprites.get(2);
		sprite_lr = kshipSprites.get(3);

		game_height = Gdx.graphics.getHeight();
		game_width = Gdx.graphics.getWidth();

		camera = new OrthographicCamera(game_width,game_height);

		/*
		generate dimensions based on screen size
		 */
		kship_width = Gdx.graphics.getWidth()/6f;
		kship_height = kship_width * .6f;

		kship_x = Gdx.graphics.getWidth()/2;
		kship_y = Gdx.graphics.getHeight()/2;





	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.5f, .5f, .5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		updateShip(Gdx.graphics.getDeltaTime());
		batch.begin();
		batch.draw(sprite_current,kship_x,kship_y,kship_width/2f,kship_height/2f,kship_width,kship_height,1,1,kship_r);
		batch.end();
	}


	private void updateShip(float deltaTime){
		float radians = kship_r * ((float) Math.PI) / 180f;
		float thrustdx = 0;
		float thrustdy = 0;
		sprite_current = sprite_n;

		if (isRightThrusting && isLeftThusting){
			thrustdx = -thrust_accel * ((float) Math.sin(radians));
			thrustdy = (thrust_accel * ((float) Math.cos(radians)));
			sprite_current = sprite_lr;
		} else if (isLeftThusting || isRightThrusting){
			sprite_current = (isLeftThusting) ? sprite_l : sprite_r;
			kship_rv += ((isLeftThusting) ? -thrust_accel : thrust_accel)*deltaTime; //calculate new rotational velocity
			thrustdx = -(thrust_accel / 7f) * ((float) Math.sin(radians));
			thrustdy = ((thrust_accel / 7f) * ((float) Math.cos(radians)));
		}

		kship_xv += thrustdx * deltaTime; //calculate new horizontal velocity
		kship_yv += thrustdy * deltaTime; //calculate new vertical velocity

		//Apply new velocities to ship position and rotation
		kship_y += kship_yv;
		kship_x += kship_xv;
		kship_r += kship_rv;

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

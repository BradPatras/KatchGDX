package com.iboism.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class KatchGame extends ApplicationAdapter implements GestureDetector.GestureListener {
	private AssetManager assets;SpriteBatch batch;


	private TextureAtlas kshipAtlas;
	private Array<TextureAtlas.AtlasRegion> kshipSprites;
	private float elapsed;
	OrthographicCamera camera;

	/* GAME STUFF */
	float game_height;
	float game_width;


	/* SHIP STUFF */
	/*
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
		Gdx.input.setInputProcessor(new GestureDetector(this));
		batch = new SpriteBatch();
		assets = new AssetManager();
		assets.load("kship.pack", TextureAtlas.class);
		assets.finishLoading();

		game_height = Gdx.graphics.getHeight();
		game_width = Gdx.graphics.getWidth();

		kshipAtlas = assets.get("kship.pack");
		kshipSprites = kshipAtlas.getRegions();

		camera = new OrthographicCamera(game_width,game_height);



	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		batch.end();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}
}

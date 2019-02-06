package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;

public class Frogger extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture img;
	private TiledMap map;
	private OrthoCachedTiledMapRenderer mapRenderer;
	private OrthographicCamera camera;

	
	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.img = new Texture("core/assets/shark.png");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.camera = new OrthographicCamera();
		this.camera.viewportHeight = Gdx.graphics.getHeight();
		this.camera.viewportWidth = Gdx.graphics.getWidth();
		this.camera.update();
		TmxMapLoader loader = new TmxMapLoader();
		this.map = loader.load("core/Map/Map.tmx");

		this.mapRenderer = new OrthoCachedTiledMapRenderer(this.map);
		this.mapRenderer.setView(this.camera);
		this.mapRenderer.render();
		this.batch.begin();
		this.batch.draw(img, 0, 0);
		this.batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}

package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Frogger extends ApplicationAdapter {
	private SpriteBatch batch;
	private TiledMap map;
	private OrthoCachedTiledMapRenderer mapRenderer;
	private OrthographicCamera camera;
	private Player player;
	private Viewport viewport;

	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();
		this.camera.position.set(480/2,384/2,0);
		this.viewport = new FitViewport(480,384,camera);
		this.player = new Player(new Sprite(new Texture("core/assets/cat_back.png")));
	}

	@Override
	public void resize(int width, int height){
		this.viewport.update(width,height);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(220/255,220/255,220/255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		TmxMapLoader loader = new TmxMapLoader();
		this.map = loader.load("core/Map/Map.tmx");

		this.mapRenderer = new OrthoCachedTiledMapRenderer(this.map);
		this.mapRenderer.setView(this.camera);
		this.mapRenderer.render();
		this.batch.begin();
		this.player.draw(this.batch);
		this.batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}

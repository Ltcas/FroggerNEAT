package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
	private TmxMapLoader loader;
	private int width;
	private int height;

	public Frogger(int width,int height){
		this.width = width;
		this.height = height;
	}

	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();
		this.camera.position.set(this.width/2,this.height/2,0);
		this.viewport = new FitViewport(this.width,this.height,camera);
		this.loader = new TmxMapLoader();
		this.map = this.loader.load("core/Map/Map.tmx");
		this.player = new Player(new Sprite(
				new Texture("core/assets/cat_back.png")),this.width,this.height,
				(TiledMapTileLayer)this.map.getLayers().get("level"));
		this.mapRenderer = new OrthoCachedTiledMapRenderer(this.map);
	}

	@Override
	public void resize(int width, int height){
		this.viewport.update(width,height);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(220/255,220/255,220/255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		this.mapRenderer.setView(this.camera);
		this.mapRenderer.render();
		this.batch.begin();
		this.player.draw(this.batch);
		this.batch.end();
	}
	
	@Override
	public void dispose () {
		this.batch.dispose();
		this.map.dispose();
	}
}

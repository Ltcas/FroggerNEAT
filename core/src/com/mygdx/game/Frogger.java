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
	private Car car;
	private Car carTwo;
	private Car carThree;
	private Car bus;
	private Platform turtle1;
	private Platform turtle2;
	private Platform log3;
	private Platform log4;
	private Platform log5;
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


		this.car = new Car(new Sprite(new Texture("core/assets/racecar.png")),this.width,64,-1,5);
		this.carTwo = new Car(new Sprite(new Texture("core/assets/yellow_car.png")),0,32,1,3);
		this.carThree = new Car(new Sprite(new Texture("core/assets/yellow_car.png")),0,96,1,4);
		this.bus = new Car(new Sprite(new Texture("core/assets/bus.png")),this.width,128,-1,2);

		this.turtle1 = new Platform(new Sprite(new Texture("core/assets/turtle.png")), this.width, 192, -1, 2);
		this.log5 = new Platform(new Sprite(new Texture("core/assets/log5.png")), 0, 224, 1, 2);
		this.turtle2 = new Platform(new Sprite(new Texture("core/assets/turtle.png")), this.width, 256, -1, 2);
		this.log4 = new Platform(new Sprite(new Texture("core/assets/log4.png")), 0, 288, 1, 3);
		this.log3 = new Platform(new Sprite(new Texture("core/assets/log3.png")), this.width, 320, -1, 4);

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
		this.car.update();
		this.carTwo.update();
		this.carThree.update();
		this.bus.update();
		this.turtle1.update();
		this.turtle2.update();
		this.log3.update();
		this.log4.update();
		this.log5.update();
		this.player.draw(this.batch);
		this.car.draw(this.batch);
		this.carTwo.draw(this.batch);
		this.carThree.draw(this.batch);
		this.bus.draw(this.batch);
		this.turtle1.draw(this.batch);
		this.turtle2.draw(this.batch);
		this.log3.draw(this.batch);
		this.log4.draw(this.batch);
		this.log5.draw(this.batch);
		this.batch.end();
	}
	
	@Override
	public void dispose () {
		this.batch.dispose();
		this.map.dispose();
	}
}

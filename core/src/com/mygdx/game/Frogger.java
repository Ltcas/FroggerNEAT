package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.*;
import java.util.ArrayList;

public class Frogger extends ApplicationAdapter {
	private SpriteBatch batch;
	private TiledMap map;
	private OrthoCachedTiledMapRenderer mapRenderer;
	private OrthographicCamera camera;

	private ArrayList<Player> players;
	private ArrayList<Platform> platforms;
	private ArrayList<Car> cars;

	private Viewport viewport;
	private TmxMapLoader loader;
	private int width;
	private int height;
	private BitmapFont scoreDisplay;


	public Frogger(int width,int height){
		this.width = width;
		this.height = height;
		this.players = new ArrayList<Player>();
		this.platforms = new ArrayList<Platform>();
		this.cars = new ArrayList<Car>();
		this.scoreDisplay = new BitmapFont(Gdx.files.internal("core/Fonts/font.fnt"));
	}

	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();
		this.camera.position.set(this.width/2,this.height/2,0);
		this.viewport = new FitViewport(this.width,this.height,camera);
		this.loader = new TmxMapLoader();
		this.map = this.loader.load("core/Map/Map.tmx");
		this.addPlatforms();
		this.addCars();
		this.addPlayers();
		this.mapRenderer = new OrthoCachedTiledMapRenderer(this.map);
	}

	private void addPlayers(){
		this.players.add(new Player(new Sprite(new Texture("core/assets/cat_back.png")),
				this.width,this.height,
				(TiledMapTileLayer)this.map.getLayers().get("level"),this.cars,this.platforms));
	}

	private void addCars(){
		this.cars.add(new Car(new Sprite(new Texture("core/assets/racecar.png")),this.width,64,-1,5));
		this.cars.add(new Car(new Sprite(new Texture("core/assets/yellow_car.png")),0,32,1,3));
		this.cars.add(new Car(new Sprite(new Texture("core/assets/yellow_car.png")),0,96,1,4));
		this.cars.add(new Car(new Sprite(new Texture("core/assets/bus.png")),this.width,128,-1,2));

	}

	private void addPlatforms(){
		this.platforms.add(new Platform(new Sprite(new Texture("core/assets/turtle.png")), this.width, 192, -1, 2));
		this.platforms.add(new Platform(new Sprite(new Texture("core/assets/log5.png")), -160, 224, 1, 2));
		this.platforms.add(new Platform(new Sprite(new Texture("core/assets/turtle.png")), this.width, 256, -1, 2));
		this.platforms.add(new Platform(new Sprite(new Texture("core/assets/log4.png")), -128, 288, 1, 3));
		this.platforms.add(new Platform(new Sprite(new Texture("core/assets/log3.png")), this.width, 320, -1, 4));

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
		this.updateAll();
		this.batch.end();
	}

	private void updateAll(){
		for(Platform platform:this.platforms){
			platform.update();
			platform.draw(this.batch);
		}

		for(Car car:this.cars){
			car.update();
			car.draw(this.batch);
		}
		int maxScore = 0;
		for (Player player: this.players){
			player.update();
			if(player.getScore() > maxScore){
				maxScore = player.getScore();
			}
			player.draw(this.batch);
		}
		this.scoreDisplay.setColor(1.0f,1.0f,1.0f,1.0f);
		this.scoreDisplay.draw(this.batch,"Max Score" + maxScore,0,0);
	}

	@Override
	public void dispose () {
		this.batch.dispose();
		this.map.dispose();
	}
}

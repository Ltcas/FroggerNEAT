package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import java.util.ArrayList;

/**
 * Contains the game logic for a game of "Kittener" which is based off of the classic arcade game
 * of "Frogger".
 * @author Chance Simmons and Brandon Townsend
 * @version 14 February 2019
 */
public class Kitten extends ApplicationAdapter {

	/** Contains every single sprite that we are going to render. */
	private SpriteBatch batch;

	/** Representation of the game board. */
	private TiledMap map;

	/** Class that renders the map. */
	private OrthoCachedTiledMapRenderer mapRenderer;

	/** Class that controls the placement of the camera. */
	private OrthographicCamera camera;

	/** Class that controls where the placement camera is inside the window. */
	private Viewport viewport;

	/** Contains the list of player objects. */
	private ArrayList<Player> players;

	/** Contains the list of platform objects. */
	private ArrayList<Platform> platforms;

	/** Contains the list of car objects. */
	private ArrayList<Car> cars;

	/** Representation for the width of the game view. */
	private int width;

	/** Representation for the height of the game view. */
	private int height;

	/** Bitmap Font that will give a representation of the score the players have gained. */
	private BitmapFont scoreDisplay;

	/** Representation of the number of players. */
	private int numPlayers;

	/**
	 * Constructs a new Kitten game object.
	 * @param width The initial width of the game window.
	 * @param height The initial height of the game window.
	 */
	public Kitten(int width, int height){
		this.width = width;
		this.height = height;
		this.players = new ArrayList<Player>();
		this.platforms = new ArrayList<Platform>();
		this.cars = new ArrayList<Car>();
		this.numPlayers = 15;
	}

	/**
	 * Loads the map, creates the camera, and sets up all players, platforms, and hazards that
	 * will be placed in the game.
	 */
	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();
		this.camera.position.set(this.width/2,this.height/2,0);
		this.viewport = new FitViewport(this.width,this.height,camera);
		TmxMapLoader loader = new TmxMapLoader();
		this.map = loader.load("core/Map/Map.tmx");
		this.addPlatforms();
		this.addCars();
		this.addPlayers();
		this.mapRenderer = new OrthoCachedTiledMapRenderer(this.map);
		this.scoreDisplay = new BitmapFont(Gdx.files.local("core/Fonts/font.fnt"));
		this.scoreDisplay.getData().setScale(.5f);
	}

	/**
	 * Adds a the number of players specified by numPlayers.
	 */
	private void addPlayers(){
		for(int i = 0;i < this.numPlayers;i++){
			this.players.add(new Player(this.width,this.height,
					(TiledMapTileLayer)this.map.getLayers().get("level"),this.cars,this.platforms));
		}
	}

	/**
	 * Adds the cars that will be moving across the game.
	 */
	//todo fix spawns and add more cars
	private void addCars(){
		this.cars.add(new Car(new Sprite(new Texture("core/assets/racecar.png")),this.width,64,-1,5));
		this.cars.add(new Car(new Sprite(new Texture("core/assets/yellow_car.png")),0,32,1,3));
		this.cars.add(new Car(new Sprite(new Texture("core/assets/yellow_car.png")),0,96,1,4));
		this.cars.add(new Car(new Sprite(new Texture("core/assets/bus.png")),this.width,128,-1,2));
	}

	/**
	 * Adds the platforms that will be moving across the game.
	 */
	//todo fix spawns and add more platforms.
	private void addPlatforms(){
		this.platforms.add(new Platform(new Sprite(new Texture("core/assets/turtle.png")), this.width, 192, -1, 2));
		this.platforms.add(new Platform(new Sprite(new Texture("core/assets/log5.png")), -160, 224, 1, 2));
		this.platforms.add(new Platform(new Sprite(new Texture("core/assets/turtle.png")), this.width, 256, -1, 2));
		this.platforms.add(new Platform(new Sprite(new Texture("core/assets/log4.png")), -128, 288, 1, 3));
		this.platforms.add(new Platform(new Sprite(new Texture("core/assets/log3.png")), this.width, 320, -1, 4));
	}

	/**
	 * Allows the resizing of the window and adjusts the viewport accordingly.
	 * @param width New width that the window will be sized to.
	 * @param height New width that the window will be sized to.
	 */
	@Override
	public void resize(int width, int height){
		this.viewport.update(width,height);
	}

	/**
	 * Renders the map and all sprites that belong to the sprite batch.
	 */
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

	/**
	 * Calls the update methods of all platforms, hazards, and players to adjust where they are
	 * appearing. Also updates the score the player currently has.
	 */
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
		this.scoreDisplay.setColor(Color.WHITE);
		this.scoreDisplay.draw(this.batch,"Max Score: " + maxScore,0,this.scoreDisplay.getCapHeight() + 32);
	}

	/**
	 * Disposes of all sprites in the batch and the map itself.
	 */
	@Override
	public void dispose () {
		this.batch.dispose();
		this.map.dispose();
	}
}

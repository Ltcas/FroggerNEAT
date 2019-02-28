package com.mygdx.game;

import NEAT.Population;
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

	/** Population for the NEAT algorithm. */
	private Population population;

	/**
	 * Constructs a new Kitten game object.
	 * @param width The initial width of the game window.
	 * @param height The initial height of the game window.
	 */
	public Kitten(int width, int height){
		this.width 		= width;
		this.height 	= height;
		this.players 	= new ArrayList<Player>();
		this.platforms 	= new ArrayList<Platform>();
		this.cars 		= new ArrayList<Car>();
		this.numPlayers = 20;
	}

	/**
	 * Loads the map, creates the camera, and sets up all players, platforms, and hazards that
	 * will be placed in the game.
	 */
	@Override
	public void create () {
		this.batch 	= new SpriteBatch();
		this.camera = new OrthographicCamera();
		this.camera.position.set(this.width/2,this.height/2,0);
		this.viewport = new FitViewport(this.width,this.height,camera);
		TmxMapLoader loader = new TmxMapLoader();
		this.map = loader.load("core/Map/Map.tmx");
		this.addPlatforms();
		this.addCars();
		this.addPlayers();
		this.population = new Population(this.numPlayers);
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
		int initEdgeLeft 	= 0;
		int initEdgeRight 	= this.width;
		int offset 			= 32;
		int directionLeft 	= -1;
		int directionRight 	= 1;
		int row 			= 32;

		Sprite racecar = new Sprite(new Texture("core/assets/racecar.png"));
		Sprite yellowCar = new Sprite(new Texture("core/assets/yellow_car.png"));
		Sprite bus = new Sprite(new Texture("core/assets/bus.png"));

		this.cars.add(new Car(yellowCar, initEdgeLeft, row, offset*-1, directionRight, 3));
		this.cars.add(new Car(yellowCar, initEdgeLeft, row, offset*-3, directionRight, 3));
		this.cars.add(new Car(yellowCar, initEdgeLeft, row, offset*-5, directionRight, 3));

		this.cars.add(new Car(racecar, initEdgeRight, row*2, offset, directionLeft, 6));

		this.cars.add(new Car(yellowCar, initEdgeLeft, row*3, offset*-1, directionRight, 4));
		this.cars.add(new Car(yellowCar, initEdgeLeft, row*3, offset*-3, directionRight, 4));
		this.cars.add(new Car(yellowCar, initEdgeLeft, row*3, offset*-5, directionRight, 4));

		this.cars.add(new Car(bus, initEdgeRight, row*4, offset, directionLeft,2));
		this.cars.add(new Car(bus, initEdgeRight, row*4, offset*5, directionLeft,2));
	}

	/**
	 * Adds the platforms that will be moving across the game.
	 */
	//todo fix spawns and add more platforms.
	private void addPlatforms(){
		int initEdgeLeft	= 0;
		int initEdgeRight	= this.width;
		int offset 			= 32;
		int directionLeft 	= -1;
		int directionRight 	= 1;
		int row 			= 32;

		Sprite turtle	= new Sprite(new Texture("core/assets/turtle.png"));
		Sprite log3		= new Sprite(new Texture("core/assets/log3.png"));
		Sprite log4 	= new Sprite(new Texture("core/assets/log4.png"));
		Sprite log5 	= new Sprite(new Texture("core/assets/log5.png"));

		this.platforms.add(new Platform(turtle, initEdgeRight, row*6, offset, directionLeft, 2));
		this.platforms.add(new Platform(turtle, initEdgeRight, row*6, offset*2, directionLeft, 2));
		this.platforms.add(new Platform(turtle, initEdgeRight, row*6, offset*5, directionLeft, 2));
		this.platforms.add(new Platform(turtle, initEdgeRight, row*6, offset*6, directionLeft, 2));

		this.platforms.add(new Platform(log5, initEdgeLeft, row*7, offset*-5, directionRight, 2));

		this.platforms.add(new Platform(turtle, initEdgeRight, row*8, offset, directionLeft, 4));
		this.platforms.add(new Platform(turtle, initEdgeRight, row*8, offset*2, directionLeft, 4));
		this.platforms.add(new Platform(turtle, initEdgeRight, row*8, offset*8, directionLeft, 4));
		this.platforms.add(new Platform(turtle, initEdgeRight, row*8, offset*9, directionLeft, 4));

		this.platforms.add(new Platform(log4, initEdgeLeft, row*9, offset*-4, directionRight, 3));
		this.platforms.add(new Platform(log4, initEdgeLeft, row*9, offset*-10, directionRight, 3));

		this.platforms.add(new Platform(log3, initEdgeRight, row*10, offset, directionLeft, 5));
		this.platforms.add(new Platform(log3, initEdgeRight, row*10, offset*7, directionLeft, 5));
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
		double maxScore = 0;
		for (Player player: this.players){
			if(player.isAlive()){
				player.update();
				if(player.getScore() > maxScore){
					maxScore = player.getScore();
				}
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

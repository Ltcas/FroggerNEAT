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
 * @version 5 March 2019
 */
public class Kittener extends ApplicationAdapter {

    /** Number of pixels for the height and width of a tile. */
    private final static int TILE_PIX = 32;

    /** Represents how a sprite will move to the left direction. */
    private final static int DIR_LEFT = -1;

    /** Represents how a sprite will move to the right direction. */
    private final static int DIR_RIGHT = 1;

	/** 2D array that is used to show hazards and tiles that can be moved on */
	private static int[][] mapVision;

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

	/** Holds the max score that the players have reached */
	private double maxOverAll;

	/**
	 * Constructs a new Kittener game object.
	 * @param width The initial width of the game window.
	 * @param height The initial height of the game window.
	 */
	public Kittener(int width, int height){
		Kittener.mapVision 	= new int[12][15];
		this.width 			= width;
		this.height 		= height;
		this.maxOverAll     = 0;
		this.players 		= new ArrayList<Player>();
		this.platforms 		= new ArrayList<Platform>();
		this.cars 			= new ArrayList<Car>();
		this.numPlayers 	= 20;
		this.initMapVision();
	}

	/**
	 * Initializes the map vision array with 1's and 0's. The 1's are for hazards and the 0's
	 * are for tiles that can be moved on.
	 */
	public void initMapVision(){
		for(int i = 0; i < Kittener.mapVision.length; i++){
			for(int j = 0; j < Kittener.mapVision[i].length; j++){
				if(i < 1 || i > 5){
					// Switch to floor for debugging.
					Kittener.mapVision[i][j] = MapObjects.FLOOR.getValue();
					//Kittener.mapVision[i][j] = MapObjects.HAZARD.getValue();
				}else{
					Kittener.mapVision[i][j] = MapObjects.FLOOR.getValue();
				}
			}
		}
	}

	/**
	 * Loads the map, creates the camera, and sets up all players, platforms, and hazards that
	 * will be placed in the game.
	 */
	@Override
	public void create () {
		this.batch 	= new SpriteBatch();
		this.camera = new OrthographicCamera();
		this.camera.position.set((float) this.width/2,(float) this.height/2,0);
		this.viewport = new FitViewport(this.width,this.height,camera);
		TmxMapLoader loader = new TmxMapLoader();
		this.map = loader.load("core/Map/MapNoWater.tmx");
		//this.addPlatforms();
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
		for(int i = 0; i < this.numPlayers; i++){
			this.players.add(new Player(this.width,this.height,
					(TiledMapTileLayer)this.map.getLayers().get("level"),this.cars,this.platforms));
		}
	}

	/**
	 * Adds the cars that will be moving across the game.
	 */
	private void addCars(){
		int initEdgeLeft 	= 0;
		int initEdgeRight 	= this.width;
		int offset 			= TILE_PIX;
		int row 			= TILE_PIX;

		Sprite racecar = new Sprite(new Texture("core/assets/racecar.png"));
		Sprite yellowCar = new Sprite(new Texture("core/assets/yellow_car.png"));
		Sprite bus = new Sprite(new Texture("core/assets/bus.png"));

//		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row, offset*-1, DIR_RIGHT, 3));
//		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row, offset*-3, DIR_RIGHT, 3));
//		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row, offset*-5, DIR_RIGHT, 3));
//
//		this.cars.add(new Car(racecar, 1, initEdgeRight, row*2, offset, DIR_LEFT, 5));
//
//		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*3, offset*-1, DIR_RIGHT, 4));
//		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*3, offset*-3, DIR_RIGHT, 4));
//		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*3, offset*-5, DIR_RIGHT, 4));
//
//		this.cars.add(new Car(bus, 2, initEdgeRight, row*4, offset, DIR_LEFT,2));
//		this.cars.add(new Car(bus, 2, initEdgeRight, row*5, offset*5, DIR_LEFT,2));

		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*6, offset*-1, DIR_RIGHT, 3));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*6, offset*-3, DIR_RIGHT, 3));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*6, offset*-5, DIR_RIGHT, 3));

		this.cars.add(new Car(racecar, 1, initEdgeRight, row*7, offset, DIR_LEFT, 5));

		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*8, offset*-1, DIR_RIGHT, 4));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*8, offset*-3, DIR_RIGHT, 4));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*8, offset*-5, DIR_RIGHT, 4));

		this.cars.add(new Car(bus, 2, initEdgeRight, row*9, offset, DIR_LEFT,2));
		this.cars.add(new Car(bus, 2, initEdgeRight, row*9, offset*5, DIR_LEFT,2));

		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*10, offset*-1, DIR_RIGHT, 2));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*10, offset*-3, DIR_RIGHT, 2));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*10, offset*-5, DIR_RIGHT, 2));
	}

	/**
	 * Adds the platforms that will be moving across the game.
	 */
	private void addPlatforms(){
		int initEdgeLeft	= 0;
		int initEdgeRight	= this.width;
		int offset 			= TILE_PIX;
		int row 			= TILE_PIX;

		Sprite turtle	= new Sprite(new Texture("core/assets/turtle.png"));
		Sprite log3		= new Sprite(new Texture("core/assets/log3.png"));
		Sprite log4 	= new Sprite(new Texture("core/assets/log4.png"));
		Sprite log5 	= new Sprite(new Texture("core/assets/log5.png"));

		this.platforms.add(new Platform(turtle, 1, initEdgeRight, row*6, offset, DIR_LEFT, 2));
		this.platforms.add(new Platform(turtle, 1, initEdgeRight, row*6, offset*2, DIR_LEFT, 2));
		this.platforms.add(new Platform(turtle, 1, initEdgeRight, row*6, offset*5, DIR_LEFT, 2));
		this.platforms.add(new Platform(turtle, 1, initEdgeRight, row*6, offset*6, DIR_LEFT, 2));

		this.platforms.add(new Platform(log5, 5, initEdgeLeft, row*7, offset*-5, DIR_RIGHT, 2));

		this.platforms.add(new Platform(turtle, 1, initEdgeRight, row*8, offset, DIR_LEFT, 4));
		this.platforms.add(new Platform(turtle, 1, initEdgeRight, row*8, offset*2, DIR_LEFT, 4));
		this.platforms.add(new Platform(turtle, 1, initEdgeRight, row*8, offset*8, DIR_LEFT, 4));
		this.platforms.add(new Platform(turtle, 1, initEdgeRight, row*8, offset*9, DIR_LEFT, 4));

		this.platforms.add(new Platform(log4, 4, initEdgeLeft, row*9, offset*-4, DIR_RIGHT, 3));
		this.platforms.add(new Platform(log4, 4, initEdgeLeft, row*9, offset*-10, DIR_RIGHT, 3));

		this.platforms.add(new Platform(log3, 3, initEdgeRight, row*10, offset, DIR_LEFT, 5));
		this.platforms.add(new Platform(log3, 3, initEdgeRight, row*10, offset*7, DIR_LEFT, 5));
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
		Gdx.gl.glClearColor(220/255, 220/255, 220/255, 1);
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
		this.initMapVision();
		for(Platform platform:this.platforms){
			platform.update(Kittener.mapVision);
			platform.draw(this.batch);
		}

		for(Car car:this.cars){
			car.update(Kittener.mapVision);
			car.draw(this.batch);
		}

		int deadCount = 0;
		double maxFitness = 0;

		// Used for debugging.
		int counter = 0;

		for (int i = 0;i < this.players.size();i++){
			Player player = this.players.get(i);

			// Debugging to see a single player's vision.
			/*if(counter == 0 && player.isAlive()) {
				//this.printMap();
				int[][] result = player.getPlayerVision();
				for(int row = 0; row < result.length; row++) {
					for(int col = 0; col < result[row].length; col++) {
						System.out.print("\t" + result[row][col]);
					}
					System.out.println();
				}
				System.out.println("\n##################################\n");
				counter++;
//				if(catWontLearn()){
//					learn();
//				}
			}*/

			if(player.isAlive()){

				player.update(Kittener.mapVision);
				int[][] vision = player.getPlayerVision();
				double[] output = this.population.getOrganisms().get(i).getNetwork().feedForward(vision);

				// Used for testing to see output.
				//System.out.println(this.population.getOrganisms().get(i).getNetwork());

				int max = 0;
				for (int j = 1; j < output.length; j++) {
					//output[j] = (double)1
					if (output[j] > output[max]) {
						max = j;
					}
				}
				if(max != 4){
					player.move(max);
				}
				if(player.shouldDie()) {
					player.kill();
				}

			}else{
				deadCount++;
			}
			if(player.getScore() > maxFitness){
				maxFitness = player.getScore();
			}
			if(player.getScore() > this.maxOverAll){
				this.maxOverAll = player.getScore();
			}
			player.draw(this.batch);
		}

		if(deadCount == this.numPlayers){
			for(int i = 0;i < this.players.size();i++){
				this.population.setFitness(i,this.players.get(i).getScore());
			}
			this.population.naturalSelection();
			try {
				// Wait a second to see how all have died.
				Thread.sleep(1000);
			} catch(InterruptedException ie) {
				System.out.println(ie.getMessage());
			}
			this.resetPlayers();

			for(Platform platform: this.platforms){
				platform.reset();
			}

			for(Car car: this.cars){
				car.reset();
			}
		}

		this.scoreDisplay.setColor(Color.WHITE);
		this.scoreDisplay.draw(this.batch, "Generation: " + this.population.getGeneration() + "\nGeneration Max: " +
				maxFitness + "\nMax Overall: " + this.maxOverAll,0,this.scoreDisplay
				.getCapHeight() + TILE_PIX + 15);
	}

	/**
	 * Helper method that prints out the map vision
	 */
	private void printMap(){
		for(int i = 0;i < Kittener.mapVision.length;i++){
			for(int j = 0;j < Kittener.mapVision[i].length;j++){
				System.out.print(Kittener.mapVision[i][j]);
			}
			System.out.println();
		}
		System.out.println("------------");
	}

	/**
	 * Resets all of the players
	 */
	public void resetPlayers(){
		for(Player player: this.players){
			player.reset();
		}
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

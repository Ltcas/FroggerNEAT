package com.mygdx.game;

import NEAT.Population;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
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
import sun.rmi.runtime.Log;

import java.util.ArrayList;

/**
 * Contains the game logic for a game of "Kittener" which is based off of the classic arcade game
 * of "Frogger".
 * @author Chance Simmons and Brandon Townsend
 * @version 5 March 2019
 */
public class Kittener extends ApplicationAdapter implements InputProcessor{

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
	private int maxOverAll;

	/** Boolean value used to determine if the game is currently paused. */
	private boolean paused;

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
		this.numPlayers 	= 150;
		this.paused = false;
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
		Gdx.input.setInputProcessor(this);
		this.addPlayers();
		this.population = new Population(this.numPlayers,25,5);
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

		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*6, offset*-1, DIR_RIGHT, 3));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*6, offset*-3, DIR_RIGHT, 3));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*6, offset*-5, DIR_RIGHT, 3));

		this.cars.add(new Car(racecar, 1, initEdgeRight, row*7, offset, DIR_LEFT, 4));

		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*8, offset*-1, DIR_RIGHT, 4));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*8, offset*-3, DIR_RIGHT, 4));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*8, offset*-5, DIR_RIGHT, 4));

		this.cars.add(new Car(bus, 2, initEdgeRight, row*9, offset, DIR_LEFT,2));
		this.cars.add(new Car(bus, 2, initEdgeRight, row*9, offset*5, DIR_LEFT,2));

		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*10, offset*-1, DIR_RIGHT, 2));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*10, offset*-3, DIR_RIGHT, 2));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*10, offset*-5, DIR_RIGHT, 2));

		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row, offset * -1, DIR_RIGHT, 0));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row, offset * -3, DIR_RIGHT, 0));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row, offset * -5, DIR_RIGHT, 0));

		this.cars.add(new Car(racecar, 1, initEdgeRight, row*2, offset, DIR_LEFT, 0));

		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*3, offset*-1, DIR_RIGHT, 0));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*3, offset*-3, DIR_RIGHT, 0));
		this.cars.add(new Car(yellowCar, 1, initEdgeLeft, row*3, offset*-5, DIR_RIGHT, 0));

		this.cars.add(new Car(bus, 2, initEdgeRight, row*4, offset, DIR_LEFT,0));
		this.cars.add(new Car(bus, 2, initEdgeRight, row*4, offset*5, DIR_LEFT,0));
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
			if(!this.paused){
				platform.update(Kittener.mapVision);
			}
			platform.draw(this.batch);
		}

		for(Car car:this.cars){
			if(!this.paused){
				car.update(Kittener.mapVision);
			}
			car.draw(this.batch);
		}

		int maxFitness = 0;

		for (int i = 0;i < this.players.size();i++){
			Player player = this.players.get(i);

			if(player.isAlive() && !this.paused){
				player.update(Kittener.mapVision);
				int[][] vision = player.getPlayerVision();
				double[] output = this.population.getOrganisms().get(i).getNetwork().feedForward(vision);

				int max = 0;
				for (int j = 1; j < output.length; j++) {
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

			}
			if(player.getScore() > maxFitness){
				maxFitness = (int)player.getScore();
			}
			if(player.getScore() > this.maxOverAll){
				this.maxOverAll = (int)player.getScore();
			}
			if(player.getFrameCount() > 2000 && player.isShown()){
			    this.disableDeadSprites();
            }

		}

		this.addCarRow(maxFitness);
		this.drawPlayers();
		this.checkDead();
		this.scoreDisplay.setColor(Color.WHITE);
		this.scoreDisplay.draw(this.batch, "Generation: " + this.population.getGeneration() + "\nGeneration Max: " +
				maxFitness + "\nMax Overall: " + this.maxOverAll,0,this.scoreDisplay
				.getCapHeight() + TILE_PIX + 40);
	}

	/**
	 * Adds cars to the bottom road as the players reach certain scores.
	 * @param score the highest score that the players have reached
	 */
	public void addCarRow(double score){

		if(score >= 5000) {
			this.cars.get(12).setSpeed(3);
			this.cars.get(13).setSpeed(3);
			this.cars.get(14).setSpeed(3);
		}
		if(score >= 7500){
			this.cars.get(15).setSpeed(4);
		}
		if(score >= 10000){
			this.cars.get(16).setSpeed(2);
			this.cars.get(17).setSpeed(2);
			this.cars.get(18).setSpeed(2);
		}
		if(score >= 12500){
			this.cars.get(19).setSpeed(2);
			this.cars.get(20).setSpeed(2);
		}
	}

	/**
	 * Draws the players, with the max and second max being colored differently than the other cats.
	 */
	public void drawPlayers(){
		Player maxPlayer;
		double maxScore = 0;
		int maxIndex = 0;
		int i = 0;
		for(Player player:this.players){
			if(player.getScore() > maxScore && player.isAlive()){
				maxScore = player.getScore();
				maxIndex = i;
			}
			i++;
		}

		maxPlayer = this.players.get(maxIndex);

		Player maxDead;
		maxScore = 0;
		i = 0;
		for(Player player:this.players){
			if(player.getScore() > maxScore && !player.isAlive()){
				maxScore = player.getScore();
				maxIndex = i;
			}
			i++;
		}

		maxDead = this.players.get(maxIndex);

		for(Player player: this.players){
			player.setColor(Color.PURPLE);
		}

		for(Player player: this.players){
			if(player.isShown() && !player.equals(maxPlayer) && !player.equals(maxDead)){
				player.draw(this.batch);
			}
		}

		if(maxDead.getScore() < maxPlayer.getScore()){
			maxDead.setColor(Color.CYAN);
			maxPlayer.setColor(Color.RED);
		}else{
			maxDead.setColor(Color.RED);
			maxPlayer.setColor(Color.CYAN);
		}
		maxDead.draw(batch);
		maxPlayer.draw(batch);
	}

    /**
     * Helper method that checks the dead count and calls natural selection when the all players are dead
     */
	private void checkDead(){
       int deadCount = 0;
       for(Player player:this.players){
           if(!player.isAlive()){
               deadCount++;
           }
       }

        if(deadCount == this.numPlayers){
            for(int i = 0;i < this.players.size();i++){
                this.population.setFitness(i,this.players.get(i).getScore());
                this.players.get(i).setShown(true);
            }
            this.population.naturalSelection();
            //try {
                // Wait a second to see how all have died.
                //Thread.sleep(1000);
            //} catch(InterruptedException ie) {
                //System.out.println(ie.getMessage());
            //}
            this.resetPlayers();

            for(Platform platform: this.platforms){
                platform.reset();
            }

			this.cars.get(12).setSpeed(0);
			this.cars.get(13).setSpeed(0);
			this.cars.get(14).setSpeed(0);
			this.cars.get(15).setSpeed(0);
			this.cars.get(16).setSpeed(0);
			this.cars.get(17).setSpeed(0);
			this.cars.get(18).setSpeed(0);
			this.cars.get(19).setSpeed(0);
			this.cars.get(20).setSpeed(0);
            for(Car car: this.cars){
                car.reset();
            }
        }
    }
    /**
     * Helper method that disables the rendering of dead players sprites
     */
	private void disableDeadSprites(){
        for(Player player:this.players){
            if(!player.isAlive()){
                player.setShown(false);
            }
        }
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
	 * Helper method that resets all of the players
	 */
	private void resetPlayers(){
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

	/**
	 * Used to detect if a key has been pressed, and performs certain actions based on the key that
	 * was pressed.
	 * @param keyCode the keycode of the key that was pressed down
	 * @return true if a key was pressed
	 */
	@Override
	public boolean keyDown(int keyCode){
		if(keyCode == Input.Keys.P){
			this.paused = !this.paused;
		}else if(keyCode == Input.Keys.LEFT){
			for(Player player:this.players){
				player.addSpeed(1);
			}

			for(Platform platform:this.platforms){
				platform.addSpeed(-1);
			}

			for(Car car:this.cars){
				car.addSpeed(-1);
			}
		}else if(keyCode == Input.Keys.RIGHT){
			for(Player player:this.players){
				player.addSpeed(-1);
			}

			for(Platform platform:this.platforms){
				platform.addSpeed(1);
			}

			for(Car car:this.cars){
				car.addSpeed(1);
			}
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character){
		return false;
	}

	@Override
	public boolean keyUp(int keyCode){
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX,int screenY){
		return false;
	}

	@Override
	public boolean scrolled(int amount){
		return false;
	}

	@Override
	public boolean touchDown(int screenX,int screenY,int pointer,int button){
		return false;
	}

	@Override
	public boolean touchDragged(int screenX,int screenY,int pointer){
		return false;
	}

	@Override
	public boolean touchUp(int screenX,int screenY,int pointer,int button){
		return false;
	}
}

package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Sprite that models a kitten player.
 * @author Chance Simmons and Brandon Townsend
 * @version 5 March 2019
 */
public class Player extends Sprite{

    /**The texture for when the player is moving forward*/
    private final static Texture BACK = new Texture("core/assets/cat_back.png");

    /**The texture for when the player is moving down*/
    private final static Texture FRONT = new Texture("core/assets/cat_front.png");

    /**The texture for when the player is moving right*/
    private final static Texture RIGHT = new Texture("core/assets/cat_right.png");

    /**The texture for when the player is moving left*/
    private final static Texture LEFT = new Texture("core/assets/cat_left.png");

    /** Constant to represent the number of pixels for the width and height of a tile. */
    private final static int TILE_PIX = 32;

    /**The x position of the player*/
    private int x;

    /**The y position of the player*/
    private int y;

    /**The width of the screen*/
    private int width;

    /**The height of the screen*/
    private int height;

    /**Used to see if the player is still alive*/
    private boolean isAlive;

    /**The score of the player*/
    private double score;

    /**The collision map*/
    private TiledMapTileLayer collision;

    /**List of cars*/
    private ArrayList<Car> cars;

    /**List of platforms*/
    private ArrayList<Platform> platforms;

    /**Random generator used for movements*/
    private Random randomGenerator;

    /** */
    private int deathFrameCount;

    private int frameCount;

    /**
     * Constructor that sets up a player.
     * @param width width of the screen
     * @param height height of the screen
     * @param collision the collision map for the tiled map
     * @param cars the list of cars
     * @param platforms the list of platforms
     */
    public Player(int width, int height,TiledMapTileLayer collision,ArrayList<Car> cars,ArrayList<Platform> platforms){
        super();
        this.set(new Sprite(Player.BACK));
        this.width = width;
        this.height = height;
        this.x = TILE_PIX*8;
        this.y = 0;
        this.isAlive = true;
        this.setPosition(this.x,this.y);
        this.collision = collision;
        this.cars = cars;
        this.platforms = platforms;
        this.score = 0;
        this.deathFrameCount = 0;
        this.frameCount = 0;
        this.randomGenerator = new Random();
    }

    /**
     * Test to see if the player has collided with a car, platform, or water.
     */
    public void testCollision() {
        int tileID = this.collision.getCell(this.x / TILE_PIX, this.y / TILE_PIX).getTile().getId();
        boolean onPlatform = false;
        for (Platform platform : this.platforms) {
            if (platform.getBoundingRectangle().overlaps(this.getBoundingRectangle())) {
                int futureX = this.x + platform.getSpeed() * platform.getDirection();
                if (futureX + TILE_PIX < this.width && futureX > 0) {
                    this.x = futureX;
                    this.translateX(platform.getSpeed() * platform.getDirection());
                    onPlatform = true;
                }
            }
        }

        for (Car car : this.cars) {
            if ((this.getBoundingRectangle().overlaps(car.getBoundingRectangle()) || tileID == 3) && !onPlatform) {
                this.isAlive = false;
                this.setTexture(new Texture("core/assets/death.png"));
            }
        }
    }

    /**
     * Gets the score of the player.
     * @return the score that the player has
     */
    public double getScore(){
        return (int) Math.round(this.score * (1.0 / this.frameCount) * 1000);
    }

    /**
     * Resets player and score back to the starting point.
     */
    private void reset(){
        this.isAlive = true;
        this.frameCount = 1;
        this.x = TILE_PIX*8;
        this.y = 0;
        this.score = 0;
        this.setPosition(this.x,this.y);
        this.setTexture(new Texture("core/assets/cat_back.png"));
    }

    /**
     * Updates movement and then test for collision.
     */
    public void update(int[][] map){
        this.frameCount += 1;
        if (this.frameCount % 60 == 0) {
            move(this.randomGenerator.nextInt(4));

            // Test print for the player's "map vision".
            System.out.println("###############");
            for(int row = 0; row < map.length; row++) {
                for(int col = 0; col < map[row].length; col++) {
                    System.out.print(map[row][col] + " ");
                }
                System.out.println();
            }
            System.out.println("###############");
        }
        testCollision();
    }

    /**
     * Move the player in a certain direction.
     * @param direction int that is used to pick the direction the player will move
     */
    public void move(int direction){
        if(this.isAlive){
            switch (direction){
                case 0:
                    if(this.y + TILE_PIX < this.height){
                        this.setTexture(Player.BACK);
                        this.y += TILE_PIX;
                        this.score += 100;
                    }else{
                        this.reset();
                    }
                    break;
                case 1:
                    if(this.x + TILE_PIX < this.width){
                        this.setTexture(Player.RIGHT);
                        this.x += TILE_PIX;
                    }
                    break;
                case 2:
                    if(this.y - TILE_PIX >= 0){
                        this.setTexture(Player.FRONT);
                        this.y -= TILE_PIX;
                        this.score -= 100;
                    }
                    break;
                case 3:
                    if(this.x - TILE_PIX >= 0){
                        this.setTexture(Player.LEFT);
                        this.x -= TILE_PIX;
                    }
                    break;
            }
            this.setPosition(this.x,this.y);
        }
    }

    /**
     * Gets the status of the player
     * @return true if the player is alive, false otherwise
     */
    public boolean isAlive(){
        return this.isAlive;
    }
}

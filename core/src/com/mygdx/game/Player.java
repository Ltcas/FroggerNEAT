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
    private final static Texture BACK = new Texture("core/assets/cat_back_new.png");

    /**The texture for when the player is moving down*/
    private final static Texture FRONT = new Texture("core/assets/cat_front_new.png");

    /**The texture for when the player is moving right*/
    private final static Texture RIGHT = new Texture("core/assets/cat_right_new.png");

    /**The texture for when the player is moving left*/
    private final static Texture LEFT = new Texture("core/assets/cat_left_new.png");

    /** Constant to represent the number of pixels for the width and height of a tile. */
    private final static int TILE_PIX = 32;

    /** The size of the vision map that the player can see */
    private final int VISION_SIZE = 5;

    /** Determines how frequent the player is allowed to move based on the number of frames. */
    private int moveSpeed;

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
    private int score;

    /**The collision map*/
    private TiledMapTileLayer collision;

    /**List of cars*/
    private ArrayList<Car> cars;

    /**List of platforms*/
    private ArrayList<Platform> platforms;

    /**Used to count number of frames to limit movement */
    private int frameCount;

    /** The frame difference between the last time the player increased it's score. */
    private int frameDiff;

    /** The previous best score that the player achieved. */
    private double prevScore;

    /** The array that stores what the player can see */
    private int[][] playerVision;

    /** Used to determine if the players sprite should be rendered to the screen */
    private boolean shown;

    /**
     * Constructor that sets up a player.
     * @param width width of the screen
     * @param height height of the screen
     * @param collision the collision map for the tiled map
     * @param cars the list of cars
     * @param platforms the list of platforms
     * @param platforms the list of platforms
     */
    public Player(int width, int height,TiledMapTileLayer collision,ArrayList<Car> cars,ArrayList<Platform> platforms){
        super();
        this.set(new Sprite(Player.BACK));
        this.width = width;
        this.height = height;
        this.x = TILE_PIX*8;
        this.y = 0;
        this.moveSpeed = 10;
        this.isAlive = true;
        this.setPosition(this.x,this.y);
        this.collision = collision;
        this.cars = cars;
        this.platforms = platforms;
        this.score = 0;
        this.frameCount = 0;
        this.frameDiff = 0;
        this.prevScore = 0.0;
        this.shown = true;
        this.playerVision = new int[VISION_SIZE][VISION_SIZE];
    }

    /**
     * Test to see if the player has collided with a car, platform, or water.
     */
    public void testCollision() {
        int tileID = this.collision.getCell(this.x / TILE_PIX, this.y / TILE_PIX).getTile().getId();
        boolean onPlatform = false;
        if(tileID == 3){
            this.isAlive = false;
            this.setTexture(new Texture("core/assets/death_new.png"));
        }
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
                this.setTexture(new Texture("core/assets/death_new.png"));
            }
        }
    }

    /**
     * Gets the score of the player.
     * @return the score that the player has
     */
    public double getScore(){
        return this.score;
    }

    /**
     * Resets player and score back to the starting point.
     */
    public void reset(){
        if(this.isAlive) {
            this.y = 0;
            this.setPosition(this.x,this.y);
        } else {
            this.isAlive = true;
            this.frameCount = 1;
            this.frameDiff = 0;
            this.prevScore = 0.0;
            this.x = TILE_PIX*8;
            this.y = 0;
            this.score = 0;
            this.setPosition(this.x,this.y);
            this.setTexture(new Texture("core/assets/cat_back_new.png"));
        }
    }

    /**
     * Updates movement and then test for collision.
     */
    public void update(int[][] mapVision){
        if(this.isAlive) {
            this.frameCount++;
            int xPositon = this.x / Player.TILE_PIX - 2;
            int yPostion = (this.height/ Player.TILE_PIX - 1) - (this.y / Player.TILE_PIX) - 2;
            for(int i = 0;i < VISION_SIZE;i++){
                for(int j = 0;j < VISION_SIZE;j++){
                    if(xPositon+j < 0 || xPositon+j >= this.width / Player.TILE_PIX || yPostion+i < 0 || yPostion+i >= this.height / Player.TILE_PIX){
                        if(yPostion+i < 0) {
                            this.playerVision[i][j] = MapObjects.FLOOR.getValue();
                        } else {
                            this.playerVision[i][j] = MapObjects.HAZARD.getValue();
                        }
                    }else{
                        if(j > yPostion) {
                            this.playerVision[i][j] = mapVision[yPostion + i][xPositon + j] / 2;
                        } else {
                            this.playerVision[i][j] = mapVision[yPostion + i][xPositon + j];
                        }
                    }
                }
            }
            testCollision();
        }
    }

    /**
     * Move the player in a certain direction.
     * @param direction int that is used to pick the direction the player will move
     */
    public void move(int direction){
        if(this.isAlive && this.frameCount % this.moveSpeed == 0){
            switch (direction){
                case 0:
                    if(this.y + TILE_PIX < this.height){
                        this.setTexture(Player.BACK);
                        this.y += TILE_PIX;
                        this.score += 100;
                    }else{
                        this.score += 100;
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
     * Gets the player vision
     * @return the player vision
     */
    public int[][] getPlayerVision(){
        return this.playerVision;
    }

    /**
     * Gets the frame count for this player
     * @return the players frame count.
     */
    public int getFrameCount() {
        return this.frameCount;
    }

    /**
     * Gets the status of the player
     * @return true if the player is alive, false otherwise
     */
    public boolean isAlive(){
        return this.isAlive;
    }

    /**
     * Checks to see if the player should die if they have not moved in a certain amount of time.
     * @return true if the player should be eliminated
     */
    public boolean shouldDie() {
        boolean result = false;
        if(this.score - this.prevScore < 100) {
            this.frameDiff++;
            if(frameDiff >= 12*60) {
                result = true;
            }
        } else {
            prevScore = this.score;
            this.frameDiff = 0;
        }
        return result;
    }

    /**
     * Sets whether or not the sprite should be shown on the screen.
     * @param shown true if the sprite should be shown on the screen.
     */
    public void setShown(boolean shown){
        this.shown = shown;
    }

    /**
     * Gets the value of shown to be used to determine if this sprite should be shown
     * @return the value of shown
     */
    public boolean isShown(){
        return this.shown;
    }

    /**
     * Kills this player
     */
    public void kill() {
        isAlive = false;
        this.setTexture(new Texture("core/assets/death_new.png"));
    }

    /**
     * Adds speed to the player
     * @param speed the amount of speed to add
     */
    public void addSpeed(int speed){
        if(this.moveSpeed + speed != 0){
            this.moveSpeed += speed;
        }
    }
}

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Chance on 2/6/2019.
 */
public class Player extends Sprite implements InputProcessor{
    /**The texture for when the player is moving forward*/
    private final static Texture BACK = new Texture("core/assets/cat_back.png");
    /**The texture for when the player is moving down*/
    private final static Texture FRONT = new Texture("core/assets/cat_front.png");
    /**The texture for when the player is moving right*/
    private final static Texture RIGHT = new Texture("core/assets/cat_right.png");
    /**The texture for when the player is moving left*/
    private final static Texture LEFT = new Texture("core/assets/cat_left.png");
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
    /**Random generator used for movements*/
    private Random randomGenerator;

    /**
     * Constructor that sets up a player
     * @param width
     * @param height
     * @param collision
     * @param cars
     * @param platforms
     */
    public Player(int width, int height,TiledMapTileLayer collision,ArrayList<Car> cars,ArrayList<Platform> platforms){
        super();
        this.set(new Sprite(this.BACK));
        this.width = width;
        this.height = height;
        this.x = 32*8;
        this.y = 0;
        this.isAlive = true;
        this.setPosition(this.x,this.y);
        this.collision = collision;
        this.cars = cars;
        this.platforms = platforms;
        this.score = 0;
        this.randomGenerator = new Random();
        Gdx.input.setInputProcessor(this);
    }

    public void testCollision(){
        int tileID = this.collision.getCell(this.x/32,this.y/32).getTile().getId();
        boolean onPlatform = false;
        for(Platform platform:this.platforms){
            if(platform.getBoundingRectangle().overlaps(this.getBoundingRectangle())){
                int futureX = this.x + platform.getSpeed() * platform.getDirection();
                if(futureX + 32 < this.width && futureX > 0){
                    this.x = futureX;
                    this.translateX(platform.getSpeed() * platform.getDirection());
                    onPlatform = true;
                }
            }
        }

        for(Car car:this.cars){
            if((this.getBoundingRectangle().overlaps(car.getBoundingRectangle()) || tileID == 3) && !onPlatform){
                this.isAlive = false;
                this.setTexture(new Texture("core/assets/death.png"));
                this.score = 0;
            }
        }
    }

    @Override
    public boolean keyDown (int keycode) {
        if(this.isAlive){
            switch (keycode){
                case Input.Keys.UP:
                    if(this.y + 32 < this.height){
                        this.setTexture(this.BACK);
                        this.y += 32;
                        this.score += 100;
                    }else{
                        this.reset();
                    }
                    break;
                case Input.Keys.DOWN:
                    if(this.y - 32 >= 0){
                        this.setTexture(this.FRONT);
                        this.y -= 32;
                        this.score -= 100;
                    }
                    break;
                case Input.Keys.RIGHT:
                    if(this.x + 32 < this.width){
                        this.setTexture(this.RIGHT);
                        this.x += 32;
                    }
                    break;
                case Input.Keys.LEFT:
                    if(this.x - 32 >= 0){
                        this.setTexture(this.LEFT);
                        this.x -= 32;
                    }
                    break;
            }
            this.setPosition(this.x,this.y);
        }else if(keycode == Input.Keys.R){
           this.reset();
        }
        return true;
    }

    public int getScore(){
        return this.score;
    }

    private void reset(){
        this.isAlive = true;
        this.x = 32*8;
        this.y = 0;
        this.setPosition(this.x,this.y);
        this.setTexture(new Texture("core/assets/cat_back.png"));
    }

    public void update(){
        int random = this.randomGenerator.nextInt(4);
        move(random);
        testCollision();
    }

    public void move(int direction){
        if(this.isAlive){
            switch (direction){
                case 0:
                    if(this.y + 32 < this.height){
                        this.setTexture(this.BACK);
                        this.y += 32;
                        this.score += 100;
                    }else{
                        this.reset();
                    }
                    break;
                case 1:
                    if(this.x + 32 < this.width){
                        this.setTexture(this.RIGHT);
                        this.x += 32;
                    }
                    break;
                case 2:
                    if(this.y - 32 >= 0){
                        this.setTexture(this.FRONT);
                        this.y -= 32;
                        this.score -= 100;
                    }
                    break;
                case 3:
                    if(this.x - 32 >= 0){
                        this.setTexture(this.LEFT);
                        this.x -= 32;
                    }
                    break;
            }
            this.setPosition(this.x,this.y);
        }
    }

    @Override
    public boolean keyUp (int keycode) {
        return true;
    }

    public boolean keyTyped (char character) {
        return false;
    }

    public boolean touchDown (int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchDragged (int x, int y, int pointer) {
        return false;
    }

    public boolean mouseMoved (int x, int y) {
        return false;
    }

    public boolean scrolled (int amount) {
        return false;
    }
}

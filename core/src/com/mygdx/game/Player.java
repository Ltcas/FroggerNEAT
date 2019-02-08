package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;

/**
 * Created by Chance on 2/6/2019.
 */
public class Player extends Sprite implements InputProcessor{

    private int x;
    private int y;
    private int width;
    private int height;
    private boolean isAlive;
    private int score;
    private TiledMapTileLayer collision;
    private ArrayList<Car> cars;
    private ArrayList<Platform> platforms;

    public Player(Sprite sprite, int width, int height,TiledMapTileLayer collision,ArrayList<Car> cars,ArrayList<Platform> platforms){
        super(sprite);
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
            }
        }
    }

    @Override
    public boolean keyDown (int keycode) {
        if(this.isAlive){
            switch (keycode){
                case Input.Keys.UP:
                    if(this.y + 32 < this.height){
                        this.setTexture(new Texture("core/assets/cat_back.png"));
                        this.y += 32;
                        this.score += 10;
                    }else{
                        this.reset();
                    }
                    break;
                case Input.Keys.DOWN:
                    if(this.y - 32 >= 0){
                        this.setTexture(new Texture("core/assets/cat_front.png"));
                        this.y -= 32;
                        this.score -= 10;
                    }
                    break;
                case Input.Keys.RIGHT:
                    if(this.x + 32 < this.width){
                        this.setTexture(new Texture("core/assets/cat_right.png"));
                        this.x += 32;
                    }
                    break;
                case Input.Keys.LEFT:
                    if(this.x - 32 >= 0){
                        this.setTexture(new Texture("core/assets/cat_left.png"));
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
        testCollision();
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

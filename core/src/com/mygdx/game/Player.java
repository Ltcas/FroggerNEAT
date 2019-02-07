package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * Created by Chance on 2/6/2019.
 */
public class Player extends Sprite implements InputProcessor{

    private int x;
    private int y;
    private int width;
    private int height;
    private boolean isAlive;

    public Player(Sprite sprite, int width, int height){
        super(sprite);
        this.width = width;
        this.height = height;
        this.x = 32*8;
        this.y = 0;
        this.isAlive = true;
        this.setPosition(this.x,this.y);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown (int keycode) {
        if(this.isAlive){
            switch (keycode){
                case Input.Keys.UP:
                    if(this.y + 32 < this.height){
                        this.setTexture(new Texture("core/assets/cat_back.png"));
                        this.y += 32;
                    }
                    break;
                case Input.Keys.DOWN:
                    if(this.y - 32 >= 0){
                        this.setTexture(new Texture("core/assets/cat_front.png"));
                        this.y -= 32;
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
        }
        return true;
    }

    @Override
    public boolean keyUp (int keycode) {
        switch (keycode){
            case Input.Keys.UP:
                break;
            case Input.Keys.DOWN:
                break;
            case Input.Keys.RIGHT:
                break;
            case Input.Keys.LEFT:
                break;
        }
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

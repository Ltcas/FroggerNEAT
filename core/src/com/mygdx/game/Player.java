package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Chance on 2/6/2019.
 */
public class Player extends Sprite implements InputProcessor{

    private int x;
    private int y;
    public Player(Sprite sprite){
        super(sprite);
        this.x = 32*8;
        this.y = 0;
        this.setPosition(this.x,this.y);
        Gdx.input.setInputProcessor(this);
    }

    public void update(){

    }

    @Override
    public boolean keyDown (int keycode) {
        switch (keycode){
            case Input.Keys.UP:
                this.setTexture(new Texture("core/assets/cat_back.png"));
                this.y += 32;
                break;
            case Input.Keys.DOWN:
                this.setTexture(new Texture("core/assets/cat_front.png"));
                this.y -= 32;
                break;
            case Input.Keys.RIGHT:
                this.setTexture(new Texture("core/assets/cat_right.png"));
                this.x += 32;
                break;
            case Input.Keys.LEFT:
                this.setTexture(new Texture("core/assets/cat_left.png"));
                this.x -= 32;
                break;
        }
        this.setPosition(this.x,this.y);
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

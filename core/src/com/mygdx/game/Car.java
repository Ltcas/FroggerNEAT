package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;

/**
 * Created by Chance on 2/7/2019.
 */
public class Car extends Sprite {

    private SpriteBatch batch;

    public Car(Sprite sprite, SpriteBatch batch, int x, int y){
        super(sprite);
        this.batch = batch;
        this.setPosition(x, y);
    }


    public void update() {
        float x = this.getX() + 1f * Gdx.graphics.getDeltaTime();
        batch.draw(this, x, 0);
    }
}

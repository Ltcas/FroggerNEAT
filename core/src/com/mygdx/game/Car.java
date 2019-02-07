package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Chance on 2/7/2019.
 */
public class Car extends Sprite {

    private SpriteBatch batch;
    private int startX;
    private int startY;
    private int direction;
    private int speed;

    public Car(Sprite sprite, SpriteBatch batch, int x, int y,int direction,int speed){
        super(sprite);
        this.batch = batch;
        this.startX = x;
        this.startY = y;
        this.direction = direction;
        this.speed = speed;
        this.setPosition(this.startX, this.startY);
    }


    public void update() {
        this.translateX(this.direction * this.speed);
        if(this.getX() < -32 || this.getX() > 480+32){
            this.setPosition(this.startX,this.startY);
        }
    }
}

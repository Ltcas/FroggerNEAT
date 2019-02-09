package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Chance on 2/7/2019.
 */
public class Car extends Sprite {

    private int startX;
    private int startY;
    private int direction;
    private int speed;

    public Car(Sprite sprite, int x, int y,int direction,int speed){
        super(sprite);
        this.startX = x;
        this.startY = y;
        this.direction = direction;
        this.speed = speed;
        this.setPosition(this.startX, this.startY);
    }


    public void update() {
        this.translateX(this.direction * this.speed);
        if(this.getX() < -64 || this.getX() > 480){
            this.setPosition(this.startX,this.startY);
        }
    }
}

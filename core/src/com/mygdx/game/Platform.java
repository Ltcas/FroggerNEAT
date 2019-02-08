package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Platform extends Sprite {

    private int startX;
    private int startY;
    private int direction;
    private int speed;

    public Platform(Sprite sprite, int x, int y,int direction,int speed){
        super(sprite);
        this.startX = x;
        this.startY = y;
        this.direction = direction;
        this.speed = speed;
        this.setPosition(this.startX, this.startY);
    }


    public void update() {
        this.translateX(this.direction * this.speed);
        if(this.getX() < -160 || this.getX() > 480+160){
            this.setPosition(this.startX,this.startY);
        }
    }
}

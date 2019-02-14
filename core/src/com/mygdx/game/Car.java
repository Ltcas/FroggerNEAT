package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Models a Car object that the player can collide with.
 * @author Chance Simmons and Brandon Townsend
 * @version 14 February 2019
 */
public class Car extends Sprite {

    /** The representation of the starting x position of a platform. */
    private int startX;

    /** The representation of the starting y position of a platform. */
    private int startY;

    /** The representation of the starting direction of a platform. */
    private int direction;

    /** The representation of the starting speed of a platform. */
    private int speed;

    /**
     * Constructor for a car object.
     * @param sprite The png representation of the car sprite.
     * @param x The starting x position of a car.
     * @param y The starting y position of a car.
     * @param direction The starting direction that a car will be traveling.
     * @param speed The starting speed that a car will be traveling.
     */
    public Car(Sprite sprite, int x, int y,int direction,int speed){
        super(sprite);
        this.startX = x;
        this.startY = y;
        this.direction = direction;
        this.speed = speed;
        this.setPosition(this.startX, this.startY);
    }

    /**
     * Updates a car object every render.
     */
    public void update() {
        this.translateX(this.direction * this.speed);
        if(this.getX() < -64 || this.getX() > 480){
            this.setPosition(this.startX,this.startY);
        }
    }
}
package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Models a Car object that the player can collide with.
 * @author Chance Simmons and Brandon Townsend
 * @version 14 February 2019
 */
public class Car extends Sprite {

    /** The representation of the starting x position of a car. */
    private int startX;

    /** The representation of the starting y position of a car. */
    private int startY;

    /** Integer representation of how many tiles a car will take up. */
    private int tileSize;

    /** Used to calculate the 'respawn' of a car. */
    private int offset;

    /** The representation of the starting direction of a car. */
    private int direction;

    /** The representation of the starting speed of a car. */
    private int speed;

    /**
     * Constructor for a car object.
     * @param sprite The png representation of the car sprite.
     * @param tileSize The representation of the size of the car.
     * @param x The starting x position of a car.
     * @param y The starting y position of a car.
     * @param direction The starting direction that a car will be traveling.
     * @param speed The starting speed that a car will be traveling.
     */
    public Car(Sprite sprite, int tileSize, int x, int y, int offset, int direction, int speed){
        super(sprite);
        this.tileSize = tileSize;
        this.startX     = x + offset;
        this.startY     = y;
        this.offset     = offset;
        this.direction  = direction;
        this.speed      = speed;
        this.setPosition(this.startX, this.startY);
    }

    /**
     * Returns the size of this car.
     * @return The size of this car.
     */
    public int getTileSize() {
        return this.tileSize;
    }

    /**
     * Updates a car object every render.
     */
    public void update(int[][] mapVision) {
        this.translateX(this.direction * this.speed);
        int boundLeft   = -192 + this.offset;
        int boundRight  = 672 + this.offset;
        if(this.getX() < boundLeft || this.getX() > boundRight){
            this.setPosition(this.startX, this.startY);
        }
    }
}
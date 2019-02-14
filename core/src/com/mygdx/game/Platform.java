package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Models a Platform object that the player can ride on.
 * @author Chance Simmons and Brandon Townsend
 * @version 14 February 2018
 */
public class Platform extends Sprite {

    /** The representation of the starting x position of a platform. */
    private int startX;

    /** The representation of the starting y position of a platform. */
    private int startY;

    /** Used to calculate the 'respawn' of a car. */
    private int offset;

    /** The representation of the starting direction of a platform. */
    private int direction;

    /** The representation of the starting speed of a platform. */
    private int speed;

    /**
     * Constructor for a platform object.
     * @param sprite The png representation of the platform sprite.
     * @param x The starting x position of a platform.
     * @param y The starting y position of a platform.
     * @param direction The starting direction that a platform will be traveling.
     * @param speed The starting speed that a platform will be traveling.
     */
    public Platform(Sprite sprite, int x, int y, int offset, int direction,int speed){
        super(sprite);
        this.startX = x + offset;
        this.startY = y;
        this.offset = offset;
        this.direction = direction;
        this.speed = speed;
        this.setPosition(this.startX, this.startY);
    }

    /**
     * Returns the speed the platform is traveling.
     * @return The speed the platform is traveling.
     */
    public int getSpeed(){
        return this.speed;
    }

    /**
     * Returns the direction the platform is traveling.
     * @return The direction the platform is traveling.
     */
    public int getDirection(){
        return this.direction;
    }

    /**
     * Updates a platform object every render.
     */
    public void update() {
        this.translateX(this.direction * this.speed);
        int boundLeft = -384 + this.offset;
        int boundRight = 800 + this.offset;
        if(this.getX() <boundLeft || this.getX() > boundRight){
            this.setPosition(this.startX,this.startY);
        }
    }
}

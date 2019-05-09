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

    /** Integer representation of the size of the platform. */
    private int tileSize;

    /** Used to calculate the 'respawn' of a car. */
    private int offset;

    /** The representation of the starting direction of a platform. */
    private int direction;

    /** The representation of the starting speed of a platform. */
    private int speed;

    /**
     * Constructor for a platform object.
     * @param sprite The png representation of the platform sprite.
     * @param tileSize The representation of the size of the platform.
     * @param x The starting x position of a platform.
     * @param y The starting y position of a platform.
     * @param direction The starting direction that a platform will be traveling.
     * @param speed The starting speed that a platform will be traveling.
     */
    public Platform(Sprite sprite, int tileSize, int x, int y, int offset, int direction,int speed){
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
     * Returns the size of this platform.
     * @return The size of this platform.
     */
    public int getTileSize() {
        return this.tileSize;
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
    public void update(int[][] mapVision) {
        this.translateX(this.direction * this.speed);
        int boundLeft   = -384 + this.offset;
        int boundRight  = 800 + this.offset;
        int y = 11 - (int)this.getY() / 32;
        int x = (int)this.getX() / 32;
        for (int i = 0; i < tileSize + 1; i++) {
            if(x + i > 0 && x + i < mapVision[0].length) {
                mapVision[y][x + i] = MapObjects.FLOOR.getValue();
            }
        }
        if(this.getX() <boundLeft || this.getX() > boundRight){
            this.reset();
        }
    }

    /**
     * Resets the position of this platform
     */
    public void reset(){
        this.setPosition(this.startX,this.startY);
    }

    /**
     * Adds speed to the platform
     * @param speed the speed to add.
     */
    public void addSpeed(int speed){
        this.speed += speed;
    }
}

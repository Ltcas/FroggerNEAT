package com.mygdx.game;

/**
 * Models the values that a map array will have in it to represent where a cat can move.
 * @author Chance Simmons and Brandon Townsend
 * @version 5 March 2019
 */
public enum MapObjects {
    HAZARD  (0),        //Value for a hazard on the map
    FLOOR   (10);       //Value for a floor on the map

    /** Represents the value that will be stored at each index (tile) in the array. */
    private final int value;

    /**
     * Sets up the enumeration to have a value of 1 if it's a hazard and a 0 if it's a safe floor
     * piece.
     * @param value Represents the value that will be stored at an index (tile).
     */
    MapObjects(int value) {
        this.value = value;
    }

    /**
     * Returns the value of this enumeration object.
     * @return The value of this enumeration object.
     */
    public int getValue() {
        return this.value;
    }
}

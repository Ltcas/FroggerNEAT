package NEAT;

import java.util.HashMap;

/**
 * Models an organism that will be building and modifying its own network to get the highest
 * fitness score it can.
 * @author Chance Simmons and Brandon Townsend
 * @version 25 February 2019
 */
public class Organism {

    /** Used to keep track of the innovation numbers of each gene. */
    private static HashMap<Gene, Integer> innovation = new HashMap<Gene, Integer>();

    /** Used to assign an organism a specified id number. */
    private static int id = 0;

    /** Keeps track of this organisms fitness. */
    private double fitness;

    /** The string representation of this organisms name. */
    private String name;

    /** Keeps track of this organisms generation. */
    private int generation;

    /** Used as a marker for whether this organism should be eliminated. */
    private boolean toElim;

    /** The neural network related to this organism. Used to represent phenotype. */
    private Network network;

    /** The species this organism belongs to. */
    private Species species;

    /** The genome of this organism. Used to represent genotype. */
    private Genome genome;

    /**
     * Constructor for an organism based on the passed in name.
     * @param name The name to be granted to the organism.
     */
    public Organism(String name){
        this.fitness    = 0;
        this.generation = 0;
        this.name       = name;
        this.toElim     = false;
        this.species    = null;
        this.network    = new Network(id,25,4);
        this.genome     = new Genome(this.network, id);
        id++;
    }

    /**
     * Sets the fitness of this organism.
     * @param fitness the new value of the fitness
     */
    public void setFitness(double fitness){
        this.fitness = fitness;
    }

    /**
     * Gets the fitness of this organism
     * @return the fitness of the organism
     */
    public double getFitness(){
        return this.fitness;
    }

    /**
     * Returns the species of this organism.
     * @return The species of this organism.
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * Sets this organism's species to the specified one.
     * @param species The specified species to set for this organism.
     */
    public void setSpecies(Species species) {
        this.species = species;
    }

    /**
     * Generates a string representation of this organism.
     * @return A string representation of this organism.
     */
    @Override
    public String toString() {
        return "\nName: " + this.name + "\nID: " + this.network.getId()+ "\nGen: "
                + this.generation + "\nFitness: " + this.fitness;
    }

    /**
     * Returns this organism's neural network aka. phenotype.
     * @return
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * Returns whether or not this organism should be eliminated.
     * @return True if it should be eliminated, false otherwise.
     */
    public boolean getToElim() {
        return toElim;
    }

    /**
     * Sets whether this organism should be eliminated.
     * @param toElim Should be true if setting this organism to be eliminated, false otherwise.
     */
    public void setToElim(boolean toElim) {
        this.toElim = toElim;
    }
}

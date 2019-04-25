package NEAT;

/**
 * Models an organism that will be building and modifying its own network to get the highest
 * fitness score it can.
 * @author Chance Simmons and Brandon Townsend
 * @version 25 February 2019
 */
public class Organism implements Cloneable {

    /** Used to assign an organism a specified id number. */
    private static int id = 0;

    /** Keeps track of this organisms fitness. */
    private double fitness;

    /** The string representation of this organisms name. */
    private String name;

    /** Keeps track of this organisms generation. */
    private int generation;

    /** The neural network related to this organism. Used to represent phenotype. */
    private Network network;

    private Species species;

    /**
     * Constructor for an organism based on the passed in name.
     * @param name The name to be granted to the organism.
     */
    public Organism(String name){
        this.fitness    = 0;
        this.generation = 0;
        this.name       = name;
        this.species    = null;
        this.network    = new Network(id,12*15,5);
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
     * Returns this organism's neural network aka. phenotype.
     * @return the network of this organism
     */
    public Network getNetwork() {
        return network;
    }


    public int getGeneration(){
        return this.generation;
    }

    public void setGeneration(int generation){
        this.generation = generation;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }


    /**
     * Generates a string representation of this organism.
     * @return A string representation of this organism.
     */
    @Override
    public String toString() {
        return "\tName: " + this.name + "\tID: " + this.network.getId()+ "\tGen: "
                + this.generation + "\tFitness: " + this.fitness;
    }

    /**
     * Clones this organism.
     * @return A clone of this organism.
     */
    @Override
    public Object clone() {
        Organism organism;
        try {
            organism = (Organism) super.clone();
        } catch (CloneNotSupportedException cne) {
            organism = new Organism(this.name);
        }
        organism.network = new Network(this.network);
        //System.out.printf("\n***Old Network Mem Address: %s\t New Network Mem Address: %s",
        //        this.getNetwork(), organism.getNetwork());
        organism.species = this.species;
        return organism;
    }
}

package NEAT;

/**
 * Models an organism that will be building and modifying its own network to get the highest
 * fitness score it can.
 * @author Chance Simmons and Brandon Townsend
 * @version 25 February 2019
 */
public class Organism {

    /**  */
    private static int id = 0;

    /**  */
    private double fitness;

    /**  */
    private String name;

    /**  */
    private int generation;

    /**  */
    private Network network;

    /**  */
    private Genome genome;

    /**
     * Constructor for an organism based on the passed in name.
     * @param name The name to be granted to the organism.
     */
    public Organism(String name){
        this.fitness    = 0;
        this.generation = 0;
        this.name       = name;
        this.network    = new Network(id,25,4);
        id++;
        this.genome     = new Genome();
    }

    public void updateNetwork(){

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
}

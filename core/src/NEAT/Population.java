package NEAT;

import java.util.ArrayList;
/**
 * Class that models the population of organisms and species.
 * @author Chance Simmons and Brandon Townsend
 * @version 25 February 2019
 */
public class Population {
    /** List of organisms in the population */
    ArrayList<Organism> organisms;

    /** List of species in the population */
    ArrayList<Species> species;

    /** List of innovations that have occurred in the population */
    ArrayList<Innovation> innovations;

    /** The number of organisms in the population */
    private int populationSize;

    /**
     * Constructor that initializes the population based off of a population size
     * @param populationSize the number of organisms in the population
     */
    public Population(int populationSize){
        this.organisms = new ArrayList<Organism>();
        this.populationSize = populationSize;
        this.species = new ArrayList<Species>();
        this.innovations = new ArrayList<Innovation>();
        this.initializePopulation();
    }

    /**
     * Populates the list of organism based off of the population size
     */
    private void initializePopulation(){
        for(int i = 0;i < this.populationSize;i++){
            this.organisms.add(new Organism("Test"));
        }
    }
}

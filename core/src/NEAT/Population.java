package NEAT;

import java.util.ArrayList;
/**
 * Class that models the population of organisms and species.
 * @author Chance Simmons and Brandon Townsend
 * @version 25 February 2019
 */
public class Population {
    /** List of organisms in the population */
    private ArrayList<Organism> organisms;

    /** List of species in the population */
    private ArrayList<Species> species;

    /** List of innovations that have occurred in the population */
    private ArrayList<Innovation> innovations;

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
        for(int i = 0; i < this.populationSize; i++){
            this.organisms.add(new Organism(String.format("Organism #%d", i)));
        }
    }

    /**
     * Returns the list of this populations organisms.
     * @return The list of this populations organisms.
     */
    public ArrayList<Organism> getOrganisms() {
        return organisms;
    }

    /**
     * Sets the fitness of a organism at a certain index in the organism list.
     * @param index the index to get the organism
     * @param fitness the fitness of the organism
     */
    public void setFitness(int index,double fitness){
        this.organisms.get(index).setFitness(fitness);
    }

    public void naturalSelection(){
        //Speciate
        //Sort species maybe?
        //Cull Species(kill bottom half of each species)
        //Kill stale species(Haven't changed in a certain number of generations)
        //Kill bad species(Ones that can't reproduce)
    }
}

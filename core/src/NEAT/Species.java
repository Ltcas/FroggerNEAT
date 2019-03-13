package NEAT;

import java.util.ArrayList;

public class Species {
    /** Excess Coefficient*/
    private final double EXCESS_COEFF = 2.0;

    /** Disjoint Coefficient */
    private final double DISJOINT_COEFF = 2.0;

    /** Weight Difference*/
    private final double WEIGHT_COEFF = 1;

    /** List of organisms in this speices */
    private ArrayList<Organism> organisms;

    /** Average fitness of the organisms in the species */
    private double avgFitness;

    /** Best fitness of any of the organisms in this speices */
    private double bestFitness;

    /**
     * Constructor for a species.
     */
    public Species(){
        this.organisms = new ArrayList<Organism>();
        this.avgFitness = 0;
        this.bestFitness = 0;
    }

    /**
     * Calculates the average fitness of the species
     */
    public void calculateAverage(){
        double sum = 0;
        for(Organism organism: this.organisms){
            sum += organism.getFitness();
        }
        this.avgFitness = sum / this.organisms.size();
    }

    /**
     * Adds a organism to this species
     * @param organism the organism to be added
     */
    public void addOrganism(Organism organism){
        this.organisms.add(organism);
    }

    /**
     * Removes a organism from the species
     * @param organism the organism to be removed
     */
    public void removeOrganism(Organism organism){
        this.organisms.remove(organism);
    }
}

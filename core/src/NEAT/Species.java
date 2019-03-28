package NEAT;

import java.util.ArrayList;

/**
 * Models a species
 * @author Chance Simmons and Brandon Townsend
 * @version 13 March 2019
 */
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

    /** Used to measure how many generations it has been since a useful change has been made */
    private int staleness;

    /** Cull species thresh */
    private final static double CULL_THRESH = .5;

    /**
     * Constructor for a species.
     */
    public Species(){
        this.organisms = new ArrayList<Organism>();
        this.avgFitness = 0;
        this.bestFitness = 0;
        this.staleness = 0;
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

    /**
     * Shares the fitness of the species based off of the number of organisms in the species
     */
    public void shareFitness(){
        for(Organism organism: this.organisms){
            organism.setFitness(organism.getFitness() / this.organisms.size());
        }
    }

    /**
     * Gets the organisms of this list.
     */
    public ArrayList<Organism> getOrganisms(){
        return this.organisms;
    }

    /**
     * Sorts the organisms in this species.
     */
    public void sort(){
        ArrayList<Organism> sortedOrganisms = new ArrayList<Organism>();
        for(int i = 0; i < this.organisms.size(); i++) {
            Organism maxOrganism = this.organisms.get(i);
            for(Organism o : this.organisms) {
                if(o.getFitness() > maxOrganism.getFitness()){
                    maxOrganism = o;
                }
            }
            this.organisms.remove(maxOrganism);
            sortedOrganisms.add(maxOrganism);
        }
        this.organisms = sortedOrganisms;
    }

    /**
     * Kill the low performing species.
     */
    public void cullSpecies(){
        int cullNumber = (int)Math.round(this.organisms.size() * CULL_THRESH);
        for(int i = cullNumber;i< this.organisms.size();i++){
            this.organisms.remove(this.organisms.get(i));
        }
    }
}

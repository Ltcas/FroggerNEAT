package NEAT;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.ArrayList;
import java.util.Random;

/**
 * Models a species
 * @author Chance Simmons and Brandon Townsend
 * @version 13 March 2019
 */
public class Species {
    /** List of organisms in this speices */
    private ArrayList<Organism> organisms;

    /** Average fitness of the organisms in the species */
    private double avgFitness;

    /** Best fitness of any of the organisms in this species */
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
     * Gets the average fitness of this speices
     * @return average fitness
     */
    public double getAvgFitness(){
        return this.avgFitness;
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
     * Reproduce organisms
     */
    public void reproduce(int numBabies){
        if(this.organisms.size() == 1){
            this.mutate(this.organisms.get(0));
        }else{
            for(int i = 0;i < numBabies;i++){
                Organism organism = this.crossOver();
                this.mutate(organism);
                this.organisms.add(organism);
            }
        }
    }

    public Organism crossOver(){
        Random randomGen = new Random();
        Organism parentOne = this.organisms.get();
        Organism parentTwo = this.organisms.get();
        return null;
    }

    /**
     * Mutates a given organism
     * @param organism the organism to mutate
     */
    public void mutate(Organism organism){
        Random randomGen = new Random();
        if(randomGen.nextDouble() < Constant.ADD_NODE_MUT.getValue()){

        }else if(randomGen.nextDouble() < Constant.ADD_LINK_MUT.getValue()){

        }else{
            if(randomGen.nextDouble() < Constant.WEIGHT_MUT.getValue()){

            }
            if(randomGen.nextDouble() < Constant.ENABLE_MUT.getValue()){

            }
        }

        organism.setGeneration(organism.getGeneration() + 1);
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

    /**
     * Gets the staleness of this species
     * @return the staleness of the species
     */
    public int getStaleness(){
        return this.staleness;
    }
}

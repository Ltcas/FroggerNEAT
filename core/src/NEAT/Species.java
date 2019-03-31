package NEAT;

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

    /** Random number generator used to find probabilities */
    private Random randomGen;

    /**
     * Constructor for a species.
     */
    public Species(){
        this.organisms = new ArrayList<Organism>();
        this.avgFitness = 0;
        this.bestFitness = 0;
        this.staleness = 0;
        this.randomGen = new Random();
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
     * Gets the average fitness of this species
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
        for(int i = 0;i < numBabies;i++){
            if(this.organisms.size() == 1){
                Organism organism = (Organism)this.organisms.get(0).clone();
                this.mutate(organism);
                this.organisms.add(organism);
            }else if(this.randomGen.nextDouble() < Constant.MUT_THRESH.getValue()){
                Organism organism = (Organism)this.organisms.get(this.randomGen.nextInt(this.organisms.size())).clone();
                this.mutate(organism);
                this.organisms.add(organism);
            }else{
                Organism organism = this.crossOver();
                this.mutate(organism);
                this.organisms.add(organism);
            }
        }
    }

    public Organism crossOver(){
        Organism parentOne = this.organisms.get(this.randomGen.nextInt(this.organisms.size()));
        Organism parentTwo = this.organisms.get(this.randomGen.nextInt(this.organisms.size()));

        return new Organism("Groot");
    }

    /**
     * Mutates a given organism
     * @param organism the organism to mutate
     */
    public void mutate(Organism organism){
        if(this.randomGen.nextDouble() < Constant.ADD_NODE_MUT.getValue()){
            organism.getGenome().addNodeMutation();
        }else if(this.randomGen.nextDouble() < Constant.ADD_LINK_MUT.getValue()){
            organism.getGenome().addConnectionMutation();
        }else{
            if(this.randomGen.nextDouble() < Constant.WEIGHT_MUT.getValue()){
                organism.getGenome().linkWeightMutation();
            }
            if(this.randomGen.nextDouble() < Constant.ENABLE_MUT.getValue()){
                organism.getGenome().linkEnableMutation();
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
        int cullNumber = (int)Math.round(this.organisms.size() * Constant.CULL_THRESH.getValue());
        for(int i = cullNumber;i< this.organisms.size();i++){
            this.removeOrganism(this.organisms.get(i));
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

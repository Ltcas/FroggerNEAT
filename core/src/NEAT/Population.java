package NEAT;

import java.util.ArrayList;
import java.util.Random;

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

    /**
     * Performs natural selection on the species in this population
     */
    public void naturalSelection(){
        this.speciate();
        this.sortSpecies();
        this.staleAndBadSpecies();
        ArrayList<Organism> organisms = new ArrayList<Organism>();
        for(Species species: this.species){
            int numBabies = (int)Math.round(species.getAvgFitness() / this.avgSum() * this.populationSize) - 1;
            species.reproduce(numBabies);
            organisms.addAll(species.getOrganisms());
        }
        while(organisms.size() > populationSize) {
            organisms.remove(populationSize);
        }
        this.organisms = organisms;
        System.out.println("Species Count: " + this.species.size());
        System.out.println("Organism: " + this.organisms.size());
}

    /**
     * Separates the organisms into species based on their compatibility.
     */
    public void speciate() {
        for(Organism o : organisms) {
            if(this.species.isEmpty()){
                this.species.add(new Species());
                this.species.get(0).addOrganism(o);
            }else{
                boolean foundSpecies = false;
                for(Species species:this.species){
                    if(o.getGenome().compatible(species.getOrganisms().get(0).getGenome()) <
                            Constant.COMPAT_THRESH.getValue() && !foundSpecies){
                        species.addOrganism(o);
                        foundSpecies = true;
                    }
                }
                if(!foundSpecies){
                    Species species = new Species();
                    species.addOrganism(o);
                    this.species.add(species);
                }
            }
        }
    }

    /**
     * Sorts each of the species by best performing agents.
     */
    public void sortSpecies(){
        for(Species species: this.species){
            species.shareFitness();
            species.sort();
            species.cullSpecies();
        }
    }

    /**
     * Sums all the averages in the species. Used to determine how many times the
     * @return the sum of all the species averages
     */
    public double avgSum(){
        double sum = 0;
        for(Species species:this.species){
            species.calculateAverage();
            sum += species.getAvgFitness();
        }
        return sum;
    }

    /**
     * Kills off the stale and bad species.
     */
    public void staleAndBadSpecies(){
        ArrayList<Species> killSpecies = new ArrayList<Species>();
        for(Species species: this.species){
            //Kill species that have not gotten better over a certain number of generations
            if(species.getStaleness() == Constant.STALENESS_THRESH.getValue()){
                killSpecies.add(species);
            }else if(species.getAvgFitness() / this.avgSum() * this.populationSize < 1){ //Species that can't reproduce
                killSpecies.add(species);
            }
        }

        System.out.println("Kill Species Size: " + killSpecies.size());
        for(Species species: killSpecies){
            this.organisms.removeAll(species.getOrganisms());
        }
        this.species.removeAll(killSpecies);
    }
}

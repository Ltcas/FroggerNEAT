package NEAT;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class that models the population of organisms and species.
 * @author Chance Simmons and Brandon Townsend
 * @version 2 April 2019
 */
public class Population {
    /** List of organisms in the population */
    private ArrayList<Organism> organisms;

    /**
     * List of species in the population. The size of species should always be less than or
     * equal to the size of organisms.
     */
    private ArrayList<Species> species;

    /** The number of organisms in the population */
    private int populationSize;

    /** The generation number of this population. */
    private int generation;

    /**
     * Constructor that initializes the population based off of a population size
     * @param populationSize the number of organisms in the population
     */
    public Population(int populationSize){
        this.organisms      = new ArrayList<Organism>();
        this.species        = new ArrayList<Species>();
        this.populationSize = populationSize;
        this.generation     = 0;
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
     * Gets the generation number of this population
     * @return the generation number
     */
    public int getGeneration(){
        return this.generation;
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
    public void setFitness(int index, double fitness){
        this.organisms.get(index).setFitness(fitness);
    }

    /**
     * Performs natural selection on the species in this population
     */
    public void naturalSelection(){
        System.out.println("\n\nGeneration: " + this.generation + "\nBEFORE NATURAL SELECTION");
        for(Organism org : this.organisms) {
            System.out.println(org);
        }

        this.speciate();
        this.sortSpecies();
        this.staleAndBadSpecies();
        ArrayList<Organism> organisms = new ArrayList<Organism>();

        for(Species species: this.species){
            int numBabies = (int)Math.round(species.getAvgFitness() / this.avgSum() * this.populationSize);
            species.reproduce(numBabies);
            organisms.addAll(species.getOrganisms());
        }

        Random random = new Random();
        //Check to see if the number of babies was less than population size
        while (organisms.size() < this.populationSize){
            Species species = this.species.get(random.nextInt(this.species.size()));
            Organism baby = species.addBaby();
            species.getOrganisms().add(baby);
            organisms.add(baby);
        }

        //Check to see if the number of babies went over the population size
        while(organisms.size() > this.populationSize) {
            organisms.remove(this.populationSize);
        }

        this.organisms = organisms;
        this.generation++;

        /*int counter = 0;
        System.out.println("\n\nAFTER NATURAL SELECTION");
        for(Species s: this.species) {
            System.out.printf("\nSpecies %2d - Avg. Fitness: %f", counter, s.getAvgFitness());
            for(Organism o : s.getOrganisms()) {
                System.out.printf("\n\t%s", o);
            }
            counter++;
        }*/
        System.out.println("\n\nAFTER NATURAL SELECTION");
        for(Organism org : this.organisms) {
            System.out.println(org);
            org.setFitness(0.0);
        }
    }

    /**
     * Separates the organisms into species based on their compatibility.
     */
    private void speciate() {
        for(Organism o : organisms) {
            if(this.species.isEmpty()){
                this.species.add(new Species());
                this.species.get(0).addOrganism(o);
                this.species.get(0).setCompatibilityNetwork(o.getNetwork());
                o.setSpecies(this.species.get(0));
            }else{
                boolean foundSpecies = false;
                Species originalSpecies = o.getSpecies();
                Network compatibleNetwork = o.getNetwork();
                for(Species species:this.species){

                    // Grab the first organism from a species to check if the organism if
                    // compatible with that species.
                    if(compatibleNetwork.compatible(species.getCompatibilityNetwork()) <
                            Constant.COMPAT_THRESH.getValue() && !foundSpecies){
                        species.addOrganism(o);
                        if(originalSpecies != null) {
                            originalSpecies.getOrganisms().remove(o);
                        }
                        o.setSpecies(species);
                        foundSpecies = true;
                    }
                }
                if(!foundSpecies) {
                    Species species = new Species();
                    species.addOrganism(o);
                    species.setCompatibilityNetwork(o.getNetwork());
                    if(originalSpecies != null) {
                        originalSpecies.getOrganisms().remove(o);
                    }
                    o.setSpecies(species);
                    this.species.add(species);
                }
            }
        }
        for(int i = 0; i < this.species.size(); i++) {
            Species toCheck = this.species.get(i);
            if(toCheck.getOrganisms().isEmpty()) {
                this.species.remove(i);
                i--;
            }
        }
    }

    /**
     * Sorts each of the species by best performing agents.
     */
    private void sortSpecies(){
        for(Species species: this.species){
            species.sort();
            species.cullSpecies();
            species.setChampion();
            species.setStaleness();
            species.shareFitness();
        }
    }

    /**
     * Sums all the averages in the species. Used to determine how many times the
     * @return the sum of all the species averages
     */
    private double avgSum(){
        double sum = 0;
        for(Species species : this.species){
            sum += species.getAvgFitness();
        }
        return sum;
    }

    /**
     * Kills off the stale and bad species.
     */
    private void staleAndBadSpecies(){
        ArrayList<Species> killSpecies = new ArrayList<Species>();
        Species bestSpecies = this.getMaxSpecies();
        for(Species species: this.species){

            if(!species.equals(bestSpecies)) {
                //Kill species that have not gotten better over a certain number of generations
                if (species.getStaleness() == Constant.STALENESS_THRESH.getValue()) {
                    killSpecies.add(species);
                } else if (species.getAvgFitness() / this.avgSum() * this.populationSize < 1) {

                    //Species that can't reproduce
                    killSpecies.add(species);
                }
            }
        }
        this.species.removeAll(killSpecies);
    }

    private Species getMaxSpecies() {
        Species maxSpecies = this.species.get(0);
        for(Species species : this.species) {
            if(species.getAvgFitness() > maxSpecies.getAvgFitness()) {
                maxSpecies = species;
            }
        }
        return maxSpecies;
    }
}
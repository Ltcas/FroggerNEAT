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

    /** The overall best organism that has gotten the highest fitness. */
    private Organism populationChampion;

    /** The input size of the networks for this population's organisms. */
    private int inputSize;

    /** The out size of the networks for this population's organisms. */
    private int outputSize;

    /**
     * Constructor that initializes the population based off of a population size
     * @param populationSize the number of organisms in the population
     */
    public Population(int populationSize,int inputSize,int outputSize){
        this.organisms      = new ArrayList<Organism>();
        this.species        = new ArrayList<Species>();
        this.populationSize = populationSize;
        this.populationChampion = null;
        this.generation     = 0;
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.initializePopulation();
    }

    /**
     * Populates the list of organism based off of the population size
     */
    private void initializePopulation(){
        for(int i = 0; i < this.populationSize; i++){
            this.organisms.add(new Organism(String.format("Organism #%d", i),this.inputSize,this.outputSize));
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
     * Sets the population champion for this population
     */
    private void setPopulationChampion() {
        Organism maxChamp = this.species.get(0).getChampion();
        for(Species species: this.species) {
            Organism champCheck = species.getChampion();
            if(champCheck.getFitness() > maxChamp.getFitness()) {
                maxChamp = champCheck;
            }
        }
        this.populationChampion = maxChamp;
    }

    /**
     * Performs natural selection on the species in this population
     */
    public void naturalSelection(){
        //System.out.printf("\n----- Generation %d -----", this.generation);

        this.speciate();
        this.sortSpecies();
        this.setPopulationChampion();
        this.staleAndBadSpecies();
        ArrayList<Organism> organisms = new ArrayList<Organism>();

        //int count = 0;
        for(Species species: this.species){
            int numBabies = (int)Math.round(species.getAvgFitness() / this.avgSum() * this.populationSize);
            //System.out.printf("\nSpecies %d gets %d babies", count++, numBabies);
            species.reproduce(numBabies);
            organisms.addAll(species.getOrganisms());
            //System.out.println();
        }

        Random random = new Random();
        //Check to see if the number of babies was less than population size
        while (organisms.size() < this.populationSize){
            Species species = this.species.get(random.nextInt(this.species.size()));
            Organism baby = (Organism) species.getChampion().clone();
            species.mutate(baby);
            /*System.out.print("\n\t\tAdded extra baby.");
            count = 0;
            for(Link l : baby.getNetwork().getLinks()) {
                if(!l.isEnabled()) {
                    count++;
                }
            }
            System.out.printf("\n\t\tBaby %d\t NumNodes: %d\t NumLinks: %d\t NumDisabledLinks: " +
                            "%d", organisms.size()+1, baby.getNetwork().getNumNodes(),
                                            baby.getNetwork().getNumLinks(), count);*/
            species.getOrganisms().add(baby);
            organisms.add(baby);
        }

        //Check to see if the number of babies went over the population size
        while(organisms.size() > this.populationSize) {
            organisms.remove(this.populationSize);
        }

        this.organisms = organisms;
        //System.out.println("Number of organisms after Natural Selection: " + this.organisms
        // .size());
        this.generation++;
    }

    /**
     * Separates the organisms into species based on their compatibility.
     */
    private void speciate() {
        for(Organism o : organisms) {
            if(this.species.isEmpty()){
                this.species.add(new Species());
                this.species.get(0).addOrganism(o);
                this.species.get(0).setCompatibilityNetwork(new Network(o.getNetwork()));
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
                    species.setCompatibilityNetwork(new Network(o.getNetwork()));
                    if(originalSpecies != null) {
                        originalSpecies.getOrganisms().remove(o);
                    }
                    o.setSpecies(species);
                    this.species.add(species);
                }
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
        //Species bestSpecies = this.getMaxSpecies();
        for(Species species: this.species){

            if(!species.getChampion().equals(this.populationChampion)) {
                //Kill species that have not gotten better over a certain number of generations
                if (species.getStaleness() == Constant.STALENESS_THRESH.getValue() || species.getOrganisms().isEmpty()) {
                    killSpecies.add(species);
                } else if (species.getAvgFitness() / this.avgSum() * this.populationSize < 1) {

                    //Species that can't reproduce
                    killSpecies.add(species);
                }
            }
        }
        this.species.removeAll(killSpecies);
    }
}
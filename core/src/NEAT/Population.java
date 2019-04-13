package NEAT;

import com.sun.org.apache.xpath.internal.operations.Or;

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
        this.speciate();
        this.sortSpecies();
        this.staleAndBadSpecies();
        ArrayList<Organism> organisms = new ArrayList<Organism>();

        for(Species species: this.species){
            int numBabies = (int)Math.round(species.getAvgFitness() / this.avgSum() * this.populationSize);

            // FIXME: 4/13/2019 This reproduce is when we get stuck in the infinite loop
            species.reproduce(numBabies);
            organisms.addAll(species.getOrganisms());
        }

        Random random = new Random();
        //Check to see if the number of babies was less than population size
        while (organisms.size() < this.populationSize){
            System.out.println("Species size : " + this.species.size());
            Species species = this.species.get(random.nextInt(this.species.size()));
            Organism baby = species.addBaby();
            species.getOrganisms().add(baby);
            organisms.add(baby);
        }

        //Check to see if the number of babies went over the population size
        while(organisms.size() > this.populationSize) {
            organisms.remove(this.populationSize);
        }

        // TODO: 4/2/2019 Maybe use an elimination variable in organisms instead of a whole new list.
        this.organisms = organisms;
        this.generation++;
}

    /**
     * Separates the organisms into species based on their compatibility.
     */
    private void speciate() {
//        for(int i = 0; i < this.species.size(); i++) {
//            Species checkEmpty = this.species.get(i);
//            if(checkEmpty.getOrganisms().isEmpty()) {
//                this.species.remove(checkEmpty);
//                i--;
//            }
//        }

        for(Organism o : organisms) {
            o.setToEliminate(true);
            if(this.species.isEmpty()){
                this.species.add(new Species());
                this.species.get(0).addOrganism(o);
            }else{
                boolean foundSpecies = false;
                Network compatibleNetwork = o.getNetwork();
                for(Species species:this.species){
                    Organism compatibleOrgCheck = species.getOrganisms().get(0);

                    // Grab the first organism from a species to check if the organism if
                    // compatible with that species.
                    if(compatibleNetwork.compatible(compatibleOrgCheck.getNetwork()) <
                            Constant.COMPAT_THRESH.getValue() && !foundSpecies){
                        species.addOrganism(o);
                        foundSpecies = true;
                    }
                }
                if(!foundSpecies) {
                    Species species = new Species();
                    species.addOrganism(o);
                    this.species.add(species);
                }
            }
        }

        for(int i = 0; i < this.species.size(); i++) {
            ArrayList<Organism> organismList = this.species.get(i).getOrganisms();
            for(int j = 0; j < organismList.size(); j++) {
                Organism toCheck = organismList.get(0);
                if(toCheck.isToEliminate()) {
                    organismList.remove(toCheck);
                    j--;
                }
            }
            if(organismList.isEmpty()) {
                this.species.remove(this.species.get(i));
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
            species.shareFitness();
            species.setChampion();
            species.setStaleness();
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
        for(Species species: this.species){

            //Kill species that have not gotten better over a certain number of generations
            if(species.getStaleness() == Constant.STALENESS_THRESH.getValue()){
                killSpecies.add(species);

            } else if(species.getAvgFitness() / this.avgSum() * this.populationSize < 1){

                //Species that can't reproduce
                killSpecies.add(species);
            }
        }
        this.species.removeAll(killSpecies);
    }
}

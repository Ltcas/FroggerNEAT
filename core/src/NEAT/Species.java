package NEAT;
;
import java.util.ArrayList;
import java.util.Random;

/**
 * Models a species
 * @author Chance Simmons and Brandon Townsend
 * @version 13 March 2019
 */
public class Species {
    /** List of organisms in this species */
    private ArrayList<Organism> organisms;

    /** Best fitness of any of the organisms in this species */
    private double bestFitness;

    /**Organism in this species that had the best fitness */
    private Organism champion;

    /** Used to measure how many generations it has been since a useful change has been made */
    private int staleness;

    /** Random number generator used to find probabilities */
    private Random randomGen;

    private Network compatibilityNetwork;

    /**
     * Constructor for a species.
     */
    public Species(){
        this.organisms = new ArrayList<Organism>();
        this.compatibilityNetwork = null;
        this.bestFitness = 0;
        this.staleness = 0;
        this.randomGen = new Random();
    }

    public Network getCompatibilityNetwork() {
        return compatibilityNetwork;
    }

    public void setCompatibilityNetwork(Network compatibilityNetwork) {
        this.compatibilityNetwork = compatibilityNetwork;
    }

    /**
     * Gets the average fitness of this species
     * @return average fitness
     */
    public double getAvgFitness(){
        double sum = 0;
        for(Organism o : this.organisms) {
            sum += o.getFitness();
        }
        return sum / this.organisms.size();
    }

    /**
     * Gets the staleness of this species
     * @return the staleness of the species
     */
    public int getStaleness(){
        return this.staleness;
    }

    /**
     * Gets the organisms of this list.
     * @return The list of organisms of this species.
     */
    public ArrayList<Organism> getOrganisms(){
        return this.organisms;
    }

    /**
     * Adds a organism to this species
     * @param organism the organism to be added
     */
    public void addOrganism(Organism organism){
        if(this.organisms.size() == 0){
            this.champion = (Organism)organism.clone();
            this.bestFitness = organism.getFitness();
        }
        this.organisms.add(organism);
    }

    /**
     * Sets the champion of this species if a new champion has been found
     */
    public void setChampion(){
        if(this.organisms.get(0).getFitness() > this.champion.getFitness()){
            this.champion = (Organism)this.organisms.get(0).clone();
            this.bestFitness = this.organisms.get(0).getFitness();
        }
    }

    public Organism getChampion() {
        return champion;
    }

    /**
     * Sets the staleness of this species.
     */
    public void setStaleness(){
        if(this.organisms.get(0).getFitness() < this.bestFitness){
            this.staleness++;
        }else{
            this.staleness = 0;
        }
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
     * Reproduce organisms
     * @param numBabies The number of babies this species is allowed to make.
     */
    public void reproduce(int numBabies){
        ArrayList<Organism> babies = new ArrayList<Organism>();
        Organism champClone = (Organism)this.champion.clone();
        babies.add(champClone);
        //System.out.printf("\n\tChampion: %s\n\tOther Babies:", champClone);

        for(int i = 1;i < numBabies;i++){
            Organism organism = addBaby();
            /*int count = 0;
            for(Link l : organism.getNetwork().getLinks()) {
                if(!l.isEnabled()) {
                    count++;
                }
            }
            System.out.printf("\n\t\tBaby %d\t NumNodes: %d\t NumLinks: %d\t NumDisabledLinks: " +
                    "%d", i, organism.getNetwork().getNumNodes(),
                    organism.getNetwork().getNumLinks(), count);*/
            organism.setGeneration(organism.getGeneration() + 1);
            babies.add(organism);
        }
        this.champion.setGeneration(this.champion.getGeneration() + 1);
        champClone.setGeneration(champClone.getGeneration() + 1);
        this.organisms = babies;
    }

    public Organism addBaby(){
        Organism organism;
        if(this.organisms.size() == 1){
            //System.out.print("\n\t\t\t>> Single Organism in the Species - clone and mutate.");
            organism = (Organism)this.organisms.get(0).clone();
            this.mutate(organism);
        }else if(this.randomGen.nextDouble() < Constant.MUT_THRESH.getValue()){
            //System.out.print("\n\t\t\t>> Hit Mutation Threshold - Clone random and mutate.");
            organism = (Organism)this.organisms.get(this.randomGen.nextInt(this.organisms.size())).clone();
            this.mutate(organism);
        }else{
            //System.out.print("\n\t\t\t>> Hit Crossover");
            organism = this.crossOver();
            if(this.randomGen.nextDouble() < Constant.MUT_THRESH.getValue()) {
                //System.out.print("\n\t\t\t\t>> Mutation after Crossover.");
                this.mutate(organism);
            }
        }
        return organism;
    }

    public Organism crossOver(){
        //Randomly choose two parents from the species
        Organism parentOne = this.organisms.get(this.randomGen.nextInt(this.organisms.size()));
        Organism parentTwo = this.organisms.get(this.randomGen.nextInt(this.organisms.size()));

        Organism maxParent;
        Organism leastParent;

        //Find the most fit parent
        if(parentOne.getFitness() > parentTwo.getFitness()){
            maxParent = parentOne;
            leastParent = parentTwo;
        }else{
            maxParent = parentTwo;
            leastParent = parentOne;
        }

        //Clone the most fit parent as the baby
        Organism baby = (Organism)maxParent.clone();

        //Get the list of links from the baby and the parent
        ArrayList<Link> babyLinks = baby.getNetwork().getLinks();
        ArrayList<Link> parentLinks = leastParent.getNetwork().getLinks();

        for(int i = 0;i < babyLinks.size();i++){
            for(Link otherLink: parentLinks){
                if(babyLinks.get(i).equals(otherLink)){
                    if(randomGen.nextDouble() > .5){ //Randomly choose which matching link to put in the network
                        babyLinks.get(i).setWeight(otherLink.getWeight());
                    }
                }else if(!babyLinks.contains(otherLink)){ //Check for disjoint links
                    Node input = baby.getNetwork().getNode(otherLink.getInput().getId());
                    Node output = baby.getNetwork().getNode(otherLink.getOutput().getId());
                    if(!(input == null) && !(output == null)){
                        Link newLink = baby.getNetwork().addLink(input,output);
                        newLink.setWeight(otherLink.getWeight());
                    }
                }
            }
        }
        return baby;
    }

    /**
     * Mutates a given organism
     * @param organism the organism to mutate
     */
    public void mutate(Organism organism){
        Network network = organism.getNetwork();
        if(this.randomGen.nextDouble() < Constant.ADD_NODE_MUT.getValue()){
            //System.out.print("\n\t\t\t>> Baby Below Hit Add Node Mutation");
            network.addNodeMutation();
        }
        if(this.randomGen.nextDouble() < Constant.ADD_LINK_MUT.getValue()){
            //System.out.print("\n\t\t\t>> Baby Below Hit Add Link Mutation");
            network.addLinkMutation();
        }
        if(this.randomGen.nextDouble() < Constant.WEIGHT_MUT.getValue()){
            //System.out.print("\n\t\t\t>> Baby Below Hit Link Weight Mutation");
            network.linkWeightMutation();
        }
        if(this.randomGen.nextDouble() < Constant.ENABLE_MUT.getValue()){
            //System.out.print("\n\t\t\t>> Baby Below Hit Link Toggle Mutation");
            //network.linkEnableMutation();
        }
        if(this.randomGen.nextDouble() < Constant.REENABLE_MUT.getValue()){
            //System.out.print("\n\t\t\t>> Baby Below Hit Link Reenable Mutation");
            //network.reenableLinkMutation();
        }
    }

    /**
     * Sorts the organisms in this species.
     */
    public void sort(){
        ArrayList<Organism> sortedOrganisms = new ArrayList<Organism>();
        for(int i = 0; i < this.organisms.size(); i++) {
            Organism maxOrganism = this.organisms.get(i);
            double maxFitness = maxOrganism.getFitness();
            for(Organism o : this.organisms) {
                if(o.getFitness() > maxFitness){
                    maxOrganism = o;
                }
            }
            this.organisms.remove(maxOrganism);
            sortedOrganisms.add(maxOrganism);
        }
        this.organisms = sortedOrganisms;
    }

    /**
     * Kill the low performing organisms in this species.
     */
    public void cullSpecies(){
        int cullNumber = (int)Math.round(this.organisms.size() * Constant.CULL_THRESH.getValue());
        for(int i = cullNumber;i< this.organisms.size();i++){
            this.removeOrganism(this.organisms.get(i));
        }
    }
}
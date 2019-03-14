package NEAT;

/**
 * Models a Genotype that is used to add genes and mutations to the networks.
 * @author Chance Simmons and Brandon Townsend
 * @version 25 February 2019
 */
public class Genome {
    /** ID of this genome */
    int id;

    /** Network of this Genome */
    Network network;

    /**
     * Constructor for a Genome
     */
    public Genome(Network network,int id){
        this.network = network;
        this.id = id;
    }

    /**
     * Mutation that enables a link in the network
     */
    public void linkEnableMutaion(){

    }

    /**
     * Mutation that adjusts a weight of a link
     */
    public void linkWeightMutation(){

    }

    /**
     * Mutation that adds a node to the genome/network.
     */
    public void addNodeMutation(){

    }

    /**
     * Mutation that enables the first disabled gene that is found.
     */
    public void reenableGeneMutation(){

    }

}

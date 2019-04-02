package NEAT;

import java.util.ArrayList;
import java.util.Random;

/**
 * Models a Genotype that is used to add genes and mutations to the networks.
 * @author Chance Simmons and Brandon Townsend
 * @version 25 February 2019
 */
public class Genome implements Cloneable {
    /** ID of this genome */
    int id;

    /** Network of this Genome */
    Network network;

    /** List of connection genes. */
    private ArrayList<Gene> genes;

    /**
     * Constructor for a Genome
     */
    public Genome(Network network, int id){
        this.network = network;
        this.genes = new ArrayList<Gene>();
        this.id = id;
    }

    /**
     * Checks to see if this genome is compatible with another genome.
     * @return the compatibility value
     */
    public double compatible(Genome genome){
        int excessCount = 0;
        int disjointCount = 0;
        double weightDiff = 0;
        double compatibility = 0;


        return compatibility;
    }

    /**
     * Mutation that enables a link in the network
     */
    public void linkEnableMutation(){
        Random geneChooser = new Random();
        if(this.genes.size() > 0){
            int geneNum = geneChooser.nextInt(genes.size() - 1);
            Link toToggle = genes.get(geneNum).getLink();
            toToggle.setEnabled(!toToggle.isEnabled());
        }
    }

    /**
     * Mutation that adjusts a weight of a link
     * his.weight = weightGenerator.nextDouble() * 2 - 1;
     */
    public void linkWeightMutation(){
        for(Gene g : genes) {
            if(!g.isFrozen()) {
                Random weightMutate = new Random();
                double howMuch = weightMutate.nextDouble();
                Link toMutate = g.getLink();

                // Here we completely change the weight of the link.
                if(howMuch < 0.1) {
                    toMutate.setWeight(weightMutate.nextDouble() * 2 - 1);
                } else { // Here we make a slight adjustment to the weight. Divide by a large
                    // number to make it tiny
                    toMutate.setWeight((weightMutate.nextDouble() * 2 - 1) / 50);
                    if(toMutate.getWeight() > 1) {
                        toMutate.setWeight(1);
                    } else if(toMutate.getWeight() < -1) {
                        toMutate.setWeight(-1);
                    }
                }
            }
        }
    }

    /**
     * A new connection gene is added between two previously unconnected nodes.
     */
    public void addConnectionMutation() {
        for(Gene inGene : genes) {
            Link inLink = inGene.getLink();
            for(Gene outGene : genes) {
                Link outLink = outGene.getLink();

                Node input = outLink.getInput();
                Node output = inLink.getOutput();

                // Maybe replace this with .equals
                if(input != output) {
                    Link toAdd = new Link(input, output);

                    // TODO: 3/31/2019 add innovation number.
                    this.genes.add(new Gene(toAdd, 0, 1));
                    this.network.addLink(toAdd);
                }
            }
        }
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
        for(Gene g : genes) {
            if(!g.isEnabled()) {
                g.setEnabled(true);
            }
        }
    }

    /**
     * Clones this Genome.
     * @return A clone of this genome.
     */
    @Override
    public Object clone() {
        Genome genome;
        try {
            genome = (Genome) super.clone();
        } catch (CloneNotSupportedException cne) {
            genome = new Genome(this.network, this.id);
        }
        genome.network = (Network) this.network.clone();
        genome.genes = (ArrayList<Gene>) this.genes.clone();
        return genome;
    }
}

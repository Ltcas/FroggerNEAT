package NEAT;

import java.util.ArrayList;

/**
 * Models a neural node that exists in our neural network.
 * @author Chance Simmons and Brandon Townsend
 * @version 2 April 2019
 */
public class Node {

    /** The identification number for this node. */
    private int id;

    /** The layer our node will be residing 'on'. */
    private NodeLayer layer;

    /** Sum of the inputs being passed into the node */
    private double inputSum;

    /** Output of the node */
    private double output;

    /** List that holds the outgoing links to other nodes */
    private ArrayList<Link> outgoingLinks;


    /**
     * Default constructor for a Node. Will create a node on the hidden layer with a bias of 0.
     */
    public Node(int id) {
        this(id, NodeLayer.HIDDEN);
    }

    /**
     * Creates a Node that is located on a specified layer with a specified bias.
     * @param layer The layer to place this node on.
     */
    public Node(int id, NodeLayer layer) {
        this.id = id;
        this.layer = layer;
        this.inputSum = 0;
        this.output = 0;
        this.outgoingLinks = new ArrayList<Link>();
    }

    public Node(int id, NodeLayer layer, double inputSum, double output) {
        this.id = id;
        this.layer = layer;
        this.inputSum = inputSum;
        this.output = output;
        this.outgoingLinks = new ArrayList<Link>();
    }

    /**
     * Returns the identification number of this node.
     * @return The identification number of this node.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the layer that our node is 'on'.
     * @return The layer that our node is 'on'.
     */
    public NodeLayer getLayer() {
        return layer;
    }

    public double getInputSum() {
        return this.inputSum;
    }

    /**
     * Adds the incoming sums to the input sum
     * @param sum the sum to be added to the input some
     */
    public void addInput(double sum){
        this.inputSum += sum;
    }

    /**
     * Sets the output of this node
     * @param output the new value of output
     */
    public void setOutput(double output){
        this.output = output;
    }

    /**
     * Returns the output value of this node.
     * @return The output value of this node.
     */
    public double getOutput() {
        return this.output;
    }

    /**
     * Returns the list of links that this node outputs to.
     * @return The list of links that this node outputs to.
     */
    public ArrayList<Link> getOutgoingLinks(){
        return this.outgoingLinks;
    }

    /**
     * Sigmoid activation function used to activate the node
     */
    public void activate() {
        if(this.layer != NodeLayer.INPUT){
            this.output = (1 /(1 + Math.pow(Math.E,(-1 * this.inputSum))));
        }
        for(Link link : this.outgoingLinks){
            if(link.isEnabled()){

                // Get's this link's output node and 'sends' something to it's input.
                link.getOutput().addInput(link.getWeight() * this.output);
            }
        }
        this.inputSum = 0;
    }

    @Override
    public boolean equals(Object o){
        boolean result = false;
        if(o instanceof Node){
            Node node = (Node)o;
            if(node.getId() == this.id){
                result = true;
            }
        }
        return result;
    }
}

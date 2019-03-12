package NEAT;

import java.util.ArrayList;
import java.util.Random;

/**
 * Models a neural node that exists in our neural network.
 * @author Chance Simmons and Brandon Townsend
 * @version 25 February 2019
 */
public class Node {

    /** The layer our node will be residing 'on'. */
    private NodeLayer layer;

    /** The bias our node will have. */
    private double bias;

    /** Sum of the inputs being passed into the node */
    private double inputSum;

    /** Output of the node */
    private double output;

    /** List that holds the outgoing links to other nodes */
    private ArrayList<Link> outgoingLinks;


    /**
     * Default constructor for a Node. Will create a node on the hidden layer with a bias of 0.
     */
    public Node() {
        this(NodeLayer.HIDDEN);
    }

    /**
     * Creates a Node that is located on a specified layer with a specified bias.
     * @param layer The layer to place this node on.
     */
    public Node(NodeLayer layer) {
        Random random = new Random();
        this.layer  = layer;
        this.inputSum = 0;
        this.output = 0;
        this.outgoingLinks = new ArrayList<Link>();
        this.bias   = random.nextDouble();
    }

    /**
     * Returns the layer that our node is 'on'.
     * @return The layer that our node is 'on'.
     */
    public NodeLayer getLayer() {
        return layer;
    }

    /**
     * Modifies what layer our node is 'on'.
     * @param layer The new layer our node will reside 'on'.
     */
    public void setLayer(NodeLayer layer) {
        this.layer = layer;
    }

    /**
     * Returns the bias of our node.
     * @return The bias of our node.
     */
    public double getBias() {
        return bias;
    }

    /**
     * Modifies what the bias of our node will be.
     * @param bias The new bias of our node.
     */
    public void setBias(double bias) {
        this.bias = bias;
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

    public double getOutput() {
        return this.output;
    }

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
        for(int i = 0;i < this.outgoingLinks.size();i++){
            Link connection = this.outgoingLinks.get(i);
            if(connection.isEnabled()){
                connection.getOutput().addInput(connection.getWeight() * this.output);
            }
        }
        this.inputSum = 0;
    }
}

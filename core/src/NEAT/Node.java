package NEAT;

/**
 * Models a neural node that exists in our neural network.
 * @author Chance Simmons and Brandon Townsend
 * @version 25 February 2019
 */
public class Node {

    /** Enum that represents what layer our Node will be on. */
    public enum nodeLayer {
        HIDDEN,
        INPUT,
        OUTPUT
    }

    /** The layer our node will be residing 'on'. */
    private nodeLayer layer;

    /** The bias our node will have. */
    private double bias;

    /**
     * Default constructor for a Node. Will create a node on the hidden layer with a bias of 0.
     */
    public Node() {
        this.layer  = nodeLayer.HIDDEN;
        this.bias   = 0;
    }

    /**
     * Creates a Node that is located on a specified layer with a specified bias.
     * @param layer The layer to place this node on.
     * @param bias The bias to place upon this node.
     */
    public Node(nodeLayer layer, double bias) {
        this.layer  = layer;
        this.bias   = bias;
    }

    /**
     * Returns the layer that our node is 'on'.
     * @return The layer that our node is 'on'.
     */
    public nodeLayer getLayer() {
        return layer;
    }

    /**
     * Modifies what layer our node is 'on'.
     * @param layer The new layer our node will reside 'on'.
     */
    public void setLayer(nodeLayer layer) {
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

    public void activationFunction() {
        //todo
    }
}

package NEAT;

/**
 * Class that models the links between nodes in our network.
 * @author Chance Simmons and Brandon Townsend
 * @version 25 February 2019
 */
public class Link {

    /** The weight of the connection between the input and output nodes. */
    private double weight;

    /** The node our connection is coming from. */
    private Node input;

    /** The node our connection is going to. */
    private Node output;

    /**
     * Constructor for a Link object. Creates it based on a passed in weight, input, and output
     * node.
     * @param weight The given weight for this connection.
     * @param in The specified input node for this connection.
     * @param out The specified output node for this connection.
     */
    public Link(double weight, Node in, Node out) {
        this.weight = weight;
        this.input  = in;
        this.output = out;
    }

    /**
     * Returns the weight of this connection link.
     * @return The weight of this connection link.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Used to adjust the weight of this connection link.
     * @param weight The new weight this link should be set to.
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Returns the input node for this connection link.
     * @return The input node for this connection link.
     */
    public Node getInput() {
        return input;
    }

    /**
     * Sets a new input node for this connection link.
     * @param input The specified node to be the new input node.
     */
    public void setInput(Node input) {
        this.input = input;
    }

    /**
     * Returns the output node for this connection link.
     * @return The output node for this connection link.
     */
    public Node getOutput() {
        return output;
    }

    /**
     * Sets a new output node for this connection link.
     * @param output The specified node to be the new input node.
     */
    public void setOutput(Node output) {
        this.output = output;
    }
}
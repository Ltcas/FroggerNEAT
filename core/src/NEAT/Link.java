package NEAT;

import java.util.Random;

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

    /**Used to show if the link is enabled or not */
    private boolean enabled;

    private int innovationNum;
    /**
     * Constructor for a Link object. Creates it based on a passed in weight, input, and output
     * node.
     * @param in The specified input node for this connection.
     * @param out The specified output node for this connection.
     * @param innovationNum The specified innovation number of this link.
     */
    public Link(Node in, Node out,int innovationNum) {
        Random weightGenerator = new Random();
        this.weight = weightGenerator.nextDouble() * 2 - 1;
        this.input  = in;
        this.output = out;
        this.enabled = true;
        this.innovationNum = innovationNum;
    }

    /**
     * Returns the weight of this connection link.
     * @return The weight of this connection link.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Gets the innovation number for this link
     * @return the innovation number
     */
    public int getInnovationNum(){
        return this.innovationNum;
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
     * Returns the value of enabled
     * @return true if the link is enabled
     */
    public boolean isEnabled(){
        return enabled;
    }

    /**
     * Sets whether or not this Link is enabled.
     * @param enabled Whether this Link is enabled or not.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Sets a new output node for this connection link.
     * @param output The specified node to be the new input node.
     */
    public void setOutput(Node output) {
        this.output = output;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Link) {
            Link link = (Link) o;
            return link.getInnovationNum() == this.getInnovationNum();
        }
        return false;
    }
}

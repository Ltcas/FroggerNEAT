package NEAT;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class that models our neural network.
 * @author Chance Simmons and Brandon Townsend
 * @version 25 February 2019
 */
public class Network implements Cloneable {

    /** Identification number for this network. */
    private int id;

    /** List of the input nodes for this network. */
    private ArrayList<Node> inNodes;

    /** List of the output nodes for this network. */
    private ArrayList<Node> outNodes;

    /** List of the hidden nodes for this network. */
    private ArrayList<Node> hiddenNodes;

    /** List of the links of this network. */
    private ArrayList<Link> links;

    /** Innovation List */
    private static ArrayList<Link> innovationList = new ArrayList<Link>();

    /** Bias node of the network */
    private Node biasNode;

    /**
     * Constructor that only takes an identification number and initializes empty list.
     * @param id The identification number of the new network.
     */
    public Network(int id) {
        this(id, new ArrayList<Node>(), new ArrayList<Node>(), new ArrayList<Node>(),
                new ArrayList<Link>());
    }

    /**
     * Constructor that builds a network based on passed nodes, but no links between those nodes.
     * @param id The identification number of the new network.
     * @param in A list of initialized input nodes.
     * @param out A list of initialized output nodes.
     * @param hid A list of initialized hidden nodes.
     */
    public Network(int id, ArrayList<Node> in, ArrayList<Node> out, ArrayList<Node> hid) {
        this(id, in, out, hid, new ArrayList<Link>());
    }

    /**
     * Constructor that builds a network based on passed nodes and links.
     * @param id The identification number of the new network.
     * @param in A list of initialized input nodes.
     * @param out A list of initialized output nodes.
     * @param hid A list of initialized hidden nodes.
     * @param links A list of initialized links.
     */
    public Network(int id, ArrayList<Node> in, ArrayList<Node> out, ArrayList<Node> hid,
                   ArrayList<Link> links) {
        this.id             = id;
        this.inNodes        = in;
        this.outNodes       = out;
        this.hiddenNodes    = hid;
        this.links          = links;
    }

    /**
     * Constructor that initializes a network based upon the number of nodes we would like to
     * create for our input and output layers.
     * @param id The identification number of the new network.
     * @param inCount The number of input nodes.
     * @param outCount The number of output nodes.
     */
    public Network(int id, int inCount, int outCount){
        this.inNodes        = new ArrayList<Node>();
        this.hiddenNodes    = new ArrayList<Node>();
        this.outNodes       = new ArrayList<Node>();
        this.links          = new ArrayList<Link>();
        this.biasNode = new Node(-1, NodeLayer.BIAS);
        this.biasNode.setOutput(1);
        this.id             = id;
        this.createNetwork(inCount,outCount);
    }

    /**
     * Initializes a network by creating and adding nodes to this network's list of nodes.
     * @param inCount Number of input nodes.
     * @param outCount Number of output nodes.
     */
    private void createNetwork(int inCount,int outCount){
        for(int i = 0; i < inCount; i++){
            addNode(new Node(this.getNumNodes(), NodeLayer.INPUT));
        }

        for(int i = 0; i < outCount; i++){
            addNode(new Node(this.getNumNodes(), NodeLayer.OUTPUT));
        }

        for(Node in : this.inNodes) {
            for(Node out : this.outNodes) {
                this.addLink(in,out);
            }
        }
    }

    /**
     * Returns the identification number of this network.
     * @return The identification number of this network.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the links in this network
     * @return the links of this network
     */
    public ArrayList<Link> getLinks(){
        return this.links;
    }

    /**
     * Returns the number of nodes in this network.
     * @return The number of nodes in this network.
     */
    public int getNumNodes() {
        return this.inNodes.size() + this.outNodes.size() + this.hiddenNodes.size();
    }

    /**
     * Returns the number of links in this network.
     * @return The number of links in this network.
     */
    public int getNumLinks() {
        return this.links.size();
    }

    /**
     * Adds a node to the network.
     * @param node The node to be added.
     * @return True if the node is added, false otherwise.
     */
    public boolean addNode(Node node) {
        boolean result;

        // Adding the node based on what layer it should be added to.
        if(node.getLayer() == NodeLayer.INPUT) {
            result = this.inNodes.add(node);
        } else if(node.getLayer() == NodeLayer.OUTPUT) {
            //Negative Innovation for bias node
            this.biasNode.getOutgoingLinks().add(new Link(this.biasNode,node,-1));
            result = this.outNodes.add(node);
        } else {
            //Negative Innovation for bias node
            this.biasNode.getOutgoingLinks().add(new Link(this.biasNode,node,-1));
            result = this.hiddenNodes.add(node);
        }
        return result;
    }

    /**
     * Removes a node from the network.
     * @param node The node to be removed.
     * @return True if the node is removed, false otherwise.
     */
    public boolean removeNode(Node node) {
        boolean result = true;

        // Performing check to see if any lists are empty.
        if(inNodes.isEmpty() && outNodes.isEmpty() && hiddenNodes.isEmpty()) {
            result = false;
            System.out.println("Node cannot be removed as all lists are empty.");
        } else {

            // Checking to see if the node is in any list.
            if(inNodes.contains(node)) {
                inNodes.remove(node);
            } else if(outNodes.contains(node)) {
                inNodes.remove(node);
            } else if(hiddenNodes.contains(node)) {
                hiddenNodes.remove(node);
            } else {
                result = false;
                System.out.println("Node cannot be removed as it is not in any list.");
            }
        }
        return result;
    }

    /**
     * Adds a link to the network.
     * @param inNode the input node of the link
     * @param outNode the output node of the link
     * @return True if the link is added, false otherwise.
     */
    public boolean addLink(Node inNode,Node outNode) {
        boolean result = true;
        int innovationNum = Network.innovationList.size();
        for(int i = 0;i < Network.innovationList.size();i++){
            int inputID = Network.innovationList.get(i).getInput().getId();
            int outputID = Network.innovationList.get(i).getOutput().getId();
            //If the innovation is identical to one in the overall list, set the new links
            // innovation number to that innovation number.
            if(inputID == inNode.getId() && outputID == outNode.getId()){
                innovationNum = i;
            }
        }
        //If a new innovation was created, add it to the overall list
        if(innovationNum == Network.innovationList.size()){
            Network.innovationList.add(new Link(inNode,outNode,innovationNum));
        }

        this.links.add(new Link(inNode,outNode,innovationNum));
        return result;
    }

    /**
     * Removes a link from the network.
     * @param link The link to be removed.
     * @return True if the link is removed, false otherwise.
     */
    public boolean removeLink(Link link) {
        boolean result = true;

        // Performing check to see if the list is empty.
        if(this.links.isEmpty()) {
            result = false;
            System.out.println("Link cannot be removed as the list of links is empty.");
        } else {

            // Checking to see if the link is in the list.
            if(this.links.contains(link)) {
                this.links.remove(link);
            } else {
                result = false;
                System.out.println("Link cannot be removed as it is not in the list of links.");
            }
        }
        return result;
    }

    /**
     * Effectively destroys this network by removing all of its nodes and links.
     * @return True when it has finished removing everything.
     */
    public boolean destroy() {
        for(Node i : inNodes) {
            removeNode(i);
        }
        for(Node h : hiddenNodes) {
            removeNode(h);
        }
        for(Node o : outNodes) {
            removeNode(o);
        }
        for(Link l : links) {
            removeLink(l);
        }
        return true;
    }
    
    public double[] feedForward(int[][] playerVision) {
        double[] result = new double[outNodes.size()];
        int count = 0;

        // Setting up input nodes based on what the player can see.
        for(int row = 0; row < playerVision.length; row++) {
            for(int col = 0; col < playerVision[row].length; col++) {
                Node i = this.inNodes.get(count);
                i.setOutput(playerVision[row][col]);
                count++;
            }
        }
        // activating all the nodes.
        this.biasNode.activate();
        for(Node i : inNodes) {
            i.activate();
        }
        for(Node h : hiddenNodes) {
            h.activate();
        }
        count = 0;
        for(Node o : outNodes) {
            o.activate();
            result[count++] = o.getOutput();
        }

        return result;
    }

    /**
     * Checks to see if this genome is compatible with another genome.
     * @return the compatibility value
     */
    public double compatible(Network network){
        int excessCount = 0;
        int disjointCount = 0;
        double weightDiff = 0;
        double compatibility = 0;
        for(Link link: this.links){

        }

        return compatibility;
    }

    /**
     * Mutation that enables a link in the network
     */
    public void linkEnableMutation(){
        Random geneChooser = new Random();
        if(this.links.size() > 0){
            int geneNum = geneChooser.nextInt(links.size() - 1);
            Link toToggle = this.links.get(geneNum);
            toToggle.setEnabled(!toToggle.isEnabled());
        }
    }

    /**
     * Mutation that adjusts a weight of a link
     * his.weight = weightGenerator.nextDouble() * 2 - 1;
     */
    public void linkWeightMutation(){
        for(Link link : this.links) {
            if(!link.isEnabled()) {
                Random weightMutate = new Random();
                double howMuch = weightMutate.nextDouble();

                // Here we completely change the weight of the link.
                if(howMuch < 0.1) {
                    link.setWeight(weightMutate.nextDouble() * 2 - 1);
                } else { // Here we make a slight adjustment to the weight. Divide by a large
                    // number to make it tiny
                    link.setWeight((weightMutate.nextDouble() * 2 - 1) / 50);
                    if(link.getWeight() > 1) {
                        link.setWeight(1);
                    } else if(link.getWeight() < -1) {
                        link.setWeight(-1);
                    }
                }
            }
        }
    }

    /**
     * A new connection gene is added between two previously unconnected nodes.
     */
    public void addLinkMutation() {
        for(Link inLink : this.links) {
            for(Link outLink : this.links) {

                Node input = outLink.getInput();
                Node output = inLink.getOutput();

                // Maybe replace this with .equals
                if(input != output) {
                    //TODO:Fix adding a link here
                    //Link toAdd = new Link(input, output);
                    //this.addLink(toAdd);
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
    public void reenableLinkMutation(){
        for(Link link: this.links) {
            if(!link.isEnabled()) {
                link.setEnabled(true);
            }
        }
    }

    /**
     * Returns a string representation of this network.
     * @return A String representation of this network.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("\nInput Nodes:");
        int count = 0;
        for(Node i : inNodes) {
            str.append(String.format("\n\tNode %d - Bias: %f - Output: %f", count++, i.getOutput()));
            for(Link l : i.getOutgoingLinks()) {
                str.append(String.format("\n\t\tLink Weight: %f", l.getWeight()));
            }
        }
        str.append("\nHidden Nodes:");
        count = 0;
        for(Node h : hiddenNodes) {
            str.append(String.format("\n\tNode %d - Bias: %f - Output: %f", count++, h.getOutput()));
            for(Link l : h.getOutgoingLinks()) {
                str.append(String.format("\n\t\tLink Weight: %f", l.getWeight()));
            }
        }
        str.append("\nOutput Nodes:");
        count = 0;
        for(Node o : outNodes) {
            str.append(String.format("\n\tNode %d - Bias: %f - Output: %f", count++, o.getOutput()));
//            for(Link l : o.getOutgoingLinks()) {
//                str.append(String.format("\n\t\tLink Weight: %f", l.getWeight()));
//            }
        }
        return str.toString();
    }

    /**
     * Clones this network.
     * @return A copy of this network's clone.
     */
    @Override
    public Object clone() {
        Network network;
        try {
            network = (Network)super.clone();
        } catch (CloneNotSupportedException cne) {
            network = new Network(this.id, this.inNodes, this.outNodes, this.hiddenNodes,
                    this.links);
        }
        network.inNodes = (ArrayList<Node>) this.inNodes.clone();
        network.outNodes = (ArrayList<Node>) this.outNodes.clone();
        network.hiddenNodes = (ArrayList<Node>) this.hiddenNodes.clone();
        network.links = (ArrayList<Link>) this.links.clone();
        return network;
    }
}

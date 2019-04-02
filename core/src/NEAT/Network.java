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

    /** Integer representation for the number of nodes in our network. */
    private int numNodes;

    /** Integer representation for the number of links in our network. */
    private int numLinks;

    /** List of the input nodes for this network. */
    private ArrayList<Node> inNodes;

    /** List of the output nodes for this network. */
    private ArrayList<Node> outNodes;

    /** List of the hidden nodes for this network. */
    private ArrayList<Node> hiddenNodes;

    /** List of the links of this network. */
    private ArrayList<Link> links;

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
        this.biasNode = new Node(NodeLayer.BIAS);
        this.biasNode.setOutput(1);
        this.numNodes       = inCount + outCount;
        this.numLinks       = 0;
        this.id             = id;
        this.createNetwork(inCount,outCount);
    }

    /**
     * Initializes a network by creating and adding nodes to this network's list of nodes.
     * @param inCount Number of input nodes.
     * @param outCount Number of output nodes.
     */
    private void createNetwork(int inCount,int outCount){
        for(int i = 0;i < inCount;i++){
            addNode(new Node(NodeLayer.INPUT));
            this.inNodes.get(i).setBias(0);
        }

        for(int i = 0;i < outCount;i++){
            Node outNode = new Node(NodeLayer.OUTPUT);
            this.biasNode.getOutgoingLinks().add(new Link(this.biasNode,outNode));
            addNode(outNode);
        }

        for(Node i : this.inNodes) {
            for(Node o : this.outNodes) {
                this.addLink(new Link(i, o));
            }
        }
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

        int nodeCount = 0;
        int linkCount = 0;

        // Counting up the number of nodes in each list, if any
        if(!inNodes.isEmpty() || !outNodes.isEmpty() || !hiddenNodes.isEmpty()) {
            nodeCount = inNodes.size() + outNodes.size() + hiddenNodes.size();
        }

        // Counting up the number of links, if any
        if(!this.links.isEmpty()) {
            linkCount = this.links.size();
        }

        this.numNodes = nodeCount;
        this.numLinks = linkCount;
    }

    /**
     * Returns the identification number of this network.
     * @return The identification number of this network.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the number of nodes in this network.
     * @return The number of nodes in this network.
     */
    public int getNumNodes() {
        return numNodes;
    }

    /**
     * Returns the number of links in this network.
     * @return The number of links in this network.
     */
    public int getNumLinks() {
        return numLinks;
    }

    /**
     * Adds a node to the network.
     * @param n The node to be added.
     * @return True if the node is added, false otherwise.
     */
    public boolean addNode(Node n) {
        boolean result = true;

        // Adding the node based on what layer it should be added to.
        if(n.getLayer() == NodeLayer.INPUT) {
            result = this.inNodes.add(n);
        } else if(n.getLayer() == NodeLayer.OUTPUT) {
            result = this.outNodes.add(n);
        } else {
            result = this.hiddenNodes.add(n);
        }
        if(result) {
            this.numNodes++;
        }
        return result;
    }

    /**
     * Removes a node from the network.
     * @param n The node to be removed.
     * @return True if the node is removed, false otherwise.
     */
    public boolean removeNode(Node n) {
        boolean result = true;

        // Performing check to see if any lists are empty.
        if(inNodes.isEmpty() && outNodes.isEmpty() && hiddenNodes.isEmpty()) {
            result = false;
            System.out.println("Node cannot be removed as all lists are empty.");
        } else {

            // Checking to see if the node is in any list.
            if(inNodes.contains(n)) {
                inNodes.remove(n);
            } else if(outNodes.contains(n)) {
                inNodes.remove(n);
            } else if(hiddenNodes.contains(n)) {
                hiddenNodes.remove(n);
            } else {
                result = false;
                System.out.println("Node cannot be removed as it is not in any list.");
            }
        }
        if(result) {
            this.numNodes--;
        }
        return result;
    }

    /**
     * Adds a link to the network.
     * @param l The link to be added.
     * @return True if the link is added, false otherwise.
     */
    public boolean addLink(Link l) {
        boolean result = true;
        result = this.links.add(l);
        if(result) {
            l.getInput().getOutgoingLinks().add(l);
            this.numLinks++;
        }
        return result;
    }

    /**
     * Removes a link from the network.
     * @param l The link to be removed.
     * @return True if the link is removed, false otherwise.
     */
    public boolean removeLink(Link l) {
        boolean result = true;

        // Performing check to see if the list is empty.
        if(this.links.isEmpty()) {
            result = false;
            System.out.println("Link cannot be removed as the list of links is empty.");
        } else {

            // Checking to see if the link is in the list.
            if(this.links.contains(l)) {
                this.links.remove(l);
            } else {
                result = false;
                System.out.println("Link cannot be removed as it is not in the list of links.");
            }
        }
        if(result) {
            this.numLinks--;
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
                    Link toAdd = new Link(input, output);
                    this.addLink(toAdd);
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
            str.append(String.format("\n\tNode %d - Bias: %f - Output: %f", count++, i.getBias(),
             i.getOutput()));
            for(Link l : i.getOutgoingLinks()) {
                str.append(String.format("\n\t\tLink Weight: %f", l.getWeight()));
            }
        }
        str.append("\nHidden Nodes:");
        count = 0;
        for(Node h : hiddenNodes) {
            str.append(String.format("\n\tNode %d - Bias: %f - Output: %f", count++, h.getBias(),
             h.getOutput()));
            for(Link l : h.getOutgoingLinks()) {
                str.append(String.format("\n\t\tLink Weight: %f", l.getWeight()));
            }
        }
        str.append("\nOutput Nodes:");
        count = 0;
        for(Node o : outNodes) {
            str.append(String.format("\n\tNode %d - Bias: %f - Output: %f", count++, o.getBias(),
             o.getOutput()));
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

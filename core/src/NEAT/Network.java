package NEAT;

import java.util.ArrayList;

/**
 * Class that models our neural network.
 * @author Chance Simmons and Brandon Townsend
 * @version 25 February 2019
 */
public class Network {

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

    /**
     * Constructor that only takes an identification number and initializes empty list.
     * @param id The identification number of the new network.
     */
    public Network(int id) {
        this(id, new ArrayList<Node>(), new ArrayList<Node>(), new ArrayList<Node>(),
                new ArrayList<Link>());
    }

    public Network(int id,int inCount,int outCount){
        this.inNodes = new ArrayList<Node>();
        this.hiddenNodes = new ArrayList<Node>();
        this.outNodes = new ArrayList<Node>();
        this.links = new ArrayList<Link>();
        this.numNodes = inCount + outCount;
        this.numLinks = 0;
        this.id = id;
        this.createNetwork(inCount,outCount);
    }

    private void createNetwork(int inCount,int outCount){
        for(int i = 0;i < inCount;i++){
            this.inNodes.add(new Node(NodeLayer.INPUT, 0));
        }

        for(int i = 0;i < outCount;i++){
            this.outNodes.add(new Node(NodeLayer.OUTPUT, 0));
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
            this.inNodes.add(n);
        } else if(n.getLayer() == NodeLayer.OUTPUT) {
            this.outNodes.add(n);
        } else {
            this.hiddenNodes.add(n);
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
        this.links.add(l);
        if(result) {
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
}

package NEAT;


import java.util.ArrayList;
import java.util.Random;

/**
 * Class that models our neural network.
 * @author Chance Simmons and Brandon Townsend
 * @version 25 February 2019
 */
public class Network {

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

    public Network(Network oldNetwork) {
        this.id             = oldNetwork.getId();
        this.inNodes        = new ArrayList<Node>();
        this.hiddenNodes    = new ArrayList<Node>();
        this.outNodes       = new ArrayList<Node>();
        this.links          = new ArrayList<Link>();
        this.biasNode       = new Node(-1, NodeLayer.BIAS);
        this.biasNode.setOutput(1);

        for(Node input : oldNetwork.inNodes) {
            Node toAdd = new Node(input.getId(), NodeLayer.INPUT, input.getInputSum(),
                    input.getOutput());
            this.addNode(toAdd);
            //System.out.printf("\n-> Old Node Mem Address: %s\t New Node Mem Address: %s", input,
            //        toAdd);
        }
        for(Node hidden : oldNetwork.hiddenNodes) {
            Node toAdd = new Node(hidden.getId(), NodeLayer.HIDDEN, hidden.getInputSum(),
                    hidden.getOutput());
            this.addNode(toAdd);
            //System.out.printf("\n-> Old Node Mem Address: %s\t New Node Mem Address: %s", hidden,
            //        toAdd);
        }
        for(Node output : oldNetwork.outNodes) {
            Node toAdd = new Node(output.getId(), NodeLayer.OUTPUT, output.getInputSum(),
                    output.getOutput());
            this.addNode(toAdd);
            //System.out.printf("\n-> Old Node Mem Address: %s\t New Node Mem Address: %s", output,
            //        toAdd);
        }
        int inputIndex;
        int outputIndex;
        Node newInput;
        Node newOutput;

        // Below is something atrocious, I know. It's the best way I could think about doing it
        // though. Sets up links based on the nodes.
        for(Link oldLink : oldNetwork.getLinks()) {
            Node oldLinkInput = oldLink.getInput();
            Node oldLinkOutput = oldLink.getOutput();
            if(this.inNodes.contains(oldLinkInput)) {
                inputIndex = this.inNodes.indexOf(oldLinkInput);
                newInput = this.inNodes.get(inputIndex);
                if(this.hiddenNodes.contains(oldLinkOutput)) {
                    outputIndex = this.hiddenNodes.indexOf(oldLinkOutput);
                    newOutput = this.hiddenNodes.get(outputIndex);
                } else {
                    outputIndex = this.outNodes.indexOf(oldLinkOutput);
                    newOutput = this.outNodes.get(outputIndex);
                }
            } else {
                inputIndex = this.hiddenNodes.indexOf(oldLinkInput);
                outputIndex = this.outNodes.indexOf(oldLinkOutput);
                newInput = this.hiddenNodes.get(inputIndex);
                newOutput = this.outNodes.get(outputIndex);
            }
            Link toAdd = addLink(newInput, newOutput);
            toAdd.setWeight(oldLink.getWeight());
            toAdd.setEnabled(oldLink.isEnabled());
            //System.out.printf("\n\t-> Old Link Mem Address: %s\t New Link Mem Address: %s",
            //        oldLink, toAdd);
        }
    }

    /**
     * Initializes a network by creating and adding nodes to this network's list of nodes.
     * @param inCount Number of input nodes.
     * @param outCount Number of output nodes.
     */
    private void createNetwork(int inCount,int outCount){

        // Creates all input layer nodes.
        for(int i = 0; i < inCount; i++){
            addNode(new Node(this.getNumNodes(), NodeLayer.INPUT));
        }

        // Creates all output layer nodes.
        for(int i = 0; i < outCount; i++){
            addNode(new Node(this.getNumNodes(), NodeLayer.OUTPUT));
        }

        // Fully connects all input layer nodes to all output layer nodes.
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
     * @return the link that was added
     */
    public Link addLink(Node inNode,Node outNode) {
        int innovationNum = Network.innovationList.size();

        for(int i = 0; i < Network.innovationList.size(); i++){
            Link toCheck = Network.innovationList.get(i);
            int inputID = toCheck.getInput().getId();
            int outputID = toCheck.getOutput().getId();

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

        Link toAdd = new Link(inNode, outNode, innovationNum);
        this.links.add(toAdd);
        inNode.getOutgoingLinks().add(toAdd);
        return toAdd;
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
     * Gets a node that has a certain ID.
     * @param id the ID to search for
     * @return the node
     */
    public Node getNode(int id){
        for(Node node: this.inNodes){
            if(node.getId() == id){
                return node;
            }
        }

        for(Node node:this.hiddenNodes){
            if(node.getId() == id){
                return node;
            }
        }

        for(Node node:this.outNodes){
            if(node.getId() == id){
                return node;
            }
        }

        return null;
    }

    /**
     * Checks to see if this genome is compatible with another genome.
     * @return the compatibility value
     */
    public double compatible(Network network){
        double weightSum = 0;
        int matchingCount = 0;
        int disjointCount = 0;
        double weightDiff;
        double compatibility;

        //Find the number of matching links, as well as the weight difference with the matching links
        for(Link link: this.links){
            for(Link otherLink:network.getLinks()){
                if(link.equals(otherLink)){
                    matchingCount++;
                    weightSum += Math.abs(link.getWeight() - otherLink.getWeight());
                }
            }
        }

        //Find the disjoint count based off of the max number of links
        if(this.links.size() > network.getLinks().size()){
            disjointCount = this.getNumLinks() - matchingCount;
        }else if(this.links.size() < network.getLinks().size()){
            disjointCount = network.getNumLinks() - matchingCount;
        }

        weightDiff = weightSum / matchingCount;

        compatibility = (disjointCount * Constant.DISJOINT_COEFF.getValue()) + (weightDiff *
                Constant.WEIGHT_COEFF.getValue());

        return compatibility;
    }

    /**
     * Mutation that enables a link in the network
     */
    public void linkEnableMutation(){
        if(this.links.size() > 0) {
            Random geneChooser = new Random();
            int geneNum = geneChooser.nextInt(links.size() - 1);
            Link toToggle = this.links.get(geneNum);
            toToggle.setEnabled(!toToggle.isEnabled());
        }
    }

    /**
     * Mutation that enables the first disabled gene that is found.
     */
    public void reenableLinkMutation(){
        boolean foundLink = false;
        for(int i = 0; !foundLink && i < this.links.size(); i++) {
            Link link = this.links.get(i);
            if(!link.isEnabled()) {
                link.setEnabled(true);
                foundLink = true;
            }
        }
    }

    /**
     * Mutation that adjusts a weight of a link
     * this.weight = weightGenerator.nextDouble() * 2 - 1;
     */
    public void linkWeightMutation(){
        Random random = new Random();
        for(Link link : this.links) {
            double howMuch = Math.random();

            // Here we completely change the weight of the link.
            if(howMuch < 0.1) {
                link.setWeight(Math.random() * 2 - 1);
            } else { // Here we make a slight adjustment to the weight. Divide by a large
                // number to make it tiny
                link.setWeight(link.getWeight() + random.nextGaussian() / 50);
                if(link.getWeight() > 1) {
                    link.setWeight(1);
                } else if(link.getWeight() < -1) {
                    link.setWeight(-1);
                }
            }
        }
    }

    /**
     * A new connection gene is added between two previously unconnected nodes.
     */
    public void addLinkMutation() {
        boolean found = false;
        Random random = new Random();

        // Check to see if it's a fully connected network.
        if(this.hiddenNodes.isEmpty()) {
            found = true;
        } else {
            int totalPossibleLinks = this.inNodes.size() * this.outNodes.size();
            totalPossibleLinks += this.hiddenNodes.size() * (this.inNodes.size() + this.outNodes.size());

//            System.out.println("Total possible links: " + totalPossibleLinks);
//            System.out.println("Difference: " + (totalPossibleLinks - this.links.size()));
//            System.out.println("Size of hidden nodes: " + this.hiddenNodes.size());
//            System.out.println();

            // Calculate the difference to see if we can add any links.
            if(totalPossibleLinks - this.links.size() == 0) {
                found = true;
            }
        }

        ArrayList<Link> allLinks;
        if(!found) {
            allLinks = new ArrayList<Link>();
            for(Node input : this.inNodes) {
                for(Node hidden : this.hiddenNodes) {
                    allLinks.add(new Link(input, hidden, -1));
                }
            }

            for(Node hidden : this.hiddenNodes) {
                for(Node output : this.outNodes) {
                    allLinks.add(new Link(hidden, output, -1));
                }
            }

            int start = this.inNodes.size() * this.outNodes.size();
            for(; start < this.links.size(); start++) {
                boolean removed = false;
                Link actualLink = this.links.get(start);
                for(int i = 0; !removed && i < allLinks.size(); i++) {
                    Link removeLink = allLinks.get(i);
                    if(actualLink.getInput().equals(removeLink.getInput()) &&
                            actualLink.getOutput().equals(removeLink.getOutput())) {
                        allLinks.remove(removeLink);
                        removed = true;
                    }
                }
            }

            Link toAdd = allLinks.get(random.nextInt(allLinks.size()));
            addLink(toAdd.getInput(), toAdd.getOutput());
        }
    }

    /**
     * Mutation that adds a node to the genome/network.
     */
    public void addNodeMutation(){
//        System.out.printf("\n\t\t----- Number of Nodes before call to addNodeMutation: %d",
//                this.getNumNodes());
//        int count = 0;
//        for(Link l : this.links) {
//            if(!l.isEnabled()) {
//                count++;
//            }
//        }
//        System.out.printf("\n\t\t----- Number of Disabled Links: %d", count);
        Random random = new Random();

        ArrayList<Link> inToOut = new ArrayList<Link>();
        for(Link link : this.links) {
            if(link.getInput().getLayer() == NodeLayer.INPUT && link.getOutput().getLayer() == NodeLayer.OUTPUT) {
                inToOut.add(link);
            }
        }
        Link link = inToOut.get(random.nextInt(inToOut.size()));
        link.setEnabled(false);
        Node newNode = new Node(this.getNumNodes());
        Node oldIn = link.getInput();
        Node oldOut = link.getOutput();
        this.addNode(newNode);
        this.addLink(oldIn,newNode);
        this.addLink(newNode,oldOut);
//        System.out.printf("\n\t\t----- Number of Nodes after call to addNodeMutation: %d",
//                this.getNumNodes());
//        count = 0;
//        for(Link l : this.links) {
//            if(!l.isEnabled()) {
//                count++;
//            }
//        }
//        System.out.printf("\n\t\t----- Number of Disabled Links: %d", count);
    }

/*    *//**
     * Returns a string representation of this network.
     * @return A String representation of this network.
     *//*
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
    }*/
}

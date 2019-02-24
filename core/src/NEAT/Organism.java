package NEAT;

public class Organism {
    private double fitness;
    private String name;
    private int generation;
    private Network network;
    private Genome genome;

    public Organism(double fitness,int generation){
        this.fitness = fitness;
        this.generation = generation;
        this.name = "I am Groot";
    }

    public void updateNetwork(){

    }

    @Override
    public String toString() {
        return this.name + " " + this.generation + " " + this.fitness;
    }
}

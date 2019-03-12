package NEAT;

public class Trial {
    public static void main(String[] args) {
        //Organism org1 = new Organism("I am Groot");
        //Organism org2 = new Organism("I am Steve Rogers");
        //System.out.println(org1);
        //System.out.println(org2);

        // Test print to make sure our population gets setup correctly.
        Population pop1 = new Population(2);
        for(Organism o : pop1.getOrganisms()) {
            System.out.println(o);
            System.out.println(o.getNetwork());
        }
    }
}

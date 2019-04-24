package NEAT;

/**
 * Created by Chance on 3/29/2019.
 */
public enum Constant {
    DISJOINT_COEFF (1),
    WEIGHT_COEFF (.5),
    COMPAT_THRESH(0.3),
    STALENESS_THRESH(15),
    CULL_THRESH(.5),

    // Next five are the individual chances for a mutation to occur.
    ADD_NODE_MUT(.05),
    ADD_LINK_MUT(.15),
    WEIGHT_MUT(.4),
    ENABLE_MUT(.2),
    REENABLE_MUT(.2),

    // Chance that a mutation will happen at all.
    MUT_THRESH(.75);

    private final double value;

    Constant(double value){
        this.value = value;
    }

    public double getValue(){
        return this.value;
    }
}

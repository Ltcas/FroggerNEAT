package NEAT;

/**
 * Created by Chance on 3/29/2019.
 */
public enum Constant {
    DISJOINT_COEFF (1),
    WEIGHT_COEFF (.5),
    COMPAT_THRESH(1),
    STALENESS_THRESH(15),
    CULL_THRESH(.5),

    // Next five are the individual chances for a mutation to occur.
    ADD_NODE_MUT(.01),
    ADD_LINK_MUT(.05),
    WEIGHT_MUT(.8),
    ENABLE_MUT(.3),
    REENABLE_MUT(.3),

    // Chance that a mutation will happen at all.
    MUT_THRESH(.25);

    private final double value;

    Constant(double value){
        this.value = value;
    }

    public double getValue(){
        return this.value;
    }
}

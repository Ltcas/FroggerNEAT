package NEAT;

/**
 * Created by Chance on 3/29/2019.
 */
public enum Constant {
    EXCESS_COEFF (1),
    DISJOINT_COEFF (1),
    WEIGHT_COEFF (.5),
    COMPAT_THRESH(3),
    STALENESS_THRESH(15),
    CULL_THRESH(.5),
    ADD_NODE_MUT(.5),
    ADD_LINK_MUT(.5),
    WEIGHT_MUT(.25),
    ENABLE_MUT(.25),
    MUT_THRESH(.25);

    private final double value;

    Constant(double value){
        this.value = value;
    }

    public double getValue(){
        return this.value;
    }
}

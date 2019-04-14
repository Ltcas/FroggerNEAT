package NEAT;

/**
 * Created by Chance on 3/29/2019.
 */
public enum Constant {
    DISJOINT_COEFF (1),
    WEIGHT_COEFF (.5),
    COMPAT_THRESH(0.3),
    STALENESS_THRESH(5),
    CULL_THRESH(.5),
    ADD_NODE_MUT(.1),
    ADD_LINK_MUT(.8),
    WEIGHT_MUT(.35),
    ENABLE_MUT(.25),
    REENABLE_MUT(.25),
    MUT_THRESH(.75);

    private final double value;

    Constant(double value){
        this.value = value;
    }

    public double getValue(){
        return this.value;
    }
}

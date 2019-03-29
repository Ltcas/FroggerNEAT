package NEAT;

/**
 * Created by Chance on 3/29/2019.
 */
public enum Constant {
    EXCESS_COEFF (2.0),
    DISJOINT_COEFF (2.0),
    WEIGHT_COEFF (1),
    COMPAT_THRESH(.5),
    STALENESS_THRESH(15),
    ADD_NODE_MUT(.5),
    ADD_LINK_MUT(.5),
    WEIGHT_MUT(.25),
    ENABLE_MUT(.25);

    private final double value;

    Constant(double value){
        this.value = value;
    }

    public double getValue(){
        return this.value;
    }
}

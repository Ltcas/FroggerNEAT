package NEAT;

/**
 * Constant enumerations we're using so they're located in one single location.
 * @author Chance Simmons and Brandon Townsend
 * @version 9 May 2019
 */
public enum Constant {
    DISJOINT_COEFF (1),
    WEIGHT_COEFF (.5),
    COMPAT_THRESH(0.3),
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

    /** The value of this constant */
    private final double value;

    /**
     * Constructor for the constant
     * @param value the value for the constant
     */
    Constant(double value){
        this.value = value;
    }

    /**
     * Returns the value of this constant
     * @return the value of the constant
     */
    public double getValue(){
        return this.value;
    }
}

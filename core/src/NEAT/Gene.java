package NEAT;

/**
 * Models a gene that will belong to a genome. Each gene should have a specific or no traits.
 * @author Chance Simmons and Brandon Townsend
 * @version 13 March 2019
 */
public class Gene {

    /** todo */
    private Link link;

    /** todo */
    private int innovationNum;

    /** todo */
    private double mutationNum;

    /** Used to see whether this gene is enabled or not. */
    private boolean enabled;

    /**
     * Used to see whether or not this gene is frozen. If it is, the link's weight cannot be
     * mutated.
     */
    private boolean frozen;

    /**
     * Constructor for this Gene object.
     */
    public Gene() {

    }

    /**
     * Returns this genes link.
     * @return This genes link.
     */
    public Link getLink() {
        return link;
    }

    /**
     * Sets this genes link to the specified link.
     * @param link The specified link to be set.
     */
    public void setLink(Link link) {
        this.link = link;
    }

    /**
     * Returns this genes innovation number.
     * @return This genes innovation number.
     */
    public int getInnovationNum() {
        return innovationNum;
    }

    /**
     * Sets this genes innovation number to the specified number.
     * @param innovationNum The specified number to be set.
     */
    public void setInnovationNum(int innovationNum) {
        this.innovationNum = innovationNum;
    }

    /**
     * Returns this genes mutation number.
     * @return This genes mutation number.
     */
    public double getMutationNum() {
        return mutationNum;
    }

    /**
     * Sets this genes mutation number to the specified number.
     * @param mutationNum The specified number to be set.
     */
    public void setMutationNum(double mutationNum) {
        this.mutationNum = mutationNum;
    }

    /**
     * Returns whether or not this gene is enabled.
     * @return True if this gene is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set this gene to be enabled or disabled.
     * @param enabled If true, the gene is enabled. If false, the gene is disabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns whether or not this gene is frozen.
     * @return True if this gene is frozen, false otherwise.
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Sets this gene to be frozen or thawed.
     * @param frozen If true, the gene is frozen. If false, the gene is thawed.
     */
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

}

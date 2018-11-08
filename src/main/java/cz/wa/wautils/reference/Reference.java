package cz.wa.wautils.reference;

/**
 * Reference to an object. The object can be null.
 * 
 * @author Ondrej Milenovsky
 */
public class Reference<O> {

    protected O referent;

    public Reference() {
    }

    public Reference(O referent) {
        this.referent = referent;
    }

    public O getReferent() {
        return referent;
    }

    public void setReferent(O referent) {
        this.referent = referent;
    }

}

package cz.wa.wautils.reference;

/**
 * Injector that holds reference and object to be injected.
 * Injects the object to the reference when called inject().
 * Reference must not be null when injecting, the object can be null.
 * 
 * @author Ondrej Milenovsky
 */
public class Injector<O> {
    protected Reference<O> reference;
    protected O object;

    public Injector() {
    }

    public Injector(Reference<O> reference, O object) {
        this.reference = reference;
        this.object = object;
    }

    public Reference<O> getReference() {
        return reference;
    }

    public void setReference(Reference<O> reference) {
        this.reference = reference;
    }

    public O getObject() {
        return object;
    }

    public void setObject(O object) {
        this.object = object;
    }

    /**
     * Injects the object to the reference, reference must not be null.
     */
    public void inject() {
        reference.setReferent(object);
    }

}

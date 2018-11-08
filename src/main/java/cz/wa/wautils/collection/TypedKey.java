package cz.wa.wautils.collection;

import java.io.Serializable;

/**
 * String key with class for casting.
 * 
 * @author Ondrej Milenovsky
 * */
public class TypedKey<C> implements Serializable {

    private static final long serialVersionUID = 6719089248141358343L;

    public final String key;

    public TypedKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TypedKey other = (TypedKey) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

}

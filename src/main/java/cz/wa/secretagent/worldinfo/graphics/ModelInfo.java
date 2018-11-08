package cz.wa.secretagent.worldinfo.graphics;

import org.apache.commons.lang.Validate;

import cz.wa.secretagent.world.ObjectModel;

/**
 * Info about model for tile or entity.
 * 
 * @author Ondrej Milenovsky
 */
public class ModelInfo {
    private final int offset;
    private final ObjectModel model;

    /**
     * Animated model with offset
     * @param offset frame offset >= 0
     * @param model the model
     */
    public ModelInfo(int offset, ObjectModel model) {
        Validate.isTrue(offset >= 0, "offset must be >= 0");
        Validate.notNull(model, "model is null");
        this.offset = offset;
        this.model = model;
    }

    /**
     * Simple model
     * @param model the model
     */
    public ModelInfo(ObjectModel model) {
        Validate.notNull(model, "model is null");
        this.offset = -1;
        this.model = model;
    }

    public int getOffset() {
        return offset;
    }

    public ObjectModel getModel() {
        return model;
    }

    public boolean isAnimated() {
        return offset >= 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((model == null) ? 0 : model.hashCode());
        result = prime * result + offset;
        return result;
    }

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
        ModelInfo other = (ModelInfo) obj;
        if (model == null) {
            if (other.model != null) {
                return false;
            }
        } else if (!model.equals(other.model)) {
            return false;
        }
        if (offset != other.offset) {
            return false;
        }
        return true;
    }

}

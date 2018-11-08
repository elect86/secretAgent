package cz.wa.secretagent.io.tiles.singleproperties;

import java.util.Collections;
import java.util.List;

/**
 * Info about single entity, that is created from a tile. 
 * 
 * @author Ondrej Milenovsky
 */
public class EntityProperties {
    private final List<String> args;

    public EntityProperties(List<String> args) {
        this.args = Collections.unmodifiableList(args);
    }

    public List<String> getArgs() {
        return args;
    }
}

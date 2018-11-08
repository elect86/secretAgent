package cz.wa.secretagent.view.renderer;

import java.io.Serializable;

/**
 * Some renderer, can render whole world, part of it or single entity.
 * 
 * @author Ondrej Milenovsky
 */
public interface Renderer extends Serializable {
    void init();

    void dispose();

}

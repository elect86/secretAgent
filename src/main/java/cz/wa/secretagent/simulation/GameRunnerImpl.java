package cz.wa.secretagent.simulation;

import cz.wa.secretagent.view.renderer.SAMRenderer;
import cz.wa.secretagent.worldinfo.WorldHolder;
import cz.wa.wautils.swing.listener.RunnableLog;
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import secretAgent.world.SamWorld;

import java.io.Serializable;

/**
 * Game runner that starts new thread for game simulation and rendering.
 * In single step processes input, moves the world, renders the frame (all in single thread).
 *
 * @author Ondrej Milenovsky
 */
public class GameRunnerImpl implements GameRunner, Serializable {
    private static final long serialVersionUID = -5925062471372147489L;

    private static final Logger logger = LoggerFactory.getLogger(GameRunnerImpl.class);

    public static final String THREAD_NAME = "Simulation and renderer";

    private long maxStepMs = 40;

    private WorldHolder worldHolder;
    private SAMRenderer renderer;
    private GameController controller;
    private GameSimulator simulator;

    private transient Runnable runFirst;
    private transient Thread thread;
    private transient boolean running;

    @Override
    public void init() {
        running = false;
        thread = new Thread(new RunnableLog() {
            @Override
            public void run2() {
                runGame();
            }
        }, THREAD_NAME);
    }

    public long getMaxStepMs() {
        return maxStepMs;
    }

    public void setMaxStepMs(long maxStepMs) {
        this.maxStepMs = maxStepMs;
    }

    public WorldHolder getWorldHolder() {
        return worldHolder;
    }

    @Required
    public void setWorldHolder(WorldHolder worldHolder) {
        this.worldHolder = worldHolder;
    }

    public SAMRenderer getRenderer() {
        return renderer;
    }

    @Required
    public void setRenderer(SAMRenderer renderer) {
        this.renderer = renderer;
    }

    public GameController getController() {
        return controller;
    }

    @Required
    public void setController(GameController controller) {
        this.controller = controller;
    }

    public GameSimulator getSimulator() {
        return simulator;
    }

    @Required
    public void setSimulator(GameSimulator simulator) {
        this.simulator = simulator;
    }

    @Override
    public void setRunFirst(Runnable r) {
        runFirst = r;
    }

    private void runGame() {
        try {
            if (renderer != null) {
                renderer.init();
                if (!renderer.isInitialized()) {
                    return;
                }
            }
            if (runFirst != null) {
                runFirst.run();
            }
        } catch (Throwable e) {
            logger.error("", e);
        }
        long lastTime = System.currentTimeMillis();
        while (running) {
            lastTime = nextStep(lastTime);
        }
    }

    private long nextStep(long lastTime) {
        long currentTime = System.currentTimeMillis();
        long timeDiff = FastMath.min(currentTime - lastTime, maxStepMs);
        double timeS = timeDiff / 1000.0;
        SamWorld world = worldHolder.getWorld();
        boolean activeWorld = (world != null) && world.isRunning();

        // first add the time to the world
        if (activeWorld) {
            world.addSimTimeMs(timeDiff);
        }
        // process controller
        processInput(timeS);
        // move the world
        if (activeWorld) {
            moveWorld(timeS);
        }
        // finally render screen
        render();

        lastTime = currentTime;
        return lastTime;
    }

    @Override
    public void startGame() {
        running = true;
        thread.start();
    }

    @Override
    public void endGame() {
        running = false;
    }

    private void processInput(double timeS) {
        if (controller != null) {
            try {
                controller.processInput(timeS);
            } catch (Throwable e) {
                logger.error("", e);
            }
        }
    }

    private void moveWorld(double timeS) {
        if (simulator != null) {
            try {
                simulator.move(timeS);
            } catch (Throwable e) {
                logger.error("", e);
            }
        }
    }

    private void render() {
        if (renderer != null) {
            try {
                renderer.draw(worldHolder);
            } catch (Throwable e) {
                logger.error("", e);
            }
            if (renderer.isCloseRequested()) {
                System.exit(0);
            }
        }
    }

}

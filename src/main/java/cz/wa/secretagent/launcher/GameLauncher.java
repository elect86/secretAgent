package cz.wa.secretagent.launcher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cz.wa.secretagent.io.FileSettings;
import cz.wa.secretagent.io.SAMIO;
import cz.wa.secretagent.menu.MenuHolder;
import cz.wa.secretagent.menu.MenuKeys;
import cz.wa.secretagent.simulation.GameRunner;
import cz.wa.secretagent.utils.ContextWrapper;
import cz.wa.secretagent.utils.PropertiesLoader;
import cz.wa.secretagent.utils.lwjgl.KeysUtils;
import cz.wa.secretagent.utils.lwjgl.TextureLoader;
import cz.wa.wautils.collection.TypedKey;
import cz.wa.wautils.io.FileObjectUtils;
import secretAgent.game.PlayerHolder;
import secretAgent.game.PlayerKeys;
import secretAgent.game.starter.MapStarter;

/**
 * Launches the game.
 *
 * @author Ondrej Milenovsky
 */
public class GameLauncher {
    private static final Logger logger = LoggerFactory.getLogger(GameLauncher.class);

    public static final TypedKey<GameRunner> GAME_RUNNER = new TypedKey<>("gameRunner");
    public static final TypedKey<SAMIO> SAM_IO = new TypedKey<>("samIO");
    public static final TypedKey<PlayerHolder> PLAYER_HOLDER = new TypedKey<>("playerHolder");
    public static final TypedKey<MenuHolder> MENU_HOLDER = new TypedKey<>("menuHolder");
    public static final TypedKey<FileSettings> FILE_SETTINGS = new TypedKey<>("fileSettings");
    public static final TypedKey<MapStarter> MAP_STARTER = new TypedKey<>("mapStarter");

    // files used for caching
    public static final String CONTEXT_CACHE_FILE = "cache/game_context.dat";

    private final String appContext;

    public GameLauncher(String appContext) {
        this.appContext = appContext;
    }

    public void launch(boolean loadingFrame, CacheAction cacheAction) {
        InitFrame frame = null;
        if (loadingFrame) {
            frame = new InitFrame();
            frame.setText("Initializing...");
        }

        // load the context
        File cacheFile = new File(CONTEXT_CACHE_FILE);
        if ((cacheAction == CacheAction.DELETE) || (cacheAction == CacheAction.RENEW)) {
            FileUtils.deleteQuietly(cacheFile);
        }

        // try load cached context
        ParsedContext context = null;
        if ((cacheAction == CacheAction.USE_OR_CREATE) && cacheFile.isFile()) {
            frame.setText("Loading cached game context...");
            context = loadCachedContext(cacheFile);
        }

        // parse new context
        if (context == null) {
            frame.setText("Parsing Spring xml files...");
            ContextWrapper contextWrapper = new ContextWrapper(
                    new FileSystemXmlApplicationContext(appContext));
            context = parseBeans(contextWrapper);
        }

        // create cache
        if ((cacheAction == CacheAction.USE_OR_CREATE) || (cacheAction == CacheAction.RENEW)) {
            frame.setText("Creating cached game context...");
            createCachedContext(context, cacheFile);
        }

        if (loadingFrame) {
            frame.setText("Creating game classes...");
        }
        logger.info("Creating game classes...");

        // init game classes
        initClasses(context);

        // start the game
        logger.info("Initializing display...");
        context.gameRunner.startGame();

        if (loadingFrame) {
            frame.setVisible(false);
            frame.dispose();
            frame = null;
        }
    }

    private ParsedContext parseBeans(ContextWrapper context) {
        GameRunner gameRunner = context.getBean(GAME_RUNNER);
        PlayerHolder playerHolder = context.getBean(PLAYER_HOLDER);
        MenuHolder menuHolder = context.getBean(MENU_HOLDER);
        FileSettings fileSettings = context.getBean(FILE_SETTINGS);
        SAMIO io = context.getBean(SAM_IO);
        MapStarter mapStarter = context.getBean(MAP_STARTER);

        return new ParsedContext(gameRunner, playerHolder, menuHolder, fileSettings, io, mapStarter);
    }

    private void initClasses(final ParsedContext context) {
        FileSettings fileSettings = context.fileSettings;
        PropertiesLoader.loadPropertiesToObject(fileSettings.settings2dFile, context.samIO.getSettings2d());
        PropertiesLoader.loadPropertiesToObject(fileSettings.gameSettingsFile,
                context.samIO.getGameSettings());
        loadPlayerKeys(context.playerHolder, fileSettings);
        loadMenuKeys(context.menuHolder, fileSettings);

        GameRunner gameRunner = context.gameRunner;
        gameRunner.init();
        gameRunner.setRunFirst(new Runnable() {
            @Override
            public void run() {
                logger.info("Loading initial data...");
                TextureLoader.setBgColor(new Color(0, 0, 0, 0));
                loadDefaultGraphics(context.samIO);
                context.mapStarter.startMainMenu();
                logger.info("Game initialized.");
            }
        });
    }

    private void loadPlayerKeys(PlayerHolder playerHolder, FileSettings fileSettings) {
        PlayerKeys keys = new PlayerKeys();
        if (fileSettings.playerKeysFile == null) {
            return;
        }
        File file = new File(fileSettings.playerKeysFile);
        try {
            KeysUtils.loadKeys(file, keys);
        } catch (IOException e) {
            logger.error("Cannot load keys from file: " + file.getAbsolutePath(), e);
        }
        playerHolder.setKeys(keys);
    }

    private void loadMenuKeys(MenuHolder menuHolder, FileSettings fileSettings) {
        MenuKeys keys = new MenuKeys();
        if (fileSettings.menuKeysFile == null) {
            return;
        }
        File file = new File(fileSettings.menuKeysFile);
        try {
            KeysUtils.loadKeys(file, keys);
        } catch (IOException e) {
            logger.error("Cannot load keys from file: " + file.getAbsolutePath(), e);
        }
        menuHolder.setKeys(keys);
    }

    private void loadDefaultGraphics(SAMIO io) {
        try {
            io.loadDefaultGraphics();
        } catch (IOException e) {
            logger.error("Error loading default graphics.", e);
        }
    }

    private ParsedContext loadCachedContext(File cacheFile) {
        logger.info("Loading cached context...");
        try {
            return (ParsedContext) FileObjectUtils.readObjectFromFile(cacheFile);
        } catch (ClassNotFoundException | IOException e) {
            logger.error("Failed to read cached context from file: " + cacheFile, e);
            return null;
        }
    }

    private void createCachedContext(ParsedContext context, File cacheFile) {
        logger.info("Creating cached context...");
        try {
            FileObjectUtils.writeObjectToFile(cacheFile, context);
        } catch (IOException e) {
            logger.error("Failed to create cached context to file: " + cacheFile, e);
        }
    }

    /**
     * Class holding all context beans.
     */
    private static class ParsedContext implements Serializable {
        private static final long serialVersionUID = -5357344627896702434L;

        private final GameRunner gameRunner;
        private final PlayerHolder playerHolder;
        private final MenuHolder menuHolder;
        private final FileSettings fileSettings;
        private final SAMIO samIO;
        private final MapStarter mapStarter;

        public ParsedContext(GameRunner gameRunner, PlayerHolder playerHolder, MenuHolder menuHolder,
                FileSettings fileSettings, SAMIO io, MapStarter mapStarter) {
            this.gameRunner = gameRunner;
            this.playerHolder = playerHolder;
            this.menuHolder = menuHolder;
            this.fileSettings = fileSettings;
            this.samIO = io;
            this.mapStarter = mapStarter;
        }
    }

    /**
     * Frame displayed at start of the game. 
     */
    private class InitFrame extends JFrame {
        private static final long serialVersionUID = -8806569485541236883L;

        private JLabel label;

        public InitFrame() {
            super("Secret Agent");
            setBounds(400, 300, 230, 72);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);
            initComponents();
            setVisible(true);
        }

        public void setText(String text) {
            label.setText(text);
        }

        private void initComponents() {
            JPanel p1 = new JPanel();
            p1.setBorder(new BevelBorder(BevelBorder.RAISED));
            p1.setLayout(new BorderLayout());
            add(p1, BorderLayout.CENTER);

            JPanel p2 = new JPanel();
            p2.setBorder(new EmptyBorder(8, 8, 0, 8));
            p2.setLayout(new GridLayout(2, 1));
            label = new JLabel();
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setBorder(new EmptyBorder(0, 0, 3, 0));
            p2.add(label);
            p2.add(new JLabel(" "));

            p1.add(p2, BorderLayout.CENTER);
        }
    }

}

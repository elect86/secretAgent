//package cz.wa.secretagent.launcher;
//
//import java.io.IOException;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import cz.wa.wautils.swing.component.WaDialogs;
//import secretAgent.launcher.CacheAction;
//import secretAgent.launcher.GameLauncher;
//
///**
// * The main class.
// *
// * @author Ondrej Milenovsky
// */
//public class SecretAgentMain {
//    private static final Logger logger = LoggerFactory.getLogger(SecretAgentMain.class);
//
//    public static final String VERSION = "0.03";
//
//    public static final String GAME_APP_CONTEXT = "classpath:game_context.xml";
//
//    public static final String DISABLE_LOADING_FRAME_ARG = "-noloadingframe";
//    public static final String DELETE_CONTEXT_CACHE_ARG = "-deletecache";
//    public static final String CREATE_CONTEXT_CACHE_ARG = "-createcache";
//    public static final String IGNORE_CONTEXT_CACHE_ARG = "-ignorecache";
//
//    private final String appContext;
//
//    public SecretAgentMain(String appContext) {
//        this.appContext = appContext;
//    }
//
//    private void startGame(String[] args) {
//        boolean loadingFrame = true;
//        CacheAction cacheAction = CacheAction.USE_OR_CREATE;
//        for (String arg : args) {
//            if (arg.equalsIgnoreCase(DISABLE_LOADING_FRAME_ARG)) {
//                loadingFrame = false;
//            } else if (arg.equalsIgnoreCase(DELETE_CONTEXT_CACHE_ARG)) {
//                cacheAction = CacheAction.DELETE;
//            } else if (arg.equalsIgnoreCase(CREATE_CONTEXT_CACHE_ARG)) {
//                cacheAction = CacheAction.RENEW;
//            } else if (arg.equalsIgnoreCase(IGNORE_CONTEXT_CACHE_ARG)) {
//                cacheAction = CacheAction.IGNORE;
//            }
//        }
//        new GameLauncher(appContext).launch(loadingFrame, cacheAction);
//    }
//
//    public static void main(String[] args) throws IOException {
//        System.out.println("Secret Agent Remake by Wanucha v" + VERSION);
//        System.out.println(
//                "\tuse argument " + DISABLE_LOADING_FRAME_ARG + " to disable the first loading window");
//        System.out.println(
//                "\tThe game context is by default cached, to manipulate the cache, use these arguments:");
//        System.out.println("\t" + CREATE_CONTEXT_CACHE_ARG + " - create new cache");
//        System.out.println("\t" + DELETE_CONTEXT_CACHE_ARG + " - delete cache");
//        System.out.println("\t" + IGNORE_CONTEXT_CACHE_ARG + " - ignore cache");
//        try {
//            new SecretAgentMain(GAME_APP_CONTEXT).startGame(args);
//        } catch (Throwable e) {
//            logger.error("", e);
//            try {
//                WaDialogs
//                        .showDialog(null, "Error starting application",
//                                "<html>" + limitString(e.getMessage(), 130).replace("\n", "<br>")
//                                        + "<br><br>Check 'log/log.txt' for full error report.</html>",
//                                "Exit");
//            } catch (Throwable e2) {
//                logger.error("Error displaying error window :-)", e2);
//            }
//            System.exit(0);
//        }
//    }
//
//    private static String limitString(String s, int maxLength) {
//        if (s == null) {
//            return "[no message]";
//        }
//        if (s.length() > maxLength) {
//            return s.substring(0, maxLength - 3) + "...";
//        } else {
//            return s;
//        }
//    }
//}

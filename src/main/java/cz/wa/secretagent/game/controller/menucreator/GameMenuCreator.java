//package cz.wa.secretagent.game.controller.menucreator;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.game.starter.MapStarter;
//import cz.wa.secretagent.menu.builder.TextButtonDescriptor;
//import cz.wa.secretagent.menu.window.GFrame;
//import cz.wa.secretagent.menu.window.component.selectable.GButtonListener;
//
///**
// * Methods for creating menus in game. Caches all frames.
// *
// * @author Ondrej Milenovsky
// */
//public class GameMenuCreator extends GeneralMenuCreator implements Serializable {
//    private static final long serialVersionUID = 114350147422291071L;
//
//    private MapStarter mapStarter;
//
//    private transient GFrame quitToMenuDialog;
//    private transient GFrame levelMainMenu;
//    private transient GFrame islandMainMenu;
//
//    private void init() {
//        if (quitToMenuDialog == null) {
//            quitToMenuDialog = createQuitToMenuDialog();
//        }
//    }
//
//    public GFrame getLevelMainMenu() {
//        if (levelMainMenu == null) {
//            levelMainMenu = createLevelMainMenu();
//        }
//        return levelMainMenu;
//    }
//
//    public GFrame getIslandMainMenu() {
//        if (islandMainMenu == null) {
//            islandMainMenu = createIslandMainMenu();
//        }
//        return islandMainMenu;
//    }
//
//    private GFrame createLevelMainMenu() {
//        init();
//        final GFrame returnToIslandDialog = createReturnToIslandDialog();
//
//        // buttons
//        List<TextButtonDescriptor> buttons = new ArrayList<TextButtonDescriptor>();
//        buttons.add(createSettingsButton());
//        buttons.add(new TextButtonDescriptor("Return to island", new GButtonListener() {
//            @Override
//            public void actionPerformed() {
//                if (gameSettings.confirmDialogs) {
//                    returnToIslandDialog.setSelectedIndex(0);
//                    worldHolder.getMenuHolder().addFrame(returnToIslandDialog);
//                } else {
//                    quitToIsland();
//                }
//            }
//        }));
//        buttons.add(createQuitToMenuButton());
//        buttons.add(createExitButton());
//
//        // frame
//        GButtonListener closeListener = new GButtonListener() {
//            @Override
//            public void actionPerformed() {
//                worldHolder.getMenuHolder().removeTopFrame();
//                worldHolder.getWorld().setRunning(true);
//            }
//        };
//        GFrame dialog = dialogBuilder.createDialog("Game paused", buttons, frameColor, closeListener, false);
//
//        return dialog;
//    }
//
//    private void quitToIsland() {
//        mapStarter.startIslandMap();
//        worldHolder.getMenuHolder().removeAllFrames();
//    }
//
//    private TextButtonDescriptor createExitButton() {
//        return new TextButtonDescriptor("Exit the game", new GButtonListener() {
//            @Override
//            public void actionPerformed() {
//                if (gameSettings.confirmDialogs) {
//                    GFrame exitDialog = getExitDialog();
//                    exitDialog.setSelectedIndex(0);
//                    worldHolder.getMenuHolder().addFrame(exitDialog);
//                } else {
//                    exitGame();
//                }
//            }
//        });
//    }
//
//    private TextButtonDescriptor createQuitToMenuButton() {
//        return new TextButtonDescriptor("Quit to main menu", new GButtonListener() {
//            @Override
//            public void actionPerformed() {
//                if (gameSettings.confirmDialogs) {
//                    quitToMenuDialog.setSelectedIndex(0);
//                    worldHolder.getMenuHolder().addFrame(quitToMenuDialog);
//                } else {
//                    quitToMenu();
//                }
//            }
//        });
//    }
//
//    private void quitToMenu() {
//        worldHolder.getMenuHolder().removeAllFrames();
//        mapStarter.startMainMenu();
//    }
//
//    private TextButtonDescriptor createSettingsButton() {
//        return new TextButtonDescriptor("Settings", new GButtonListener() {
//            @Override
//            public void actionPerformed() {
//                GFrame settingsMenu = getSettingsMenu();
//                settingsMenu.setSelectedIndex(0);
//                worldHolder.getMenuHolder().addFrame(settingsMenu);
//            }
//        });
//    }
//
//    private GFrame createIslandMainMenu() {
//        init();
//        // buttons
//        List<TextButtonDescriptor> buttons = new ArrayList<TextButtonDescriptor>();
//        buttons.add(createSettingsButton());
//        buttons.add(createQuitToMenuButton());
//        buttons.add(createExitButton());
//
//        // frame
//        GButtonListener closeListener = new GButtonListener() {
//            @Override
//            public void actionPerformed() {
//                worldHolder.getMenuHolder().removeTopFrame();
//                worldHolder.getWorld().setRunning(true);
//            }
//        };
//        GFrame dialog = dialogBuilder.createDialog("Game paused", buttons, frameColor, closeListener, false);
//
//        return dialog;
//    }
//
//    private GFrame createQuitToMenuDialog() {
//        return createConfirmDialog("Quit to main menu?", new GButtonListener() {
//            @Override
//            public void actionPerformed() {
//                quitToMenu();
//            }
//        }, frameColor);
//    }
//
//    private GFrame createReturnToIslandDialog() {
//        return createConfirmDialog("Return to island map?", new GButtonListener() {
//            @Override
//            public void actionPerformed() {
//                quitToIsland();
//            }
//        }, frameColor);
//    }
//
//    public MapStarter getMapStarter() {
//        return mapStarter;
//    }
//
//    @Required
//    public void setMapStarter(MapStarter mapStarter) {
//        this.mapStarter = mapStarter;
//    }
//
//}
